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
import sfg.beerworks.distributor.clients.BreweryClient;
import sfg.beerworks.distributor.domain.Beer;
import sfg.beerworks.distributor.domain.Brewery;
import sfg.beerworks.distributor.model.BeerDto;
import sfg.beerworks.distributor.repository.BeerRepository;
import sfg.beerworks.distributor.repository.BreweryRepository;

import javax.transaction.Transactional;
import java.util.Optional;

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
        breweryRepository.findAll().forEach(brewery -> {
            breweryClient.getBeerList(brewery).subscribe(response -> {
                response.forEach(this::updateBeer);
            });
        });
    }

    @Transactional
    public void updateBeer(BeerDto beerDto) {
        Optional<Beer> beerOptional = beerRepository.findBeerByUpc(beerDto.getUpc());

        Beer beer;

        if(beerOptional.isPresent()) {
            beer = beerOptional.get();
        } else {
            //new if not found
            beer = Beer.builder().build();
        }

        beer.setBeerName(beerDto.getBeerName());
        beer.setBeerStyle(beerDto.getBeerStyle());
        beer.setPrice(beerDto.getPrice());
        beer.setQuantityOnHand(beerDto.getQuantityOnHand());

        log.debug("Saving Beer: " + beer.getBeerName());

        beerRepository.save(beer);
    }

    private void accept(Brewery brewery) {
        breweryClient.getBeerList(brewery).subscribe(response -> {
            response.forEach(this::updateBeer);
        });
    }
}
