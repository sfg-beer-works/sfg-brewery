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
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Document
public class Order extends BaseEntity {

    @Builder
    public Order(String id, Long version, LocalDateTime createdDate, LocalDateTime lastModifiedDate, String customerId,
                 OrderStatus status, String customerRef, String orderStatusCallbackUrl, Set<OrderLine> orderLines) {
        super(id, version, createdDate, lastModifiedDate);
        this.status = status;
        this.customerId = customerId;
        this.customerRef = customerRef;
        this.orderStatusCallbackUrl = orderStatusCallbackUrl;
        this.orderLines = orderLines;
    }

    private OrderStatus status = OrderStatus.NEW;
    private String customerId;
    private String customerRef;
    private String orderStatusCallbackUrl;
    private Set<OrderLine> orderLines;
}
