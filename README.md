# Clojure Http Server

An [8th Light Cob Spec](https://github.com/8thlight/cob_spec) compliant Http server built in clojure

## Get up and running

### Installs

Required installs

- [Java (v8 recommended)](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Clojure](https://clojure.org/guides/getting_started)
- [Leiningen](https://leiningen.org)
- [Maven](https://maven.apache.org/install.html)

Clone both this repo and the cob spec repo with:

```sh
> git clone https://github.com/andrewMacmurray/clj-http-server.git
> git clone https://github.com/8thlight/cob_spec.git
```

### Building the Server

To build the server jar `cd` into the `clj-http-server` directory and run:

```sh
> lein deps
> lein uberjar
```

### Building the Cob Spec

One gotcha I encountered with this project is that the clojure jar can take a little while to boot up when running the tests, this can sometimes result in the tests running before the server is listening on the correct port.

As a workaround increase the value of `Thread.sleep(..)` on [this line of the Cob Spec](https://github.com/8thlight/cob_spec/blob/master/src/main/java/Server.java#L22), `5000` worked for me but this may vary for different machines.

Then build the cob spec with:

```sh
> mvn package
```

### Running the Suites

To run the unit test suite cd into the `clj-http-server` directory and run:

```sh
> lein spec
```

To run the cob spec suite follow the instructions on the [cob spec repo](https://github.com/8thlight/cob_spec) to start the fitnesse server. The clojure server jar can be located at `clj-http-server/target/clj-http-server-0.1.0-SNAPSHOT-standalone.jar` so the start command should be:

```sh
> java -jar ../clj-http-server/target/clj-http-server-0.1.0-SNAPSHOT-standalone.jar
```

After setting these follow the instructions to run the test suite

## About the Project

The structure of the project was largely inspired by [ring](https://github.com/ring-clojure/ring), a popular clojure http server abstraction.

The core pieces of the application are:

- `Request`
- `Response`
- `Handlers`
- `Middleware`

The `Request` and `Response` are clojure maps representing data about the request and response. Handlers are functions that take a Request map and return a Response map. And Middleware are functions that take a handler and return a new handler decorated with extra functionality (such as a modified request and response, or functionality to return a different response given a particular request).
