# Sample - Arquilian - What You Test Is What You Run

## Purpose of this sample

The purpose of this sample is to show you how to test an EJB in several ways :

* Unit testing with Mockito
* Integration testing with the standard EJBContainer of the EJB 3.1
* Integration testing with Arquillian

[Read more on my blog](http://agoncal.wordpress.com/2012/01/16/wytiwyr-what-you-test-is-what-you-run/)

## Class diagram

![image](https://github.com/agoncal/agoncal-sample-arquilian/raw/master/01-wytiwyr/src/main/webapp/classdiag.png)

## Compile and package

Being Maven centric, you can compile and package it with `mvn clean compile`, `mvn clean package` or `mvn clean install`. The `package` and `install` phase will automatically trigger the unit tests. Once you have your war file, you can deploy it.

## Deploy the sample

This sample has been tested with GlassFish 3.1.2 in several modes :

* GlassFish runtime : [download GlassFish](http://glassfish.java.net/public/downloadsindex.html), install it, start GlassFish (typing `asadmin start-`domain) and once the application is packaged deploy it (using the admin console or the command line `asadmin deploy target/sampleArquilianWytiwyr.war`)
* GlassFish embedded : use the [GlassFish Maven Plugin](http://maven-glassfish-plugin.java.net/) by running `mvn clean package embedded-glassfish:run`

## Execute the sample

Once deployed you can call the [ItemEJB REST service](rs/items) and see all the books in the database. You can also run some [curl](http://curl.haxx.se/) commands :

* `curl -X GET http://localhost:8080/sampleArquilianWytiwyr/rs/items`
* `curl -X GET -H "accept: application/json" http://localhost:8080/sampleArquilianWytiwyr/rs/items`

The purpose of this sample is to execute unit and integration tests. So to execute it you can run :

* `mvn test` : this will execute the unit test ItemEJBTest which uses Mockito
* `mvn integration-test` : this will execute both integration tests `ItemEJBWithArquillianIT` and `ItemEJBWithoutArquillianIT`

<div class="footer">
    <span class="footerTitle"><span class="uc">a</span>ntonio <span class="uc">g</span>oncalves</span>
</div>