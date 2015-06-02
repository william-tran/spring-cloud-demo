# hello-client
Makes a call to hello-service with a URI configured via the app's configuration. This does not use any of the **Spring Cloud Services for Pivotal Cloud Foundry**.

## hello-service setup
This app requires a hello-service to be running; any of the three versions will suffice.

## To run on your CF environment:

Update the value of helloServiceUri in [src/main/resources/application.yml](src/main/resources/application.yml) to the random route that hello-service is using. Then ```mvn clean package && cf push``` to push the app as hello-client to the currently targeted space, using a random route

## To test the app:
``` curl [the random route outputted by cf push] ```

You should see the value returned by the running ```hello-service```. If the route to the service changes, you will need to update the app's configuration, eg via [src/main/resources/application.yml](src/main/resources/application.yml), this is avoided in the [hello-client-eureka](https://github.com/willtran-/spring-cloud-demo/hello-client-eureka) example. If ```hello-service``` is down, hello-client will respond with an error, this is avoided in the [hello-client-hystrix](https://github.com/willtran-/spring-cloud-demo/hello-client-hystrix) example.
