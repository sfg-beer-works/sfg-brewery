package sfg.beerworks.distributor.web.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sfg.beerworks.distributor.services.BeerOrderService;
import sfg.beerworks.distributor.web.model.OrderStatusUpdate;

@RequestMapping("/api/v1/order/")
@RestController
public class OrderController {

    private final BeerOrderService beerOrderService;

    public OrderController(BeerOrderService beerOrderService) {
        this.beerOrderService = beerOrderService;
    }

    @PostMapping("status")
    @ResponseStatus(HttpStatus.OK)
    public void orderStatusWebHook(OrderStatusUpdate orderStatusUpdate){
        beerOrderService.updateOrderStatus(orderStatusUpdate);
    }
}
