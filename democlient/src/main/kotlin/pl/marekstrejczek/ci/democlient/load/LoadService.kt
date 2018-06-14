package pl.marekstrejczek.ci.democlient.load

import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import pl.marekstrejczek.ci.demo.event.Event
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executor
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

@Service
class LoadService(val executor: Executor, @Value("\${target.url}") val url: String) {
    companion object: KLogging()

    fun generateLoad(description: LoadDescription) {
        executor.execute(GenerateLoadTask(description, url, executor))
    }


    class GenerateLoadTask(private val description: LoadDescription, private val url: String, private val executor: Executor) : Runnable {
        override fun run() {
            val startTime = LocalDateTime.now()
            logger.info("Starting load generation according to $description")
            val webClient = WebClient.create(url)
            val latch = CountDownLatch(description.totalRequests)
            val loadContext = LoadContext(latch)
            val intervalMilliseconds = 1000L / description.perSecondRequestCount
            for (i in 0..description.totalRequests) {
                try {
                    executor.execute(SendRequestTask(description, webClient, i, loadContext))
                } catch (e : RejectedExecutionException) {
                    logger.warn("Could not send request $i", e)
                    loadContext.incFailure()
                }
                TimeUnit.MILLISECONDS.sleep(intervalMilliseconds)
            }
            logger.info("All load generated")
            latch.await()
            val endTime = LocalDateTime.now()
            val elapsed = Duration.between(startTime, endTime)
            logger.info("Outcome: $loadContext, duration: $elapsed")
        }
    }

    class SendRequestTask(private val description: LoadDescription, private val webClient: WebClient, private val sequenceNum: Int, private val loadContext: LoadContext) : Runnable {
        override fun run() {
            logger.info("Sending request for sequence number $sequenceNum")
            val event = Event(LocalDateTime.now(), "DEMO_EVENT", mapOf(Pair("loadId", description.loadId), Pair("sequenceId", sequenceNum.toString())))
            webClient.post()
                    .uri("/events")
                    .body(BodyInserters.fromObject(event))
                    .exchange()
                    .onErrorMap(this::onError)
                    .map(this::onSuccess)
                    .block()
        }

        private fun onError(t: Throwable): Throwable {
            loadContext.incFailure()
            return t
        }

        private fun onSuccess(response: Any) {
            loadContext.incSuccess()
        }
    }

    class LoadContext(val latch: CountDownLatch) {
        private var successCount = AtomicLong(0)
        private var failureCount = AtomicLong(0)

        fun incSuccess() {
            successCount.getAndIncrement()
            latch.countDown()
        }

        fun incFailure() {
            failureCount.getAndIncrement()
            latch.countDown()
        }

        override fun toString(): String {
            return "LoadContext(successCount=$successCount, failureCount=$failureCount)"
        }
    }
}