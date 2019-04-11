package sfg.beerworks.beerconsumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@EnableAsync
@SpringBootApplication
class BeerConsumerApplication

fun main(args: Array<String>) {
	runApplication<BeerConsumerApplication>(*args)
}
