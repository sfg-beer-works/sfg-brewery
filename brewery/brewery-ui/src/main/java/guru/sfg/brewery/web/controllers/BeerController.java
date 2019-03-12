package guru.sfg.brewery.web.controllers;


import guru.sfg.brewery.repositories.BeerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


    @RequestMapping("/beer")
    @Controller
    public class BeerController {

        private BeerRepository beerRepository;

        public BeerController(BeerRepository beerRepository) {
            this.beerRepository = beerRepository;
        }

        @GetMapping({"/beers", "/breweries/index", "/breweries/index.html", "/breweries.html"})
        public String listBeers(Model model) {
            model.addAttribute("beers", beerRepository.findAll());
            return "beer/index";
        }
    }


