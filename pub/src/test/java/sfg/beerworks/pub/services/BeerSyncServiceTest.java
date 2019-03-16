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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sfg.beerworks.pub.domain.Beer;
import sfg.beerworks.pub.domain.Distributor;
import sfg.beerworks.pub.repository.BeerRepository;
import sfg.beerworks.pub.repository.DistributorRepository;
import sfg.beerworks.pub.web.clients.DistributorClient;
import sfg.beerworks.pub.web.model.BeerDto;
import sfg.beerworks.pub.web.model.BeerPagedList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class BeerSyncServiceTest {

    @Mock
    BeerRepository beerRepository;

    @Mock
    DistributorRepository distributorRepository;

    @Mock
    DistributorClient distributorClient;

    @InjectMocks
    BeerSyncService syncService;

    Distributor distributor = Distributor.builder().build();

    @Test
    void syncBeersFromBreweries() {
        List<BeerDto> beerDtos = new ArrayList<>();
        beerDtos.add(BeerDto.builder().build());
        beerDtos.add(BeerDto.builder().build());
        BeerPagedList beerPagedList = new BeerPagedList(beerDtos);

        given(distributorRepository.findAll()).willReturn(Flux.fromIterable(Arrays.asList(distributor)));
        given(distributorClient.getBeerList(any())).willReturn(Mono.just(beerPagedList));
        given(beerRepository.findBeerByUpc(any())).willReturn(Mono.just(Beer.builder().build()), Mono.empty());
        given(beerRepository.save(any())).willReturn(Mono.just(Beer.builder().build()));
        syncService.syncBeersFromBreweries();

        then(distributorRepository).should().findAll();
        then(distributorClient).should().getBeerList(any());
        then(beerRepository).should(times(2)).findBeerByUpc(any());
    }
}