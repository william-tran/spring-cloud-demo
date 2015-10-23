package demo;

import org.springframework.cloud.client.ServiceInstance;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ServiceInstanceData {
	@JsonIgnore
	private ServiceInstance delegate;

	public ServiceInstanceData() {
	}

	public ServiceInstanceData(ServiceInstance delegate) {
		super();
		this.delegate = delegate;
	}

	public String getServiceId() {
		return delegate.getServiceId();
	}

	public String getHost() {
		return delegate.getHost();
	}

	public int getPort() {
		return delegate.getPort();
	}

	public boolean isSecure() {
		return delegate.isSecure();
	}

	public String getUri() {
		return delegate.getUri().toString();
	}

}
