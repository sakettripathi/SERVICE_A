package com.microservice.ServiceA.service;


import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

@Service
public class TestService {
	
	TimeLimiterConfig config = TimeLimiterConfig.custom()
			.timeoutDuration(Duration.ofSeconds(1))
			.cancelRunningFuture(true)
			.build();

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	WebClient.Builder webClientBuilder;

	private static final String BASE_URL = "http://localhost:8081/b";

	//@Async("urlProcessor")
	//@CircuitBreaker(name = "serviceB", fallbackMethod = "testCircuiBreaker")
	//@Retry(name="serviceB", fallbackMethod = "testCircuiBreaker")
	//@RateLimiter(name="serviceB")
	 @Bulkhead(name="serviceB",type = Bulkhead.Type.THREADPOOL, fallbackMethod = "testCircuiBreaker")
	//@TimeLimiter(name="serviceB", fallbackMethod = "testCircuiBreaker")
	public CompletableFuture<String> testCircuiBreaker() {
		 //System.out.println(Thread.currentThread().getName());
//	        return CompletableFuture.supplyAsync(() -> {
//	            System.out.println(Thread.currentThread().getName() + " entered callServiceB");
//				//Thread.sleep(3000); // simulate slow service
//				String result = restTemplate.getForObject(BASE_URL, String.class);
//				return result;
//	           // return "Response from Service B";
//	        });
		 	System.out.println(Thread.currentThread().getName() + " entered callServiceB");

		    String result = restTemplate.getForObject(BASE_URL, String.class);
		    return CompletableFuture.completedFuture(result);
		
		//String result = restTemplate.getForObject(BASE_URL, String.class);
		//return CompletableFuture.completedFuture(result);
	}
	
    // Fallback method signature must match (return type + Throwable)
    public CompletableFuture<String> testCircuiBreaker(Throwable throwable) {
    	 System.out.println("fallback method======== "+Thread.currentThread().getName());
        return CompletableFuture.completedFuture("Fallback: Service B is unavailable. Reason: " + throwable.getMessage());
    }
    
    
	public String callService() {

		TimeLimiter timeLimiter = TimeLimiter.of(config);

		try {
			return timeLimiter.executeFutureSupplier(() -> CompletableFuture.supplyAsync(() -> {
				try {
					Thread.sleep(3000);
//					return restTemplate.getForObject(BASE_URL, String.class);
		            webClientBuilder.build()
                    .get()
                    .uri(BASE_URL)
                    .retrieve()
                    .bodyToMono(String.class)
                    .toFuture();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return "Service Response";
			}));
		} catch (Exception e) {
			return "Fallback due to timeout!";
		}
	}
    
    
    
    
//	public CompletableFuture<String> slowMethod() {
//		 return CompletableFuture.supplyAsync(() -> {
//	            try {
//	                Thread.sleep(5000); // Simulate 5s delay
//	            } catch (InterruptedException e) {
//	                return "Interrupted!";
//	            }
//	            System.out.println("Hello-----------");
//	            return "hello";
//	        });
//	}
	
//	public String slowDecoratedMethod() {
//		 TimeLimiter timeLimiter = TimeLimiter.of(config);
//
//		    try {
//		        // CancelRunningFuture=true will abort the WebClient subscription
//		        return timeLimiter.executeFutureSupplier(() ->
//		            webClientBuilder.build()
//		                    .get()
//		                    .uri(BASE_URL)
//		                    .retrieve()
//		                    .bodyToMono(String.class)
//		                    .toFuture()
//		        );
//		    } catch (Exception e) {
//		        System.out.println("Timeout Exception: " + e);
//		        return "Fallback: Service B took too long!";
//		    }
//	}
}
