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

package guru.sfg.brewery.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.sfg.brewery.model.BeerOrderDto;
import guru.sfg.brewery.model.BeerOrderLineDto;
import guru.sfg.brewery.model.BeerOrderPagedList;
import guru.sfg.brewery.model.OrderStatusEnum;
import guru.sfg.brewery.services.BeerOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BeerOrderControllerTest {

    //test properties
    static final String API_ROOT = "/api/v1/customers/";
    static final UUID customerId = UUID.randomUUID();
    static final UUID orderId = UUID.randomUUID();
    static final UUID beerId = UUID.randomUUID();
    static final String callbackUrl = "http://example.com";

    @Mock
    BeerOrderService beerOrderService;

    @InjectMocks
    BeerOrderController controller;

    MockMvc mockMvc;

    @Captor
    ArgumentCaptor<BeerOrderDto> beerOrderDtoArgumentCaptorCaptor;

    @Captor
    ArgumentCaptor<UUID> customerUUIDCaptor;

    @Captor
    ArgumentCaptor<UUID> orderUUIDCaptor;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void listOrders() throws Exception {
        //given
        List<BeerOrderDto> orderDtos = new ArrayList<>();
        orderDtos.add(BeerOrderDto.builder().build());
        orderDtos.add(BeerOrderDto.builder().build());
        given(beerOrderService.listOrders(any(), any(Pageable.class)))
                .willReturn(new BeerOrderPagedList(orderDtos, PageRequest.of(1, 1), 2L));

        mockMvc.perform(get(API_ROOT + customerId.toString()+ "/orders").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.content", hasSize(2)));

        then(beerOrderService).should().listOrders(any(), any(Pageable.class));
    }

    @Test
    void placeOrder() throws Exception {
        //given
        //place order
        BeerOrderDto orderDto = buildOrderDto();

        //response order
        BeerOrderDto orderResponseDto = getBeerOrderDtoResponse();

        //build json string
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(orderDto);
        System.out.println("Order Request: " + jsonString);

        given(beerOrderService.placeOrder(customerUUIDCaptor.capture(),
                beerOrderDtoArgumentCaptorCaptor.capture())).willReturn(orderResponseDto);

        mockMvc.perform(post(API_ROOT + customerId.toString() + "/orders")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.customerId", is(customerId.toString())))
                .andExpect(jsonPath("$.beerOrderLines", hasSize(1)))
                .andExpect(jsonPath("$.beerOrderLines[0].beerId", is(beerId.toString())));

        then(beerOrderService).should().placeOrder(any(UUID.class), any(BeerOrderDto.class));

        assertThat(customerUUIDCaptor.getValue()).isEqualTo(customerId);
        assertThat(beerOrderDtoArgumentCaptorCaptor.getValue().getBeerOrderLines().get(0).getBeerId()).isEqualTo(beerId);
    }

    private BeerOrderDto getBeerOrderDtoResponse() {
        BeerOrderDto orderResponseDto = buildOrderDto();
        orderResponseDto.setCustomerId(customerId);
        orderResponseDto.setId(orderId);
        orderResponseDto.setOrderStatus(OrderStatusEnum.NEW);
        return orderResponseDto;
    }

    private BeerOrderDto buildOrderDto() {
        List<BeerOrderLineDto> orderLines = Arrays.asList(BeerOrderLineDto.builder()
                .beerId(beerId)
                .orderQuantity(5)
                .build());

        return BeerOrderDto.builder()
                .orderStatusCallbackUrl(callbackUrl)
                .beerOrderLines(orderLines)
                .build();
    }

    @Test
    void getOrder() throws Exception {
        given(beerOrderService.getOrderById(customerUUIDCaptor.capture(),
                orderUUIDCaptor.capture())).willReturn(getBeerOrderDtoResponse());

        mockMvc.perform(get(API_ROOT + customerId + "/orders/" + orderId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.customerId", is(customerId.toString())))
                .andExpect(jsonPath("$.beerOrderLines", hasSize(1)))
                .andExpect(jsonPath("$.beerOrderLines[0].beerId", is(beerId.toString())));
    }

    @Test
    void pickupOrder() throws Exception {
        mockMvc.perform(put(API_ROOT + customerId + "/orders/" + orderId + "/pickup"))
                .andExpect(status().isNoContent());
    }
}