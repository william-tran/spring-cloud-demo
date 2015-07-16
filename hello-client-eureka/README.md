# hello-client-eureka-oauth
Makes a call to itself with a URI provided by the **Service Registry for Pivotal Cloud Foundry**. Requests to the Eureka Server include an OAuth2 token so they can be authenticated. 

## setup
1. Grab and run the [UAA](https://github.com/cloudfoundry/uaa) locally. The latest release of the war can be found on [Maven Central](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.cloudfoundry.identity%22%20AND%20a%3A%22cloudfoundry-identity-uaa%22).
2. Clone https://github.com/pivotal-cf/spring-cloud-service-broker, checkout branch feature/eureka-oauth, and run eureka-server
3. Run this app, wait 60 seconds so the service can finish registering 

## test
1. Hit localhost:9999, you should see a greeting message. The app makes a call to itself using the loadBalancedRestTemplate.
2. Comment out the eureka.client.oauth2 section in [application.yml](src/main/resources/application.yml). do step 1 and you'll get an error.
 