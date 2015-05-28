package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public String hello() {
        return helloService.getGreeting();
    }

    @Component
    public static class HelloService {

        @Value("${helloServiceUri}")
        private String helloServiceUri;

        @Autowired
        private RestTemplate restTemplate;

        @HystrixCommand(fallbackMethod = "getDefault")
        public String getGreeting() {
            return restTemplate.getForObject(helloServiceUri, String.class);
        }

        String getDefault() {
            return "hello... something?";
        }

    }

}