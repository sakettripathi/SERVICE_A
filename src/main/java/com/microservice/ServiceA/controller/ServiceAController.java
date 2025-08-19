package com.microservice.ServiceA.controller;

import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.ServiceA.service.TestService;


@RestController
@RequestMapping("/a")
public class ServiceAController {
    
    @Autowired
    TestService testService;


    @GetMapping
    public String serviceA() {
       return testService.testCircuiBreaker();
    }

}
