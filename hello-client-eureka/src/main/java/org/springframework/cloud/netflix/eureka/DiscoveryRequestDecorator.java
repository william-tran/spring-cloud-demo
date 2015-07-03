package org.springframework.cloud.netflix.eureka;

import org.springframework.http.HttpHeaders;

public interface DiscoveryRequestDecorator {

	HttpHeaders getHeaders();
}
