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
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class PubOrder extends BaseEntity{

    @Builder
    public PubOrder(UUID id, Long version, Timestamp createdDate, Timestamp lastModifiedDate,
                    String breweryOrderId, Pub pub, Set<PubOrderLine> pubOrderLines,
                    PubOrderStatus pubOrderStatus, String orderStatusCallbackUrl) {
        super(id, version, createdDate, lastModifiedDate);
        this.breweryOrderId = breweryOrderId;
        this.pub = pub;
        this.pubOrderLines = pubOrderLines;
        this.pubOrderStatus = pubOrderStatus;
        this.orderStatusCallbackUrl = orderStatusCallbackUrl;
    }

    private String breweryOrderId;

    @ManyToOne
    private Pub pub;

    @OneToMany(mappedBy = "pubOrder", cascade = CascadeType.PERSIST)
    @Fetch(FetchMode.JOIN)
    private Set<PubOrderLine> pubOrderLines;

    private PubOrderStatus pubOrderStatus = PubOrderStatus.NEW;
    private String orderStatusCallbackUrl;
}
