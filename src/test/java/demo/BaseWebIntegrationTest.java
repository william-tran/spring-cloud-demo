package demo;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest({ "server.port=0" })
public abstract class BaseWebIntegrationTest {
    @Value("${local.server.port}")
    private int port;

    protected RestTemplate restTemplate;

    @PostConstruct
    public void init() {
        restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Arrays.asList(new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
                    throws IOException {
                HttpRequest wrappedRequest = new HttpRequestWrapper(request) {
                    @Override
                    public URI getURI() {
                        return UriComponentsBuilder
                                .fromUri(request.getURI())
                                .scheme("http")
                                .host("localhost")
                                .port(port)
                                .build().toUri();
                    }
                };
                return execution.execute(wrappedRequest, body);
            }
        }));
    }

}
