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
import sfg.beerworks.pub.domain.Beer;
import sfg.beerworks.pub.repository.BeerRepository;

@Component
public class BeerHandlerImpl implements BeerHandler {

    private BeerRepository beerRepository;

    @Override
    public Mono<ServerResponse> listBeers(ServerRequest request) {
        return  ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(beerRepository.findAll(), Beer.class);
    }

    @Override
    public Mono<ServerResponse> getBeerById(ServerRequest request) {
        return null;
    }
}
