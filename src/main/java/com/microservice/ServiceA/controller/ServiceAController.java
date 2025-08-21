package com.microservice.ServiceA.controller;


import java.util.concurrent.ExecutionException;

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
    public String serviceA() throws InterruptedException, ExecutionException {
    	//Thread.sleep(5000);
       return testService.testCircuiBreaker().get();
    }
    
    @GetMapping("/service")
    public String createOrder(){
    	return testService.callService();
    }
    
//    @GetMapping("/timeDecorate")
//    //@TimeLimiter(name="serviceB", fallbackMethod = "testTimeLimiter")
//    public String timeDecorate(){
//    	return testService.slowDecoratedMethod();
//    }
    
//    public CompletableFuture<String> testTimeLimiter(Throwable throwable) {
//    	return CompletableFuture.completedFuture(
//    	        "Fallback: Service B is unavailable. Reason: " + throwable.getMessage()
//    	    );
//    }
    
}
