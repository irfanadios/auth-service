package project.irfanadios.authservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorld {
    @GetMapping("/api/auth-service/hello-world")
    public String helloWorld() {
        return "Hello World";
    }
}
