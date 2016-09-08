package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@SpringBootApplication
public class HelloClient {

    public static void main(String[] args) {
        SpringApplication.run(HelloClient.class, args);
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

    @Component
    public static class HelloService {

        @Value("${helloServiceUri}")
        private String helloServiceUri;

        private RestTemplate restTemplate = new RestTemplate();

        public String getGreeting(String uri) {
            return restTemplate.getForObject(uri, String.class);
        }
        
        public String getGreeting() {
            return getGreeting(helloServiceUri);
        }

    }

}