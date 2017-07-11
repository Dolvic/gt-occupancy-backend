package gatech.mobile.occupancy.startup

import mu.KLogging
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.Ordered
import org.springframework.data.mongodb.core.convert.MappingMongoConverter
import org.springframework.data.mongodb.core.convert.MongoTypeMapper
import org.springframework.stereotype.Component
import javax.annotation.Priority

@Priority(Ordered.HIGHEST_PRECEDENCE)
@Component
class MongoInitializer(private val converter: MappingMongoConverter, private val typeMapper: MongoTypeMapper)
    : ApplicationRunner
{
    companion object : KLogging()

    override fun run(args: ApplicationArguments?)
    {
        logger.debug { "Removing type mapping from Spring Mongo." }
        converter.typeMapper = typeMapper
    }
}
