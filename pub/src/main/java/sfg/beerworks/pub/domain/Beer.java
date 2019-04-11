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

package sfg.beerworks.pub.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Document
public class Beer extends BaseEntity{

    @Builder
    public Beer(String id, Integer version, LocalDateTime createdDate, LocalDateTime lastModifiedDate, Distributor distributor,
                String beerName, String beerStyle, Long upc, BigDecimal price,
                Integer quantityOnHand, Integer quantityOnOrder) {
        super(id, version, createdDate, lastModifiedDate);
        this.distributor = distributor;
        this.beerName = beerName;
        this.beerStyle = beerStyle;
        this.upc = upc;
        this.price = price;
        this.quantityOnHand = quantityOnHand;
        this.quantityOnOrder = quantityOnOrder;
    }

    private Distributor distributor;
    private String beerName;
    private String beerStyle;
    private Long upc;
    private BigDecimal price;
    private Integer quantityOnHand = 0;
    private Integer quantityOnOrder = 0;
}
