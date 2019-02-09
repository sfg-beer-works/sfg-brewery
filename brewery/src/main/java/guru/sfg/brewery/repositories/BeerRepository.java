package guru.sfg.brewery.repositories;

import guru.sfg.brewery.domain.Beer;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by jt on 2019-01-26.
 */
public interface BeerRepository extends PagingAndSortingRepository<Beer, Long> {
}
