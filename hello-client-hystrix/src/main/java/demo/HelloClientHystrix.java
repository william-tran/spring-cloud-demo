package demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.cloud.netflix.hystrix.amqp.HystrixConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import io.pivotal.spring.cloud.config.java.CloudConnectorsConfig;
import io.pivotal.spring.cloud.service.hystrix.HystrixAmqpConnectionFactory;

@RestController
@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
public class HelloClientHystrix {
	
	private static final Logger log = LoggerFactory.getLogger(HelloClientHystrix.class);

    public static void main(String[] args) {
        SpringApplication.run(HelloClientHystrix.class, args);
    }

    @Autowired
    private HelloService helloService;

    @RequestMapping("/")
    public String hello() {
    	rabbitTemplate.convertAndSend("to-hello-service","hello-client got an http request");
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
    
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @RabbitListener(queues = "to-hello-client")
    public void receiveMessage(String message) {
    	log.info("received '{}' on to-hello-client queue", message);
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
    
    
    @Configuration
    @Profile("cloud")
    public static class CloudConfig extends CloudConnectorsConfig {
       @Bean
       @Primary
       public ConnectionFactory rabbitConnectionFactory() {
    	  ConnectionFactory rabbitConnectionFactory = connectionFactory().rabbitConnectionFactory();
          return rabbitConnectionFactory;
       }
       
       @Bean
       @HystrixConnectionFactory
       public ConnectionFactory hystrixConnectionFactory() {
          return connectionFactory().hystrixConnectionFactory();
       }
       
       @Bean
       public EurekaClientConfigBean eurekaClientConfig() {
          return connectionFactory().eurekaClientConfig();
       }
    }

}