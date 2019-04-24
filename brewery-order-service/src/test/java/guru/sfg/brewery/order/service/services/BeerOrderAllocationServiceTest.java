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

package guru.sfg.brewery.order.service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

@DataJpaTest
@ComponentScan(basePackages = {"guru.sfg.brewery.order.service.services", "guru.sfg.brewery.order.service.web.mappers"})
class BeerOrderAllocationServiceTest extends BaseServiceTest {

    @Autowired
    BeerOrderAllocationService beerOrderAllocationService;

//    @Test
//    void runBeerOrderAllocation() throws InterruptedException {
//        //run allocation
//        beerOrderAllocationService.runBeerOrderAllocation();
//
//        Iterable<BeerOrder> orders = beerOrderRepository.findAll();
//
//        orders.forEach(order -> {
//            System.out.println(order.getCustomerRef() + " : " + order.getOrderStatus());
//
//            order.getBeerOrderLines().forEach(line -> {
//                System.out.println("Line: Beer: " + line.getBeer().getBeerName() + " qty ordered: "
//                    + line.getOrderQuantity() + " qty aloc: " + line.getQuantityAllocated());
//            });
//        });
//    }
}