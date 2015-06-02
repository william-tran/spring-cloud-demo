# hello-service
Returns a value configured via configuration on the classpath. This doesn't use any of the Spring Cloud Services.

## To run on your CF environment:
``` mvn clean package && cf push ```
This will push the app as hello-service to the currently targeted space, using a random route

## To test the app:
``` curl [the random route outputted by cf push] ```

You should see the value configured by ```hello.message``` in [src/main/resources/application.yml](src/main/resources/application.yml). In order to change this value, you have to either have to edit the application.yml and redeploy (breaking the [12 Factor Config rule](http://12factor.net/config)) or 

```cf set-env HELLO_MESSAGE "some other message"``` 

which is not under version control or easily tracable.
