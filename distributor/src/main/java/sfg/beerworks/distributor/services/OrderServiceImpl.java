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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import sfg.beerworks.distributor.repository.OrderRespository;
import sfg.beerworks.distributor.web.mappers.OrderMapper;
import sfg.beerworks.distributor.web.model.OrderDto;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRespository orderRespository;
    private final OrderMapper orderMapper;
    private final ApplicationEventPublisher publisher;

    public OrderServiceImpl(OrderRespository orderRespository, OrderMapper orderMapper,
                            ApplicationEventPublisher publisher) {
        this.orderRespository = orderRespository;
        this.orderMapper = orderMapper;
        this.publisher = publisher;
    }

    @Override
    public Mono<OrderDto> saveNewOrder(Mono<OrderDto> orderDtoMono) {
        return orderDtoMono
                .map(orderMapper::dtoToOrder)
                .flatMap(orderRespository::save)
                .doOnNext(savedOrder -> {
                    log.debug("Saved Order: " + savedOrder.getId());
                })
                .map(orderMapper::orderToDto);
    }
}
