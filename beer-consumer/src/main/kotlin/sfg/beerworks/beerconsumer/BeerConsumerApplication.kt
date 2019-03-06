package sfg.beerworks.beerconsumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BeerConsumerApplication

fun main(args: Array<String>) {
	runApplication<BeerConsumerApplication>(*args)
}
