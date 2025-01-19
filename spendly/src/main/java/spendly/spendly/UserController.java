package spendly.spendly;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class UserController {
    
    private final UserRepository userRepository;

    UserController(UserRepository repository){
        userRepository=repository;
    }

    @GetMapping("/")
    public String homePage() {
        // Restituisce il file templates/index.html
        return "login";
    }

    @GetMapping("/user")
    Iterable<User> getUsers(){
        return  userRepository.findAll();
    }

    @PostMapping("/user")
    User creaUser(@RequestBody User newUser){   //Valore preso dal body della richiesta
        return userRepository.save(newUser);
    }
    
}
