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
import org.springframework.stereotype.Service;
import sfg.beerworks.distributor.domain.BreweryOrder;
import sfg.beerworks.distributor.domain.BreweryOrderStatus;
import sfg.beerworks.distributor.repository.BreweryOrderRepository;
import sfg.beerworks.distributor.web.model.OrderStatusUpdate;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class BeerOrderServiceImpl implements BeerOrderService {

    private final BreweryOrderRepository breweryOrderRepository;

    public BeerOrderServiceImpl(BreweryOrderRepository breweryOrderRepository) {
        this.breweryOrderRepository = breweryOrderRepository;
    }

    @Override
    public void updateOrderStatus(OrderStatusUpdate orderStatusUpdate) {

        try {
            if (orderStatusUpdate.getCustomerRef() != null) {
                UUID breweryOrderId = UUID.fromString(orderStatusUpdate.getCustomerRef());
                updateOrder(breweryOrderId, orderStatusUpdate);
            } else {
                log.debug("Customer Ref is null");
            }
        } catch (Exception e) {
            log.error("Error processing update", e);
        }
    }

    private void updateOrder(UUID breweryOrderId, OrderStatusUpdate orderStatusUpdate) {
        Optional<BreweryOrder> breweryOrderOptional = breweryOrderRepository.findById(breweryOrderId);

        if(breweryOrderOptional.isPresent()){
           BreweryOrder breweryOrder = breweryOrderOptional.get();

           try {
               BreweryOrderStatus breweryOrderStatus = BreweryOrderStatus.valueOf(orderStatusUpdate.getOrderStatus());

               breweryOrder.setBreweryOrderStatus(breweryOrderStatus);

               breweryOrderRepository.save(breweryOrder);
           } catch (Exception e){
                log.error("Error parsing enum", e);
           }
        } else {
            log.error("Order Not found");
        }
    }
}
