package gatech.mobile.occupancy

import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class Application
{
    @Bean
    fun kotlinModule() = KotlinModule()

    companion object
    {
        @JvmStatic fun main(args: Array<String>)
        {
            SpringApplication.run(Application::class.java, *args)
        }
    }
}
