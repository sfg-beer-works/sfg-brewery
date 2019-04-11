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

package sfg.beerworks.pub.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import sfg.beerworks.pub.domain.Beer;
import sfg.beerworks.pub.repository.BeerRepository;
import sfg.beerworks.pub.repository.DistributorRepository;
import sfg.beerworks.pub.web.clients.DistributorClient;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Slf4j
@Service
public class BeerSyncService {
    private final BeerRepository beerRepository;
    private final DistributorRepository distributorRepository;
    private final DistributorClient distributorClient;

    public BeerSyncService(BeerRepository beerRepository, DistributorRepository distributorRepository,
                           DistributorClient distributorClient) {
        this.beerRepository = beerRepository;
        this.distributorRepository = distributorRepository;
        this.distributorClient = distributorClient;
    }

    @Scheduled(fixedRate = 10500) //every 10 seconds
    public void syncBeersFromBreweries() {
        distributorRepository.findAll().log("Running syc")
                .subscribe(distributor -> distributorClient.getBeerList(distributor)
                        .subscribe(page -> Flux.fromIterable(page.getContent())
                                .subscribe(beerDto -> beerRepository.findBeerByUpc(beerDto.getUpc())
                                        .defaultIfEmpty(Beer.builder()
                                                .id(UUID.randomUUID().toString())
                                                .quantityOnHand(12) //init qty to 12
                                                .createdDate(LocalDateTime.now(ZoneId.of("Z"))).build())
                                        .map(beer -> {
                                            beer.setBeerName(beerDto.getBeerName());
                                            beer.setBeerStyle(beerDto.getBeerStyle());
                                            beer.setPrice(beerDto.getPrice());
                                            beer.setUpc(beerDto.getUpc());
                                            beer.setLastModifiedDate(LocalDateTime.now(ZoneId.of("Z")));
                                            return beer;
                                        }).map(beerRepository::save)
                                        .subscribe(beerMono -> {
                                            beerMono.subscribe(beer -> {
                                                log.debug("Saved Beer: " + beer.getBeerName() + " - " + beer.getId());
                                            });
                                        })
                        )));
    }
}
