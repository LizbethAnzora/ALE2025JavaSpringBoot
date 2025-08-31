package com.ALE2025.ClinicaMedica.Controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class homeController {
   
    @RequestMapping
    public String index() {
        return "home/index";
    }
}


