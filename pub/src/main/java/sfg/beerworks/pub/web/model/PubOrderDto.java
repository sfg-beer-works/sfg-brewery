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

package sfg.beerworks.pub.web.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sfg.beerworks.pub.domain.PubOrderLine;

import java.time.OffsetDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class PubOrderDto extends BaseItem {

    @Builder
    public PubOrderDto(String id, Integer version, OffsetDateTime createdDate, OffsetDateTime lastModifiedDate,
                       String customerRef, Set<PubOrderLine> pubOrderLines, String orderStatusCallbackUrl) {
        super(id, version, createdDate, lastModifiedDate);
        this.customerRef = customerRef;
        this.pubOrderLines = pubOrderLines;
        this.orderStatusCallbackUrl = orderStatusCallbackUrl;
    }

    private Set<PubOrderLine> pubOrderLines;
    private String customerRef;
    private String orderStatusCallbackUrl;
}
