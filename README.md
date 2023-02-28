# Building a Cloud-Based Distributed System for Storing and Retrieving Audio Data Using Java and Oracle Cloud Infrastructure

In this project, we developed a distributed software system using Java and Oracle Cloud Infrastructure to store and access audio files with their 
relevant attributes. Our system is thread-safe and uses an executor and concurrent hash map to handle client requests. We have also created an OpenAPI 
specification on SwaggerHub to document the system's endpoints and operations. The system has been deployed on the Oracle Cloud Infrastructure and is 
functioning as intended.

URL to the project: http://168.138.93.110:9090/coen6731/audio

To test the project, you can use Postman and try accessing the URL.

In addition, an OpenAPI specification has been created using SwaggerHub to document the endpoints and operations of our distributed software system for storing audio items. This standardized documentation helps developers to interact with the system's API. You can access the SwaggerHub link here: https://app.swaggerhub.com/apis/ITSVISHRUTHKHATRI/Audio/1.0.0
https://app.swaggerhub.com/apis/PRANAVJHA/AudioAPI/1.0.2

### Import To Eclipse Java EE Edition

1. Download the zip of this repo and unzip it.

2. Import the project into eclipse as a maven project.

   ![import](img/import.gif)

3. Config Run for development.

   ![](img/jettyrun.gif)

4. Config Run for Deployment

   ![deprun](img/deprun.gif)

5. Access servlet.

   ![image-20230115133718047](img/get.png)

   As you have the servlet at `org.example.controller.HelloServlet.java`.



### CMD

#### To develop the project.

``` bash 
mvn jetty:run
```

> The server config for development is at `pom.xml:86`.
>
> Feel free to configure it.



#### To deploy the project.

``` bash
mvn clean install exec:exec
```
> The server config for deployment is in `org.example.EmbeddingJettyStarter`.
>
> Feel free to configure it.



### Configuration of Jetty

#### Configure in coding

In `org.example.EmbeddingJettyStarter`, you can directly change the port or the context path by:

``` java
Server server = new Server(8080);		// change the port number 
// ...
context.setContextPath("/new_context");   // change the context path
// ...
```

Then you can access the servlet by:

http://localhost:8080/new_context/HelloServlet

At the same time, please make sure your servlet's annotation value mapping is valid for the context path:

``` java
@WebServlet(name = "HelloServlet", value = "HelloServlet")
```

Do not add the root in the front:

``` java
@WebServlet(name = "HelloServlet", value = "/HelloServlet")
```
