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

package sfg.beerworks.distributor.web.clients;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import sfg.beerworks.distributor.domain.Brewery;
import sfg.beerworks.distributor.web.model.BeerPagedList;

@Slf4j
@Component
public class BreweryClientImpl implements BreweryClient {

    public static final String LIST_BEER_URL = "/api/v1/beer";

    @Override
    public Mono<BeerPagedList> getBeerList(Brewery brewery) {
        return WebClient.create(brewery.getBaseUrl())
                .get()
                .uri(LIST_BEER_URL)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .retrieve()
                .bodyToMono(BeerPagedList.class)
                .doOnError(error -> {
                    log.error("Error Calling endpoing", error);
                });
    }
}
