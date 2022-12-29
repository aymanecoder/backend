package com.example.demo.services;

import com.example.demo.Entities.Scrapping;
import com.example.demo.Repository.ScrappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScrapingServiceImp implements ScrapingService{
    @Autowired
    private ScrappingRepository scrapingRepository;
    @Override
    public List<Scrapping> fetchScrapingList() {
//        System.out.println(scrapingRepository.findAll());
        return scrapingRepository.findAll();
    }
}
