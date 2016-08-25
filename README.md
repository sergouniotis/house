# JSF APP USING WILDFLY SWARM

Run the latest version of the wildfly swarm with Docker and Docker-compose.

It will give you the ability to keep track of expenditures.

Based on the official images:

* [java](https://hub.docker.com/_/openjdk/)
* [postgres](https://hub.docker.com/_/postgres/)

# Requirements

## Setup

1. Install [Maven](https://maven.apache.org/install.html).
2. Install [Docker](http://docker.io).
3. Install [Docker-compose](http://docs.docker.com/compose/install/).
4. Clone this repository

# Usage

Build using maven

```bash
$ mvn clean install docker:build -Dmaven.test.skip=true
```

Start the app stack using *docker-compose*:

```bash
$ docker-compose up
```

You can also choose to run it in background (detached mode):

```bash
$ docker-compose up -d
```