package com.microservice.ServiceA;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

//@EnableAsync
@SpringBootApplication
public class ServiceAApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceAApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
    
	@Bean
	public WebClient.Builder webClientBuilder() {
		return WebClient.builder();
	}
	
	@Bean(name="urlProcessor")
	public Executor urlExecuterBean() {
		ThreadPoolTaskExecutor executer = new ThreadPoolTaskExecutor();
		executer.setCorePoolSize(10);
		executer.setMaxPoolSize(100);
		executer.setQueueCapacity(100);
		executer.setThreadNamePrefix("url-processor-thread-");
		executer.initialize();
		return executer;
	}
}
