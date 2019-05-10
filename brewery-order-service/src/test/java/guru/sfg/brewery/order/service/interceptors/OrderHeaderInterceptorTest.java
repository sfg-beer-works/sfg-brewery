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

package guru.sfg.brewery.order.service.interceptors;

import guru.sfg.brewery.order.service.domain.BeerOrder;
import guru.sfg.brewery.order.service.domain.OrderStatusEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class OrderHeaderInterceptorTest {

    @Mock
    ApplicationEventPublisher publisher;

    @InjectMocks
    OrderHeaderInterceptor orderHeaderInterceptor;

    @Test
    void onFlushDirty() {
        //given
        BeerOrder beerOrder = BeerOrder.builder().build();
        Object[] currentState = {OrderStatusEnum.NEW};
        Object[] prevState = {OrderStatusEnum.READY};

        //when
        orderHeaderInterceptor.onFlushDirty(beerOrder, null, currentState, prevState, null, null);

        //then
        then(publisher).should().publishEvent(any());
    }
}