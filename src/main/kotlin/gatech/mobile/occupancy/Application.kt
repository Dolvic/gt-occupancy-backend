package gatech.mobile.occupancy

import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@SpringBootApplication
@EnableConfigurationProperties
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
        }
    }

    @Bean
    fun mongoTypeMapper() = DefaultMongoTypeMapper(null)

    companion object
    {
        @JvmStatic fun main(args: Array<String>)
        {
            SpringApplication.run(Application::class.java, *args)
        }
    }
}
