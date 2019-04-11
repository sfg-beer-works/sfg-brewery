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

package sfg.beerworks.beerconsumer.web.client

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import sfg.beerworks.beerconsumer.web.model.BeerDto
import sfg.beerworks.beerconsumer.web.model.CustomerDto
import sfg.beerworks.beerconsumer.web.model.PubOrderDto

@ConfigurationProperties("sfg.beerworks")
@Component
class PubClientImpl(val webclientBuilder: WebClient.Builder) : PubClient {

    companion object {
        val V1_LIST_BEER_URL = "/api/v1/beer"
        val V1_CUSTOMER_URL = "/api/v1/customers"
        val V1_CUSTOMER_ORDER_URL = "/api/v1/customers/{customerId}/orders"
    }

    lateinit var puburl: String

    override fun getBeers(): Flux<BeerDto> {
       return  webclientBuilder.baseUrl(puburl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8.toString())
                .build()
                .get()
                .uri(V1_LIST_BEER_URL)
                .retrieve()
                .bodyToFlux(BeerDto::class.java)
    }

    override fun createCustomer(customerDto: CustomerDto): Mono<CustomerDto> {
        return webclientBuilder.baseUrl(puburl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8.toString())
                .build()
                .post()
                .uri(V1_CUSTOMER_URL)
                .body(Mono.just(customerDto), CustomerDto::class.java)
                .retrieve()
                .bodyToMono(CustomerDto::class.java)
    }

    override fun orderBeer(customerId : String, pubOrderDto: PubOrderDto): Mono<PubOrderDto> {
        return webclientBuilder.baseUrl(puburl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8.toString())
                .build()
                .post()
                .uri(V1_CUSTOMER_ORDER_URL, customerId)
                .body(Mono.just(pubOrderDto), PubOrderDto::class.java)
                .retrieve()
                .bodyToMono(PubOrderDto::class.java)
    }
}