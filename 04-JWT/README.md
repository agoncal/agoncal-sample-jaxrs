# Sample - JAX-RS 2.0 - JWT

## Purpose of this sample

The purpose of this sample is to show you how to generate a JWT token at login, and then check it when the REST service is invoked (through name binding)

[Read more on my blog](http://agoncal.wordpress.com/2012/01/16/wytiwyr-what-you-test-is-what-you-run/)

## Compile and package

Being Maven centric, you can compile and package it with `mvn clean compile`, `mvn clean package` or `mvn clean install`.

## Test the sample

This sample uses [Arquillian](http://arquillian.org/) for integration tests. So to execute it you have to :

* Run [WildFly](http://wildfly.org/) on a separate process
* `mvn clean test -Parquillian-wildfly-remote`

Or use the embedded version of [WildFly](http://wildfly.org/) 

* `mvn clean test -Parquillian-wildfly-managed`

<div class="footer">
    <span class="footerTitle"><span class="uc">a</span>ntonio <span class="uc">g</span>oncalves</span>
</div>