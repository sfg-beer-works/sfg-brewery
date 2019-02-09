package guru.sfg.brewery.domain;

import lombok.*;

import javax.persistence.Entity;

/**
 * Created by jt on 2019-01-26.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Brewery extends BaseEntity {

    private String breweryName;

}
