package sfg.beerworks.distributor.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import sfg.beerworks.distributor.domain.BreweryOrder;
import sfg.beerworks.distributor.domain.BreweryOrderStatus;
import sfg.beerworks.distributor.repository.BreweryOrderRepository;
import sfg.beerworks.distributor.web.model.BreweryOrderStatusUpdate;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class BeerOrderServiceImplTest {

    @Mock
    BreweryOrderRepository breweryOrderRepository;

    @InjectMocks
    BeerOrderServiceImpl service;

    @Test
    void updateOrderStatusTestEmptyObj() {
        service.updateOrderStatus(BreweryOrderStatusUpdate.builder().build());
    }

    @Test
    void testGoodStatusGoodUUID() {
        given(breweryOrderRepository.findById(anyString())).willReturn(Mono.just(BreweryOrder.builder().build()));

        service.updateOrderStatus(BreweryOrderStatusUpdate.builder()
                .customerRef(UUID.randomUUID().toString())
                .orderStatus(BreweryOrderStatus.READY.name())
                .build());

        then(breweryOrderRepository).should().findById(any(String.class));
        then(breweryOrderRepository).should().save(any());
        then(breweryOrderRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void testOrderNotFound() {
        given(breweryOrderRepository.findById(anyString())).willReturn(Mono.empty());

        service.updateOrderStatus(BreweryOrderStatusUpdate.builder()
                .customerRef(UUID.randomUUID().toString())
                .orderStatus(BreweryOrderStatus.READY.name())
                .build());

        then(breweryOrderRepository).should().findById(anyString());
        then(breweryOrderRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void testBadStatusGoodUUID() {
        given(breweryOrderRepository.findById(anyString())).willReturn(Mono.just(BreweryOrder.builder().build()));

        service.updateOrderStatus(BreweryOrderStatusUpdate.builder()
                .customerRef(UUID.randomUUID().toString())
                .orderStatus("asdf")
                .build());

        then(breweryOrderRepository).should().findById(anyString());
        then(breweryOrderRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void testGoodStatusBadUUID() {
        service.updateOrderStatus(BreweryOrderStatusUpdate.builder()
                .customerRef("asdf")
                .orderStatus(BreweryOrderStatus.READY.name())
                .build());
        then(breweryOrderRepository).shouldHaveNoMoreInteractions();
    }
}