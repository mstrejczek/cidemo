package pl.marekstrejczek.ci.democlient.load

import mu.KLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class LoadController(val loadService: LoadService) {
    companion object: KLogging()

    @PostMapping("/load")
    fun load(@RequestBody description: LoadDescription) {
        logger.info("Received load request: $description")
        loadService.generateLoad(description)
    }

}