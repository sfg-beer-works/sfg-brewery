/*
 *  Copyright 2019 the original author or authors.
 *
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package sfg.beerworks.beerconsumer.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import sfg.beerworks.beerconsumer.web.client.PubClient
import sfg.beerworks.beerconsumer.web.model.CustomerDto
import sfg.beerworks.beerconsumer.web.model.PubOrderDto
import sfg.beerworks.beerconsumer.web.model.PubOrderLineDto
import java.time.Duration
import java.time.OffsetDateTime
import java.util.*

@Service
class BeerConsumerService(val pubClient: PubClient) {

    var customer: CustomerDto? = null

    @Scheduled(fixedRate = 5000)
    fun drinkBeer() {

        if(customer == null){
            val newCustomerDTO: CustomerDto = CustomerDto(UUID.randomUUID().toString(), 1, OffsetDateTime.now(),
                    OffsetDateTime.now(), "Beer Drinker")

            pubClient.createCustomer(newCustomerDTO)
                    .doOnError {error ->
                        println(error.localizedMessage)
                    }
                    .subscribe {returnedCustomer ->
                        this.customer = returnedCustomer
                    }
        }

        pubClient.getBeers()
                .take(Duration.ofSeconds(5))
                .collectList().doOnError {error ->
                    println(error.localizedMessage)
                }
                .subscribe { beerList ->
                    if (beerList.size > 0){
                        val beer = beerList.shuffled().last()

                        println("Ordering Beer: " + beer.beerName)

                        var pubOrder = PubOrderDto(UUID.randomUUID().toString(), "http://exmaple.com")
                        pubOrder.pubOrderLines?.add(PubOrderLineDto(beer.upc, 1))

                        val mapper = jacksonObjectMapper()
                        println(mapper.writeValueAsString(pubOrder))

                        pubClient.orderBeer(customer?.id.toString(), pubOrder).subscribe()
                    }
                }
    }
}