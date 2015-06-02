# hello-client-eureka
Makes a call to hello-service with a URI provided by the **Service Registry for Pivotal Cloud Foundry**.

## hello-service setup
This app requires hello-service-eureka to be running. In this example we'll do a blue-green deployment of hello-service-eureka, and consume that in hello-client-eureka.

1. Shut down any running instances of hello-service.
1. Run the following commands to start both a blue and a green deployment of hello-service-eureka:

  ```
cd ../hello-service-eureka
mvn package
cf push hello-service-blue --no-start
cf set-env hello-service-blue SPRING_PROFILES_ACTIVE blue
cf start hello-service-blue
cf push hello-service-green --no-start
cf set-env hello-service-green SPRING_PROFILES_ACTIVE green
cf start hello-service-green
  ```

## To run on your CF environment:
```cd``` to the ```hello-client-eureka``` folder and ```mvn clean package && cf push``` to push the app as hello-client to the currently targeted space, using a random route

## To test the app:
``` curl [the random route outputted by cf push] ```

You should see the value returned by either ```hello-service-blue``` ("Hello blue!") or ```hello-service-green``` ("Hello green!"), and this should switch each time you run ```curl```. If you stop either  ```hello-service-blue``` or ```hello-service-green```, you should only see responses from the live service. Try starting and stopping each of the blue and green services. 
