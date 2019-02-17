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

import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.domain.BeerOrder;
import guru.sfg.brewery.domain.BeerOrderLine;
import guru.sfg.brewery.domain.Customer;
import guru.sfg.brewery.model.BeerOrderDto;
import guru.sfg.brewery.model.BeerOrderLineDto;
import guru.sfg.brewery.model.BeerOrderPagedList;
import guru.sfg.brewery.model.BeerStyleEnum;
import guru.sfg.brewery.repositories.BeerOrderRepository;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ComponentScan(basePackages = {"guru.sfg.brewery.services", "guru.sfg.brewery.mappers"})
class BeerOrderServiceImplTest {

    @Autowired
    BeerOrderService beerOrderService;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerOrderRepository beerOrderRepository;

    Beer testBeerGalaxy;
    Beer testBeerJava;
    Customer testCustomer;
    BeerOrder testOrder1;
    BeerOrder testOrder2;

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
                .beerStyle(BeerStyleEnum.PALE_ALE)
                .minOnHand(10)
                .quantityToBrew(1000)
                .build());

        testCustomer = customerRepository.save(Customer
                .builder()
                .customerName("Test 1").apiKey(UUID.randomUUID())
                .build());

        Set<BeerOrderLine> orderLines1 = new HashSet<>();
        orderLines1.add(BeerOrderLine.builder().beer(testBeerGalaxy).quantity(5).build());
        orderLines1.add(BeerOrderLine.builder().beer(testBeerJava).quantity(15).build());

        testOrder1 = beerOrderRepository.save(BeerOrder.builder()
            .customer(testCustomer)
                .customerRef("asdf")
                .orderStatusCallbackUrl("http://example.com/post")
                .beerOrderLines(orderLines1)
                .build());

        Set<BeerOrderLine> orderLines2 = new HashSet<>();
        orderLines1.add(BeerOrderLine.builder().beer(testBeerGalaxy).quantity(5).build());
        orderLines1.add(BeerOrderLine.builder().beer(testBeerJava).quantity(15).build());

        testOrder2 = beerOrderRepository.save(BeerOrder.builder()
                .customer(testCustomer)
                .customerRef("asdf443")
                .orderStatusCallbackUrl("http://example.com/post")
                .beerOrderLines(orderLines2)
                .build());
    }

    @Test
    void listOrders() {
        System.out.println(testBeerGalaxy.getId());

        //make sure we have two orders
        assertThat(beerOrderRepository.count()).isEqualTo(2L);

        BeerOrderPagedList pagedList = beerOrderService.listOrders(testCustomer.getId(), PageRequest.of(0, 25));

        assertThat(pagedList.getTotalElements()).isEqualTo(2L);
        assertThat(pagedList.getContent().size()).isEqualTo(2);
    }

    @Test
    void placeOrder() {
        BeerOrderDto dto = BeerOrderDto.builder()
                .orderStatusCallbackUrl("http://foo.com")
                .beerOrderLines(Arrays.asList(BeerOrderLineDto
                        .builder().beerId(testBeerGalaxy.getId()).orderQuantity(12).build()))
                .build();

        BeerOrderDto placedOrder = beerOrderService.placeOrder(testCustomer.getId(), dto);

        assertThat(placedOrder.getId()).isNotNull();
        assertThat(placedOrder.getOrderStatus().name()).isEqualToIgnoringCase("NEW");
    }

    @Test
    void getOrderById() {
        BeerOrderDto dto = beerOrderService.getOrderById(testCustomer.getId(), testOrder1.getId());

        assertThat(dto.getId()).isEqualTo(testOrder1.getId());
    }

    @Test
    void pickupOrder() {
        beerOrderService.pickupOrder(testCustomer.getId(), testOrder1.getId());

        BeerOrderDto dto = beerOrderService.getOrderById(testCustomer.getId(), testOrder1.getId());

        assertThat(dto.getId()).isEqualTo(testOrder1.getId());
        assertThat(dto.getOrderStatus().name()).isEqualTo("PICKED_UP");
    }
}