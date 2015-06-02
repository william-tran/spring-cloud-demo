# hello-service-eureka
Returns a value configured via the **Config Server For Pivotal Cloud Foundry**, and registers itself as a service via the **Service Registry for Pivotal Cloud Foundry**.

## Config Server setup
Please see [hello-service-config-server](https://github.com/willtran-/spring-cloud-demo/tree/master/hello-service-config-server)

## Service Registry setup
If a Service Registry called ```eureka``` hasn't been set up in the space, you'll need to set one up. See the relevant [docs section](http://cf-p1-docs-staging.cfapps.io/spring-cloud-services/service-registry/#create-service-registry). Specifically: 

* Use ```eureka``` as the name of the service instance.

## To run on your CF environment:
``` mvn clean package && cf push ```
This will push the app as hello-service to the currently targeted space, using a random route, and bind the app to the services ```config-server``` and ```eureka```.

## To test the app:
You should be able to do all the same things as [hello-service-config-server](https://github.com/willtran-/spring-cloud-demo/tree/master/hello-service-config-server). Additionally, you should be able to see hello-service registered in the Service Registry dashboard. If you stop the hello-service app, you should see an entry in the Cancelled Leases section in the History tab of the Service Registry dashboard.
