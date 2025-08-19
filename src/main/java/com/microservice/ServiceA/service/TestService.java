package com.microservice.ServiceA.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@Service
public class TestService {

	@Autowired
	RestTemplate restTemplate;

	private static final String BASE_URL = "http://localhost:8081/b";

	@CircuitBreaker(name = "serviceB", fallbackMethod = "testCircuiBreaker")
	//@Retry(name="serviceB", fallbackMethod = "testCircuiBreaker")
	//@RateLimiter(name="serviceB")
	//@Bulkhead(name="serviceB",type = Bulkhead.Type.THREADPOOL, fallbackMethod = "testCircuiBreaker")
	public String testCircuiBreaker() {
		//int count = 1;
		//System.out.println("Retry method called "+count++ )
		//try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
		return restTemplate.getForObject(BASE_URL, String.class);
	}
	
    // Fallback method signature must match (return type + Throwable)
    public String testCircuiBreaker(Throwable throwable) {
        return "Fallback: Service B is unavailable. Reason: " + throwable.getMessage();
    }

}
