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

package sfg.beerworks.pub.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import sfg.beerworks.pub.web.handlers.BeerHandler;
import sfg.beerworks.pub.web.handlers.CustomerHandler;
import sfg.beerworks.pub.web.handlers.PubOrderHandler;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class PubRouterConfig {

    @Bean
    public RouterFunction beerRouter(BeerHandler beerHandler){
        return route(GET("/api/v1/beer")
                        .and(accept(APPLICATION_JSON)), beerHandler::listBeers)
                .and(route(GET("/api/v1/beer/{beerId}")
                        .and(accept(APPLICATION_JSON)), beerHandler::getBeerById));
    }

    @Bean
    public RouterFunction customerRouter(CustomerHandler customerHandler){
        return route(POST("/api/v1/customers")
                        .and(accept(APPLICATION_JSON)), customerHandler::saveNewCustomer)
                .and(route(GET("/api/v1/customers/{customerId}")
                        .and(accept(APPLICATION_JSON)), customerHandler::getCustomer));
    }

    @Bean
    public RouterFunction orderRouter(PubOrderHandler pubOrderHandler) {
        return route(POST("/api/v1/customers/{customerId}/orders")
                .and(accept(APPLICATION_JSON)), pubOrderHandler::saveNewPubOrder);
    }
}
