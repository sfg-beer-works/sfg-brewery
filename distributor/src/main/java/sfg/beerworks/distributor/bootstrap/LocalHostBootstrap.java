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

package sfg.beerworks.distributor.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import sfg.beerworks.distributor.domain.Brewery;
import sfg.beerworks.distributor.repository.BreweryRepository;

@Profile({"default", "localhost"})
@Component
public class LocalHostBootstrap implements CommandLineRunner {

    private final BreweryRepository breweryRepository;

    public LocalHostBootstrap(BreweryRepository breweryRepository) {
        this.breweryRepository = breweryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadBreweries();
    }

    private void loadBreweries() {
        breweryRepository.save(Brewery.builder()
                .baseUrl("http://localhost:8080")
                .breweryName("Cage")
                .build()).subscribe();
    }
}
