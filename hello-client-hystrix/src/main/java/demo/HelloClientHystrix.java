package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
public class HelloClientHystrix {

    public static void main(String[] args) {
        SpringApplication.run(HelloClientHystrix.class, args);
    }

    @Autowired
    private HelloService helloService;

    @RequestMapping("/")
    public String hello(@RequestParam(required=false) String uri) {
    	if (uri != null) {
    		return helloService.getGreeting(uri);
    	} else {
    		return helloService.getGreeting();
    	}
    }
    
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
    	return new RestTemplate();
    }

    @Component
    public static class HelloService {

        @Value("${helloServiceUri}")
        private String helloServiceUri;

        @Autowired
        private RestOperations restTemplate;

        @HystrixCommand(fallbackMethod = "getDefault")
        public String getGreeting(String uri) {
            return restTemplate.getForObject(uri, String.class);
        }
        
        @HystrixCommand(fallbackMethod = "getDefault")
        public String getGreeting() {
            return getGreeting(helloServiceUri);
        }

        String getDefault() {
            return "hello... something?";
        }

    }

}