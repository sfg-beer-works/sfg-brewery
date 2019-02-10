package guru.sfg.brewery.bootstrap;

import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.domain.BeerInventory;
import guru.sfg.brewery.domain.Brewery;
import guru.sfg.brewery.repositories.BeerInventoryRepository;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.repositories.BreweryRepository;
import org.springframework.boot.CommandLineRunner;

/**
 * Created by jt on 2019-01-26.
 */
public class DefaultBreweryLoader implements CommandLineRunner {

    private final BreweryRepository breweryRepository;
    private final BeerRepository beerRepository;
    private final BeerInventoryRepository beerInventoryRepository;

    public DefaultBreweryLoader(BreweryRepository breweryRepository,
                                BeerRepository beerRepository, BeerInventoryRepository beerInventoryRepository) {
        this.breweryRepository = breweryRepository;
        this.beerRepository = beerRepository;
        this.beerInventoryRepository = beerInventoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadBreweryData();
    }

    private void loadBreweryData() {
        if (breweryRepository.count() == 0){
            breweryRepository.save(Brewery
                    .builder()
                    .breweryName("Cage Brewing")
                    .build());

            Beer mangoBobs = Beer.builder()
                    .beerName("Mango Bobs")
                    .beerStyle("India Session Ale")
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .build();

            beerRepository.save(mangoBobs);

            beerInventoryRepository.save(BeerInventory.builder()
                    .beer(mangoBobs)
                    .quantityOnHand(100)
                    .build());

            Beer galaxyCat = Beer.builder()
                    .beerName("Galaxy Cat")
                    .beerStyle("Pale Ale")
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .build();

            beerRepository.save(galaxyCat);

            beerInventoryRepository.save(BeerInventory.builder()
                    .beer(galaxyCat)
                    .quantityOnHand(100)
                    .build());

            Beer pinball = Beer.builder()
                    .beerName("Pinball Porter")
                    .beerStyle("Porter")
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .build();

            beerRepository.save(pinball);

            beerInventoryRepository.save(BeerInventory.builder()
                    .beer(pinball)
                    .quantityOnHand(100)
                    .build());
        }
    }
}
