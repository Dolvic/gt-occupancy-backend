package gatech.mobile.occupancy

import com.fasterxml.jackson.module.kotlin.KotlinModule
import gatech.mobile.occupancy.startup.BuildingsInitializer
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.client.RestTemplateCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@SpringBootApplication
@EnableConfigurationProperties
@EnableAsync
class Application
{
    @Bean
    fun kotlinModule() = KotlinModule()

    @Bean
    fun corsConfigurer() = object : WebMvcConfigurerAdapter()
    {
        override fun addCorsMappings(registry: CorsRegistry)
        {
            super.addCorsMappings(registry)
            registry.addMapping("/**")
                    .allowedMethods("*")
        }
    }

    @Bean
    fun restTemplateCustomizer() = RestTemplateCustomizer {
        val clientFactory = HttpComponentsClientHttpRequestFactory()
        clientFactory.setConnectTimeout(5)
        it.requestFactory = clientFactory
    }

    @Bean
    fun mongoTypeMapper() = DefaultMongoTypeMapper(null)

    companion object
    {
        @JvmStatic fun main(args: Array<String>)
        {
            val context = SpringApplication.run(Application::class.java, *args)
            val buildingsInitializer = context.getBean(BuildingsInitializer::class.java)
            buildingsInitializer.addMissingBuildings()
        }
    }
}
