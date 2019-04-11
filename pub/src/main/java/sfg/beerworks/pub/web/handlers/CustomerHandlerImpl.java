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

package sfg.beerworks.pub.web.handlers;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import sfg.beerworks.pub.domain.Customer;
import sfg.beerworks.pub.repository.CustomerRepository;
import sfg.beerworks.pub.web.mappers.CustomerMapper;
import sfg.beerworks.pub.web.model.CustomerDto;


@Component
public class CustomerHandlerImpl implements CustomerHandler {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerHandlerImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public Mono<ServerResponse> saveNewCustomer(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(customerRepository.saveAll(
                        request.bodyToMono(CustomerDto.class)
                                .map(dto -> {
                                    Customer customer = customerMapper.customerDtoToCustomer(dto);
                                    customer.setVersion(null);
                                    return customer;
                                }))
                        .map(customerMapper::customerToCustomerDto), CustomerDto.class);
    }

    @Override
    public Mono<ServerResponse> getCustomer(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(customerRepository.findById(request.pathVariable("customerId"))
                .map(customerMapper::customerToCustomerDto), CustomerDto.class);
    }
}
