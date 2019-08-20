package cc.before30.kotex.demokt.domain

import org.elasticsearch.index.query.QueryBuilders.matchQuery
import org.elasticsearch.search.aggregations.AggregationBuilder
import org.elasticsearch.search.aggregations.AggregationBuilders
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms
import org.elasticsearch.search.aggregations.metrics.tophits.InternalTopHits
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder
import org.springframework.stereotype.Service

/**
 *
 * MethodDescriptionService
 *
 * @author before30
 * @since 2019-08-17
 */
@Service
class MethodDescriptionService(val methodDescriptionRepository: MethodDescriptionRepository,
                               val elasticsearchTemplate: ElasticsearchTemplate) {

    fun insert(methodDescription: MethodDescription): MethodDescription {
        return methodDescriptionRepository.save(methodDescription)
    }

    fun findAll(): List<MethodDescription> {
        return methodDescriptionRepository.findAll().asSequence().toList()
    }

    fun search(query: String): List<String> {
        val searchQuery = NativeSearchQueryBuilder()
                .withQuery(matchQuery("methodName.ngram", query))
                .build()

        val aggregation = AggregationBuilders
                .terms("dedup")
                .field("methodName")
                .subAggregation(
                        AggregationBuilders
                                .topHits("dedup_docs")
                                .size(1))
        val response = elasticsearchTemplate.client
                .prepareSearch("benedict")
                .setSize(0)
                .setTypes("_doc")
                .setQuery(searchQuery.query)
                .addAggregation(aggregation)
                .execute()
                .actionGet()

        val results = response.aggregations
                .flatMap { (it as StringTerms).buckets }
                .flatMap { it.aggregations }
                .flatMap { (it as InternalTopHits).hits.hits.toList() }
                .map { it.sourceAsMap.get("methodName").toString() }

        return results
    }
}