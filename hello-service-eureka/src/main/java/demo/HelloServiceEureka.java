package demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Bean
    @RefreshScope
    @ConfigurationProperties(prefix = "hello")
    HelloProperties helloProperties() {
        return new HelloProperties();
    }	

    @RequestMapping("/")
    public String hello() {
    	rabbitTemplate.convertAndSend("to-hello-client","hello-service got an http request");
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
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @RabbitListener(queues = "to-hello-service")
    public void receiveMessage(String message) {
    	log.info("received '{}' on to-hello-service queue", message);
    }
    
    @Bean
	public Queue toHelloServiceQueue() {
		return new Queue("to-hello-service", true);
	}
    
    @Bean
	public Queue toHelloClientQueue() {
		return new Queue("to-hello-client", true);
	}
    
    @Bean
	public DirectExchange exchange() {
		return new DirectExchange("hello", true, false);
	}

	@Bean
	public Binding toHelloClientBinding() {
		return BindingBuilder.bind(toHelloClientQueue()).to(exchange()).with(toHelloClientQueue().getName());
	}
	
	@Bean
	public Binding toHelloServiceBinding() {
		return BindingBuilder.bind(toHelloServiceQueue()).to(exchange()).with(toHelloServiceQueue().getName());
	}
}
