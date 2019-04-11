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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import sfg.beerworks.distributor.services.BeerOrderService;
import sfg.beerworks.distributor.web.model.BreweryOrderStatusUpdate;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class BreweryOrderControllerTest {

    @Mock
    BeerOrderService beerOrderService;

    @InjectMocks
    BreweryOrderController controller;

    WebTestClient client;

    @BeforeEach
    void setUp() {
        client = WebTestClient.bindToController(controller).build();
    }

    @Test
    void testOrderStatusUpdate() {
        //given
        BreweryOrderStatusUpdate breweryOrderStatusUpdate = BreweryOrderStatusUpdate.builder()
                .id(UUID.randomUUID().toString())
                .customerRef("1234")
                .build();

        //when/then
        client.post().uri("/api/v1/order/status")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(breweryOrderStatusUpdate), BreweryOrderStatusUpdate.class)
                .exchange()
                .expectStatus().isOk();

        then(beerOrderService).should().updateOrderStatus(any());
    }
}