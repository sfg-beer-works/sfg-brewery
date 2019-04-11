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
import sfg.beerworks.distributor.domain.OrderStatus;
import sfg.beerworks.distributor.repository.BeerRepository;
import sfg.beerworks.distributor.repository.OrderRespository;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class OrderAllocationService {
    private final OrderRespository orderRespository;
    private final BeerRepository beerRepository;

    public OrderAllocationService(OrderRespository orderRespository, BeerRepository beerRepository) {
        this.orderRespository = orderRespository;
        this.beerRepository = beerRepository;
    }

    @Scheduled(fixedRate = 1000)
    public void allocateOrders(){

        AtomicInteger allocationCount = new AtomicInteger();

        orderRespository.findAllByStatus(OrderStatus.NEW)
                .subscribe(order -> {
                    if (order != null && order.getOrderLines() != null && order.getOrderLines().size() > 0){
                        allocationCount.incrementAndGet();

                        order.getOrderLines().stream().forEach(orderLine -> {
                            beerRepository.findBeerByUpc(orderLine.getUpc())
                                    .subscribe(beer -> {
                                        if (beer.getQuantityOnHand() >= orderLine.getOrderQuantity()){
                                            beer.setQuantityOnHand(beer.getQuantityOnHand() - orderLine.getOrderQuantity());
                                            beerRepository.save(beer).subscribe();
                                            order.setStatus(OrderStatus.READY);
                                        }
                                    });
                        });

                        orderRespository.save(order).subscribe();
                    }
                });

        log.debug("Orders Allocated: " + allocationCount.get());
    }
}
