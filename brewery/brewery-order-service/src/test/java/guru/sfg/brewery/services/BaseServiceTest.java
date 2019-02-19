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

package guru.sfg.brewery.services;

import guru.sfg.brewery.domain.*;
import guru.sfg.brewery.model.BeerStyleEnum;
import guru.sfg.brewery.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class BaseServiceTest {

    @Autowired
    BeerOrderService beerOrderService;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    BeerOrderLineRepository beerOrderLineRepository;

    @Autowired
    BeerInventoryRepository beerInventoryRepository;

    Beer testBeerGalaxy;
    Beer testBeerJava;
    Beer testBeerMangoBob;
    BeerInventory testInventoryGalaxy;
    BeerInventory testInventoryJava;

    Customer testCustomer;
    BeerOrder testOrder1;
    BeerOrder testOrder2;
    BeerOrder testOrder3;

    @BeforeEach
    void setUp() {
        testBeerGalaxy = beerRepository.save(Beer.builder()
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyleEnum.PALE_ALE)
                .minOnHand(10)
                .quantityToBrew(1000)
                .build());

        testBeerJava = beerRepository.save(Beer.builder()
                .beerName("Java Jill")
                .beerStyle(BeerStyleEnum.PORTER)
                .minOnHand(10)
                .quantityToBrew(1000)
                .build());

        testBeerMangoBob = beerRepository.saveAndFlush(Beer.builder()
                .beerName("Mango Bobs")
                .beerStyle(BeerStyleEnum.IPA)
                .minOnHand(10)
                .quantityToBrew(1000)
                .build());

        testInventoryGalaxy = beerInventoryRepository.save(BeerInventory.builder()
                .beer(testBeerGalaxy)
                .quantityOnHand(1000)
                .build());

        testInventoryJava = beerInventoryRepository.save(BeerInventory.builder()
                .beer(testBeerJava)
                .quantityOnHand(10)
                .build());

        testCustomer = customerRepository.save(Customer
                .builder()
                .customerName("Test 1").apiKey(UUID.randomUUID())
                .build());

        Set<BeerOrderLine> orderLines1 = new HashSet<>();
        orderLines1.add(BeerOrderLine.builder().beer(testBeerGalaxy).orderQuantity(15).quantityAllocated(0).build());
        orderLines1.add(BeerOrderLine.builder().beer(testBeerJava).orderQuantity(7).quantityAllocated(0).build());

        testOrder1 = beerOrderRepository.save(BeerOrder.builder()
                .orderStatus(OrderStatusEnum.NEW)
                .customer(testCustomer)
                .customerRef("testOrder1")
                .orderStatusCallbackUrl("http://example.com/post")
                .beerOrderLines(orderLines1)
                .build());

        orderLines1.forEach(line -> {
            line.setBeerOrder(testOrder1);
        });

        beerOrderRepository.save(testOrder1);

        Set<BeerOrderLine> orderLines2 = new HashSet<>();
        orderLines2.add(BeerOrderLine.builder().beer(testBeerGalaxy).orderQuantity(15).quantityAllocated(0).build());
        orderLines2.add(BeerOrderLine.builder().beer(testBeerJava).orderQuantity(7).quantityAllocated(0).build());

        testOrder2 = beerOrderRepository.save(BeerOrder.builder()
                .orderStatus(OrderStatusEnum.NEW)
                .customer(testCustomer)
                .customerRef("testOrder2")
                .orderStatusCallbackUrl("http://example.com/post")
                .beerOrderLines(orderLines2)
                .build());

        orderLines2.forEach(line -> {
            line.setBeerOrder(testOrder2);
        });

        beerOrderRepository.save(testOrder2);

        Set<BeerOrderLine> orderLines3 = new HashSet<>();
        orderLines3.add(BeerOrderLine.builder().beer(testBeerGalaxy).orderQuantity(15).quantityAllocated(0).build());
        orderLines3.add(BeerOrderLine.builder().beer(testBeerJava).orderQuantity(7).quantityAllocated(0).build());

        testOrder3 = beerOrderRepository.save(BeerOrder.builder()
                .orderStatus(OrderStatusEnum.NEW)
                .customer(testCustomer)
                .customerRef("testOrder3")
                .orderStatusCallbackUrl("http://example.com/post")
                .beerOrderLines(orderLines3)
                .build());

        orderLines3.forEach(line -> {
            line.setBeerOrder(testOrder3);
        });
        beerOrderRepository.saveAndFlush(testOrder3);
    }
}
