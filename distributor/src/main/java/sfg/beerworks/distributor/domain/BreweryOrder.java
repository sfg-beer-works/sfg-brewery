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

package sfg.beerworks.distributor.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Document
public class BreweryOrder extends BaseEntity{

    @Builder
    public BreweryOrder(String id, Long version, LocalDateTime createdDate, LocalDateTime lastModifiedDate,
                        String breweryOrderId, Pub pub, Set<BreweryOrderLine> beerOrderLines,
                        BreweryOrderStatus breweryOrderStatus, String orderStatusCallbackUrl) {
        super(id, version, createdDate, lastModifiedDate);
        this.breweryOrderId = breweryOrderId;
        this.pub = pub;
        this.beerOrderLines = beerOrderLines;
        this.breweryOrderStatus = breweryOrderStatus;
        this.orderStatusCallbackUrl = orderStatusCallbackUrl;
    }

    private String breweryOrderId;

    @DBRef
    private Pub pub;
    private Set<BreweryOrderLine> beerOrderLines;
    private BreweryOrderStatus breweryOrderStatus = BreweryOrderStatus.NEW;
    private String orderStatusCallbackUrl;
}
