package demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableDiscoveryClient
public class HelloServiceEureka {
	private static final Logger log = LoggerFactory.getLogger(HelloServiceEureka.class);

    public static void main(String[] args) {
        SpringApplication.run(HelloServiceEureka.class, args);
    }
    
	@Value("${vcap.application.uris[0]:unknown}")
	private String hostname;

    @Bean
    @RefreshScope
    @ConfigurationProperties(prefix = "hello")
    HelloProperties helloProperties() {
        return new HelloProperties();
    }

    @RequestMapping("/")
    public String hello() {
    	log.debug("responding with {}", helloProperties().getMessage());
    	return "Message from " + hostname + " : " + helloProperties().getMessage();
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
