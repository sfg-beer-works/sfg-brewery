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

package sfg.beerworks.distributor.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import sfg.beerworks.distributor.services.BeerService;
import sfg.beerworks.distributor.web.mappers.BeerMapper;
import sfg.beerworks.distributor.web.model.BeerPagedList;

@Slf4j
@RestController("/api/v1/beer")
public class BeerController {
    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;

    private final BeerService beerService;
    private final BeerMapper beerMapper;

    public BeerController(BeerService beerService, BeerMapper beerMapper) {
        this.beerService = beerService;
        this.beerMapper = beerMapper;
    }

    @GetMapping
    public Mono<BeerPagedList> listBeers(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                  @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                  @RequestParam(value = "beerName", required = false) String beerName,
                                  @RequestParam(value = "beerStyle", required = false) String beerStyle){

        if (pageNumber == null || pageNumber < 0){
            pageNumber = DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        return beerService.listBeers(beerName, beerStyle, PageRequest.of(pageNumber, pageSize));
    }
}
