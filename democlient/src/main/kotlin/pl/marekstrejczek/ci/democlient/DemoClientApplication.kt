package pl.marekstrejczek.ci.democlient

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

@SpringBootApplication
class DemoClientApplication {

    @Bean
    fun asyncExecutor(): Executor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 25
        executor.maxPoolSize = 50
        executor.setQueueCapacity(500)
        executor.setThreadNamePrefix("LoadGenerator-")
        executor.initialize()
        return executor
    }
}

fun main(args: Array<String>) {
    runApplication<DemoClientApplication>(*args)
}

