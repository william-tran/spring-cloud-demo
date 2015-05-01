package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

    public static void main(String[] args) {
       SpringApplication.run(Application.class, args);
    }
    
    @Autowired
    private HelloProperties helloProperties;
    
    @RequestMapping("/")
    public String hello() {
        return helloProperties.getMessage();
    }
    
    @Component
    @RefreshScope
    @ConfigurationProperties(prefix="hello")
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
