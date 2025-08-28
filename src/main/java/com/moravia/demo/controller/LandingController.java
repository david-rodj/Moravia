package com.moravia.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LandingController {

    //http://localhost:8081/
    @GetMapping("/")
    public String landing() {
        return "index"; 
    }
}
