package guru.sfg.brewery.repositories;

import guru.sfg.brewery.domain.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by jt on 2019-01-26.
 */
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {
}
