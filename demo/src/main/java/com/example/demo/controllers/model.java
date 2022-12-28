package com.example.demo.controllers;

//import org.springframework.ui.Model;

import com.example.demo.model.Model;
import org.h2.util.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;

@RestController
public class model {

   @GetMapping("/model")
    public String getMetric() throws Exception {

       Model.main(null);
//        for (String recommandedDatum : Model.recommandedData) {
//            System.out.println(recommandedDatum);
//        }

       return Model.metrics();


   }
}
