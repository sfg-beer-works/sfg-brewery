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

package sfg.beerworks.pub.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import sfg.beerworks.pub.domain.Beer;
import sfg.beerworks.pub.domain.PurchaseOrder;
import sfg.beerworks.pub.domain.PurchaseOrderLine;
import sfg.beerworks.pub.domain.PurchaseOrderStatus;
import sfg.beerworks.pub.repository.BeerRepository;
import sfg.beerworks.pub.repository.PurchaseOrderRepository;
import sfg.beerworks.pub.web.clients.DistributorClient;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
public class BeerOrderedEventListener {

    public static final int DEFULAT_ORDER_QTY = 240;
    private final BeerRepository beerRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final DistributorClient distributorClient;

    public BeerOrderedEventListener(BeerRepository beerRepository, PurchaseOrderRepository purchaseOrderRepository,
                                    DistributorClient distributorClient) {
        this.beerRepository = beerRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.distributorClient = distributorClient;
    }

    @Async
    @EventListener
    public void listen(BeerOrderedEvent event){

        log.debug("Beer Ordered");
        Beer beerOrdered = (Beer) event.getSource();

        beerRepository.findBeerByUpc(beerOrdered.getUpc()).subscribe(beer -> {
            log.debug("Beer Name: " + beer.getBeerName() + " : Quantity Onhand: " + beer.getQuantityOnHand());

            if(beer.getQuantityOnHand() < 10 && beer.getQuantityOnOrder() < 10) { // create po
                Set<PurchaseOrderLine> orderLines = new HashSet<>(1);

                orderLines.add(PurchaseOrderLine.builder()
                        .id(UUID.randomUUID().toString())
                        .createdDate(LocalDateTime.now(ZoneId.of("UTC")))
                        .lastModifiedDate(LocalDateTime.now(ZoneId.of("UTC")))
                        .upc(beer.getUpc())
                        .quantityOrdered(DEFULAT_ORDER_QTY)
                        .build());

                PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                        .id(UUID.randomUUID().toString())
                        .createdDate(LocalDateTime.now(ZoneId.of("UTC")))
                        .lastModifiedDate(LocalDateTime.now(ZoneId.of("UTC")))
                        .purchaseOrderLines(orderLines)
                        .build();

                purchaseOrderRepository.save(purchaseOrder)
                        .subscribe(savedOrder -> {
                            beer.setQuantityOnOrder(beer.getQuantityOnHand() + DEFULAT_ORDER_QTY);
                            beerRepository.save(beer).subscribe();

                            distributorClient.placeOrder(purchaseOrder).subscribe(placedOrder -> {
                                placedOrder.setPurchaseOrderStatus(PurchaseOrderStatus.PLACED);
                                purchaseOrderRepository.save(placedOrder).subscribe();
                            });
                        });
            }
        });

    }
}
