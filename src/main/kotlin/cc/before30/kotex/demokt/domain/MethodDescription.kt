package cc.before30.kotex.demokt.domain

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

/**
 *
 * MethodDescription
 *
 * @author before30
 * @since 2019-08-17
 */
@Document(indexName = "benedict", type = "method")
class MethodDescription(
        @Id
        var methodId: String? = null,
        val methodName: String,
        val fullDescription: String
) {
    constructor(): this(null, "", "")

    override fun toString(): String {
        return "methodName=$methodName, fullDescription=$fullDescription"
    }
}