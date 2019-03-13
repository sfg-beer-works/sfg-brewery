package guru.sfg.brewery.web.controllers;


import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.repositories.BeerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;


@RequestMapping("/beers")
    @Controller

    public class BeerController {

       //ToDO: Add service
        private BeerRepository beerRepository;

        public BeerController(BeerRepository beerRepository) {
            this.beerRepository = beerRepository;
        }

        @RequestMapping("/find")
        public String findBeers(Model model){
            model.addAttribute("beer", Beer.builder().build());
            return "beers/findBeers";
        }

    @GetMapping
    public String processFindForm(Beer beer, BindingResult result, Model model){
        // allow parameterless GET request for /beers to return all records

        if (beer.getBeerName() == null) {
            beer.setBeerName(""); // empty string signifies broadest possible search
        }

        // find beers by name
        Page<Beer> pagedResult = beerRepository.findAllByBeerName(beer.getBeerName(), createPageRequest(0,10,Sort.Direction.DESC,"beerName"));
        List<Beer> beerList = pagedResult.getContent();
        if (beerList.isEmpty()) {
            // no beers found
            result.rejectValue("beerName", "notFound", "not found");
            return "beers/findBeers";
        } else if (beerList.size() == 1) {
            // 1 beer found
            beer = beerList.get(0);
            return "redirect:/beers/" + beer.getId();
        } else {
            // multiple beers found
            model.addAttribute("selections", beerList);
            return "beers/beerList";
        }
    }


    @GetMapping("/{beerId}")
    public ModelAndView showBeer(@PathVariable UUID beerId) {
        ModelAndView mav = new ModelAndView("beers/beerDetails");
        mav.addObject(beerRepository.findById(beerId).get());
        return mav;
    }


    private PageRequest createPageRequest(int page, int size, Sort.Direction sortDirection, String propertyName) {
        return PageRequest.of(page,
                size,
                new Sort(sortDirection, propertyName));
    }
    }


