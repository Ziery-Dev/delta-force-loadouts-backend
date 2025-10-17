package com.ziery.DeltaForceLoadouts.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testesecurity")
public class TesteSecurityController {

    @RequestMapping
    public String testeGet(){
        return "Passou no teste simples security";
    }
}
