package sfg.beerworks.distributor.services;

import org.junit.jupiter.api.Test;
import sfg.beerworks.distributor.model.OrderStatusUpdate;




class BeerOrderServiceImplTest {

    @Test
    void updateOrderStatus() {
        BeerOrderServiceImpl service = new BeerOrderServiceImpl();

        service.updateOrderStatus(OrderStatusUpdate.builder().build());

    }
}