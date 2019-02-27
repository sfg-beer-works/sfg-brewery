package guru.sfg.brewery.web.controllers;

import guru.sfg.brewery.repositories.BreweryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/brewery")
@Controller
public class BreweryController {

    // TODO: Create a service for brewery
    private BreweryRepository breweryRepository;

    public BreweryController(BreweryRepository breweryRepository) {
        this.breweryRepository = breweryRepository;
    }

    @GetMapping({"", "/index", "index.html"})
    public String listBreweries(Model model) {

        model.addAttribute("breweries", breweryRepository.findAll());
        return "breweries/index";
    }
}
