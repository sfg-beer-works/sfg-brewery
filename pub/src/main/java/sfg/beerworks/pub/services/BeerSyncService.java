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
import sfg.beerworks.pub.clients.DistributorClient;
import sfg.beerworks.pub.domain.Beer;
import sfg.beerworks.pub.model.BeerDto;
import sfg.beerworks.pub.repository.BeerRepository;
import sfg.beerworks.pub.repository.DistributorRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
public class BeerSyncService {
    private final BeerRepository beerRepository;
    private final DistributorRepository distributorRepository;
    private final DistributorClient distributorClient;

    public BeerSyncService(BeerRepository beerRepository, DistributorRepository distributorRepository, DistributorClient distributorClient) {
        this.beerRepository = beerRepository;
        this.distributorRepository = distributorRepository;
        this.distributorClient = distributorClient;
    }

    @Scheduled(fixedRate = 10500) //every 10 seconds
    public void syncBeersFromBreweries(){
        distributorRepository.findAll().forEach(distributor -> {
            distributorClient.getBeerList(distributor).subscribe(response -> {
                response.getContent().forEach(this::updateBeer);
            });
        });
    }

    @Transactional
    public void updateBeer(BeerDto beerDto) {
        Optional<Beer> beerOptional = beerRepository.findBeerByUpc(beerDto.getUpc());

        Beer beer = beerOptional.orElseGet(() -> Beer.builder().build());
        beer.setBeerName(beerDto.getBeerName());
        beer.setBeerStyle(beerDto.getBeerStyle());
        beer.setPrice(beerDto.getPrice());
        beer.setQuantityOnHand(beerDto.getQuantityOnHand());
        beer.setUpc(beerDto.getUpc());

        log.debug("Pub Saving Beer: " + beer.getBeerName() + " UPC: " + beer.getUpc());

        beerRepository.save(beer);
    }
}
