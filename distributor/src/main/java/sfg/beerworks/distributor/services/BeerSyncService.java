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

package sfg.beerworks.distributor.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import sfg.beerworks.distributor.domain.Beer;
import sfg.beerworks.distributor.repository.BeerRepository;
import sfg.beerworks.distributor.repository.BreweryRepository;
import sfg.beerworks.distributor.web.clients.BreweryClient;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Slf4j
@Service
public class BeerSyncService {

    private final BeerRepository beerRepository;
    private final BreweryRepository breweryRepository;
    private final BreweryClient breweryClient;

    public BeerSyncService(BeerRepository beerRepository, BreweryRepository breweryRepository,
                           BreweryClient breweryClient) {
        this.beerRepository = beerRepository;
        this.breweryRepository = breweryRepository;
        this.breweryClient = breweryClient;
    }

    @Scheduled(fixedRate = 10000) //every 10 seconds
    public void syncBeersFromBreweries(){
        breweryRepository.findAll().log("Finding all breweries")
                .subscribe(brewery -> {
                    breweryClient.getBeerList(brewery)
                            .subscribe(page -> Flux.fromIterable(page.getContent())
                                .subscribe(beerDto -> beerRepository.findBeerByUpc(beerDto.getUpc())
                                .defaultIfEmpty(Beer.builder()
                                        .id(UUID.randomUUID().toString())
                                        .createdDate(LocalDateTime.now(ZoneId.of("Z"))).build())
                                .map(beer -> {
                                    beer.setBeerName(beerDto.getBeerName());
                                    beer.setBeerStyle(beerDto.getBeerStyle());
                                    beer.setPrice(beerDto.getPrice());
                                    beer.setQuantityOnHand(beerDto.getQuantityOnHand());
                                    beer.setUpc(beerDto.getUpc());
                                    beer.setLastModifiedDate(LocalDateTime.now(ZoneId.of("Z")));
                                    return beer;
                                }).map(beerRepository::save)
                                .subscribe(savedBeer -> {
                                    savedBeer.subscribe(foo -> {
                                        System.out.println(foo.getBeerName());
                                    });
                                    log.debug("Saved: ");// + beer.getBeerName());
                                    beerRepository.count().subscribe(count -> log.debug("Repository count: " + count));
                                })));
                });
    }
}
