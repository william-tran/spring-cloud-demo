# hello-client-hystrix
Makes a call to hello-service with a URI provided by the **Service Registry for Pivotal Cloud Foundry**, and uses a circuit breaker provided by the **Circuit Breaker for Pivotal Cloud Foundry**.

## hello-service setup
In this example, we'll need a blue-green deployment of ```hello-service```. Please see the **hello-service setup** in [hello-client-eureka](https://github.com/willtran-/spring-cloud-demo/tree/master/hello-client-eureka) to set this up.

## Circuit Breaker setup
If a Circuit Breaker called ```hystrix``` hasn't been set up in the space, you'll need to set one up. See the relevant [docs section](http://cf-p1-docs-staging.cfapps.io/spring-cloud-services/circuit-breaker/#create-dashboard). Specifically: 

* Use ```hystrix``` as the name of the service instance.

## To run on your CF environment:
Run ```mvn clean package && cf push``` to push the app as hello-client to the currently targeted space, using a random route

## To test the app:
``` $ while true; do curl [the random route outputted by cf push]; done ```

You should see the value returned by either ```hello-service-blue``` ("Hello blue!") or ```hello-service-green``` ("Hello green!"), and this should switch on each invocation of curl.  You can view the state of the calls to ```hello-service``` on the Circuit Breaker Dashboard. 

Shut down ```hello-service-green``` with ```cf stop hello-service-green```. In the Circuit Breaker Dashboard you'll see the error rate climb and the circuit open. In the curl output, you'll see ```Hello blue!hello... something?Hello blue!hello... something?```... for a few seconds and then ```hello... something?``` continuously once the circuit is fully open. After about 30 seconds, ```hello-client-hystrix``` will re-fetch the list of live services from the Service Registry, and then stop attempting to make calls to ```hello-service-green``` since that will no longer be in the list. At this point, only sucessful calls will be made, and the circuit will close. 

At no point is an error ever sent to curl; instead, the fallback response "hello... something?" is sent. 

Try switching on/off ```hello-service-blue``` and ```hello-service-green```, and switch both off, and see the behaviour of the Circuit Breaker Dashboard and curl output. 
