# hello-service-config-server
Returns a value configured via the **Config Server For Pivotal Cloud Foundry**.

## Config Server setup
If a Config Server called ```config-server``` hasn't been set up in the space, you'll need to set one up. See the relevant [docs section](http://cf-p1-docs-staging.cfapps.io/spring-cloud-services/config-server/#create-config-server). Specifically: 

1. Use ```config-server``` as the name of the service instance.
2. Fork [https://github.com/willtran-/spring-cloud-config](https://github.com/willtran-/spring-cloud-config)
3. Use github, and the url to your fork, eg ```https://github.com/my-github-account/spring-cloud-config``` in the server's configuration.

## To run on your CF environment:
``` mvn clean package && cf push ```
This will push the app as hello-service to the currently targeted space, using a random route, and bind the app to the ```config-server``` service.

## To test the app:
``` curl [the random route outputted by cf push] ```

You should see the value configured by ```hello.message``` in the [hello-service.yml](https://github.com/willtran-/spring-cloud-config/blob/master/hello-service.yml) in your repo. In order to change this value, you can edit the file directly in github, and then  

```curl -X POST [the random route outputted by cf push]/refresh```

to trigger hello-service to update its configuration. You can also

``` 
cf set-env hello-service SPRING_PROFILES_ACTIVE development 
cf restart hello-service
```

to make the app use [hello-service-development.yml](https://github.com/willtran-/spring-cloud-config/blob/master/hello-service.yml). To change the log level at runtime, update the value in your configuration hosted on github:

```
logging.level:
  demo: DEBUG
```
trigger the app to update its configuration:

```curl -X POST [the random route outputted by cf push]/refresh```

And then watch the logs, you should see DEBUG level output:

```cf logs hello-service```

