package demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class HelloService {
    private static final Logger log = LoggerFactory.getLogger(HelloService.class);

    public static void main(String[] args) {
        SpringApplication.run(HelloService.class, args);
    }

    @Bean
    @ConfigurationProperties(prefix = "hello")
    HelloProperties helloProperties() {
        return new HelloProperties();
    }

    @RequestMapping("/")
    public String hello() {
        log.debug("responding with {}", helloProperties().getMessage());
        return helloProperties().getMessage();
    }

    public static class HelloProperties {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
