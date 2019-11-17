package cc.kpug.benedict.muffin.config

import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.context.annotation.Configuration

@EnableWebMvc
@Configuration
class WebConfiguration: WebMvcConfigurer {
 
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
    }
}