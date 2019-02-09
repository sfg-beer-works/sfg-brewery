package guru.sfg.brewery.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by jt on 2019-01-26.
 */
@Controller
public class IndexController {

    @RequestMapping({"", "/"})
    public String index(){

        return "index";
    }
}
