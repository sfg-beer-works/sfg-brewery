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
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class BreweryOrder {

    @Builder
    public BreweryOrder(String breweryOrderId, Pub pub, Set<BreweryOrderLine> beerOrderLines, BreweryOrderStatus breweryOrderStatus, String orderStatusCallbackUrl) {
        this.breweryOrderId = breweryOrderId;
        this.pub = pub;
        this.beerOrderLines = beerOrderLines;
        this.breweryOrderStatus = breweryOrderStatus;
        this.orderStatusCallbackUrl = orderStatusCallbackUrl;
    }

    private String breweryOrderId;

    @ManyToOne
    private Pub pub;

    @OneToMany(mappedBy = "beerOrder", cascade = CascadeType.PERSIST)
    @Fetch(FetchMode.JOIN)
    private Set<BreweryOrderLine> beerOrderLines;

    private BreweryOrderStatus breweryOrderStatus = BreweryOrderStatus.NEW;
    private String orderStatusCallbackUrl;
}
