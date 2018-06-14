package pl.marekstrejczek.ci.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.aws.jdbc.config.annotation.EnableRdsInstance
import org.springframework.context.annotation.ImportResource

@SpringBootApplication
@ImportResource("classpath:sql/queries-bean.xml")
@EnableRdsInstance(
        dbInstanceIdentifier = "strejcze2",
        username = "strejcze",
        password = "strejcze1234",
        readReplicaSupport = true)
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}
