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

package sfg.beerworks.pub.web.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import sfg.beerworks.pub.domain.Beer;
import sfg.beerworks.pub.domain.PubOrder;
import sfg.beerworks.pub.domain.PubOrderStatus;
import sfg.beerworks.pub.events.BeerOrderedEvent;
import sfg.beerworks.pub.repository.BeerRepository;
import sfg.beerworks.pub.repository.PubOrderRepository;
import sfg.beerworks.pub.web.mappers.PubOrderMapper;
import sfg.beerworks.pub.web.model.PubOrderDto;

@Slf4j
@Component
public class PubOrderHandlerImpl implements PubOrderHandler {

    private final PubOrderMapper pubOrderMapper;
    private final PubOrderRepository pubOrderRepository;
    private final BeerRepository beerRepository;
    private final ApplicationEventPublisher publisher;

    public PubOrderHandlerImpl(PubOrderMapper pubOrderMapper, PubOrderRepository pubOrderRepository,
                               BeerRepository beerRepository, ApplicationEventPublisher publisher) {
        this.pubOrderMapper = pubOrderMapper;
        this.pubOrderRepository = pubOrderRepository;
        this.beerRepository = beerRepository;
        this.publisher = publisher;
    }

    @Override
    public Mono<ServerResponse> saveNewPubOrder(ServerRequest request) {
        String customerId = request.pathVariable("customerId");

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(pubOrderRepository.saveAll(
                        request.bodyToMono(PubOrderDto.class)
                                .map(dto -> {
                                    PubOrder pubOrder= pubOrderMapper.dtoToPubOrder(dto);
                                    pubOrder.setCustomerId(customerId);
                                    pubOrder.setPubOrderStatus(PubOrderStatus.DELIVERED_TO_CUSTOMER);

                                    //update beer quantity, quantity delivered
                                    if (pubOrder.getPubOrderLines() != null) {
                                        pubOrder.getPubOrderLines().forEach(orderLine -> {
                                            Mono<Beer> beerMono = beerRepository.findBeerByUpc(orderLine.getUpc());

                                            beerMono.subscribe(beer -> {
                                                if(beer.getQuantityOnHand() >= orderLine.getOrderQuantity() ){
                                                    beer.setQuantityOnHand(beer.getQuantityOnHand()
                                                            - orderLine.getOrderQuantity());
                                                    beerRepository.save(beer).subscribe();

                                                    orderLine.setQuantityDelivered(orderLine.getOrderQuantity());

                                                    publisher.publishEvent(new BeerOrderedEvent(beer, orderLine.getQuantityDelivered()));
                                                }
                                            });
                                        });
                                    } else {
                                        log.warn("No order lines");
                                    }

                                    return pubOrder;
                                }))
                        .map(pubOrderMapper::pubOrderToDto), PubOrderDto.class);
    }
}
