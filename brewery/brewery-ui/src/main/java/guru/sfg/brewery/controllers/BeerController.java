package guru.sfg.brewery.controllers;

import guru.sfg.brewery.repositories.BeerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BeerController {

    private final BeerRepository repository;

    public BeerController(BeerRepository repository) {
        this.repository = repository;
    }


    @RequestMapping("/beer")
    public String getAllBeer(Model model) {
        model.addAttribute("beers", repository.findAll());
        return "beer/index";
    }
    @RequestMapping("beer/{name}")
    public String getBeerMyName(@PathVariable String name, Model model) {

        model.addAttribute("", repository.findByBeerName(name));
        return "";
    }
}
