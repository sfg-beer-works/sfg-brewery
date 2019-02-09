package guru.sfg.brewery.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by jt on 2019-01-26.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class BeerOrderLine extends BaseEntity{

    @ManyToOne
    private BeerOrder beerOrder;

    @ManyToOne
    private Beer beer;

    private Integer orderQuantity;
}
