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

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import sfg.beerworks.beerconsumer.web.model.BeerDto
import sfg.beerworks.beerconsumer.web.model.CustomerDto
import sfg.beerworks.beerconsumer.web.model.PubOrderDto

interface PubClient {
    fun getBeers(): Flux<BeerDto>

    fun createCustomer(customerDto : CustomerDto) : Mono<CustomerDto>

    fun orderBeer(customerId : String, pubOrderDto: PubOrderDto) : Mono<PubOrderDto>
}