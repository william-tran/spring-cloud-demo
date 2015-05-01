package demo;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HelloIntegrationTest extends BaseWebIntegrationTest {
    
    @Test
    public void testHello() throws Exception {
        String response = restTemplate.getForObject("/", String.class);
        assertEquals("Hello world!", response);
    }

}
