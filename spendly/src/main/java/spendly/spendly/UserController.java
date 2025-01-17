package spendly.spendly;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    
    @GetMapping("/")
    public String homePage() {
        // Restituisce il file templates/index.html
        return "login";
    }
}
