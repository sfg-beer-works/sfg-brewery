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

package guru.sfg.brewery.web.mappers;

import guru.sfg.brewery.domain.BeerOrder;
import guru.sfg.brewery.domain.BeerOrderLine;
import guru.sfg.brewery.web.model.BeerOrderDto;
import guru.sfg.brewery.web.model.BeerOrderLineDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = {BeerOrderMapperTest.Config.class})
class BeerOrderMapperTest {

    @Configuration
    static class Config{

        @Bean
        DateMapper dateMapper(){
            return new DateMapper();
        }

        @Bean
        BeerOrderMapper beerOrderMapper(DateMapper dateMapper){
            BeerOrderMapper beerOrderMapper = new BeerOrderMapperImpl();

            Field field = ReflectionUtils.findField(BeerOrderMapperImpl.class, "dateMapper");
            ReflectionUtils.makeAccessible(field);
            ReflectionUtils.setField(field, beerOrderMapper, dateMapper);

            return beerOrderMapper;
        }
    }

    @Autowired
    BeerOrderMapper orderMapper;

    @Test
    void testToDto() {
        BeerOrderLine line = BeerOrderLine.builder()
                                .id(UUID.randomUUID()).build();

        BeerOrder order = BeerOrder.builder().id(UUID.randomUUID())
                            .beerOrderLines(Set.of(line)).build();

        BeerOrderDto dto = orderMapper.beerOrderToDto(order);

        assertThat(dto.getId()).isEqualTo(order.getId());
        assertThat(dto.getBeerOrderLines()).hasSize(1);
    }

    @Test
    void testFromDto() {
        BeerOrderLineDto line = BeerOrderLineDto.builder()
                .id(UUID.randomUUID()).build();

        BeerOrderDto order = BeerOrderDto.builder().id(UUID.randomUUID())
                .beerOrderLines(Arrays.asList(line)).build();

        BeerOrder beerOrder = orderMapper.dtoToBeerOrder(order);

        assertThat(beerOrder.getId()).isEqualTo(order.getId());
        assertThat(beerOrder.getBeerOrderLines()).hasSize(1);

    }
}