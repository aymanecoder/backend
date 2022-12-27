package com.example.demo.controllers;

import com.example.demo.Entities.Scrapping;
import com.example.demo.Entities.User;
import com.example.demo.Repository.ScrappingRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.services.ScrapingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
//@RequestMapping()
//@CrossOrigin("*")
public class ScrappingController {

    @Autowired
    private ScrapingService service;



    @GetMapping("/webscraping")
    public List<Scrapping>   ScrppingByUrl() {

         return service.fetchScrapingList();

    }

}
