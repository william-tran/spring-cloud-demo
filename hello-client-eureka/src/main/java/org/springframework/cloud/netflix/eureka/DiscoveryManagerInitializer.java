/*
 * Copyright 2013-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.netflix.eureka;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.discovery.DiscoveryClient.DiscoveryClientOptionalArgs;
import com.netflix.discovery.DiscoveryManager;
import com.netflix.discovery.EurekaClientConfig;
import com.sun.jersey.api.client.filter.ClientFilter;

/**
 * @author Spencer Gibb
 */
public class DiscoveryManagerInitializer {

	@Autowired
	private EurekaClientConfig clientConfig;

	@Autowired
	private EurekaInstanceConfig instanceConfig;

	@Autowired(required = false)
	private Collection<DiscoveryRequestDecorator> requestDecorators;

	public synchronized void init() {
		if (DiscoveryManager.getInstance().getDiscoveryClient() == null) {
			DiscoveryClientOptionalArgs args = new DiscoveryClientOptionalArgs();
			if (requestDecorators != null) {
				List<ClientFilter> filters = new ArrayList<ClientFilter>();
				for (DiscoveryRequestDecorator decorator : requestDecorators) {
					filters.add(new ClientFilterAdapter(decorator));
				}
				args.setAdditionalFilters(filters);
			}
			DiscoveryManager.getInstance().initComponent(this.instanceConfig,
					this.clientConfig, args);
		}
		if (ApplicationInfoManager.getInstance().getInfo() == null) {
			ApplicationInfoManager.getInstance().initComponent(this.instanceConfig);
		}
	}

}
