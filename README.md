Excersite have two part: Backend using Spring Boot(Java), Front End using Angular.

## Backend (contained in Uploader folder)

### Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.example.Uploader.UploaderApplication` class from your IDE (Intellij IDEA, Eclipse).

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```
## Frontend (contained in image-angular folder)

### Install NodeJs and Angular on local

1. Get and install NodeJs from [here](https://nodejs.org/en/)
2. Install Angular by run `npm i -g @angular/cli`

### Install npm packages

Install the npm packages described in the `package.json` and verify that it works:

```shell
npm install
npm start
```

