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

package sfg.beerworks.pub.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import sfg.beerworks.pub.domain.Distributor;
import sfg.beerworks.pub.repository.DistributorRepository;

import java.util.UUID;

@Profile({"default", "localhost"})
@Component
public class LocalHostBootstrap implements CommandLineRunner {
    private final DistributorRepository distributorRepository;

    public LocalHostBootstrap(DistributorRepository distributorRepository) {
        this.distributorRepository = distributorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadDistributors();
    }

    private void loadDistributors() {
        distributorRepository.save(Distributor.builder()
                .id(UUID.randomUUID().toString())
                .distributorName("SFG Beer Distributor")
                .baseUrl("http://localhost:8090")
                .build()).subscribe();
    }
}
