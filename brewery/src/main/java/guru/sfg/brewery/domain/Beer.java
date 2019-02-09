package guru.sfg.brewery.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.Set;

/**
 * Created by jt on 2019-01-26.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Beer extends BaseEntity {

    private String beerName;
    private String beerStyle;

    /**
     * Min on hand qty - used to trigger brew
     */
    private Integer minOnHand;
    private Integer quantityToBrew;
    private BigDecimal price;

    @OneToMany(mappedBy = "beer")
    private Set<BeerInventory> beerInventory;
}
