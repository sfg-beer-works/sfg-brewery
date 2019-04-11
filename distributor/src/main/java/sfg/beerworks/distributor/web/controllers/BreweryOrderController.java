package sfg.beerworks.distributor.web.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sfg.beerworks.distributor.services.BeerOrderService;
import sfg.beerworks.distributor.web.model.BreweryOrderStatusUpdate;

@RequestMapping("/api/v1/beweryorder/")
@RestController
public class BreweryOrderController {

    private final BeerOrderService beerOrderService;

    public BreweryOrderController(BeerOrderService beerOrderService) {
        this.beerOrderService = beerOrderService;
    }

    @PostMapping("status")
    @ResponseStatus(HttpStatus.OK)
    public void orderStatusWebHook(BreweryOrderStatusUpdate breweryOrderStatusUpdate){
        beerOrderService.updateOrderStatus(breweryOrderStatusUpdate);
    }
}
