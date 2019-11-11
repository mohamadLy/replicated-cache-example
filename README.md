# Replicated Cache Example using Akka and Hazelcast
This is example that shows how to use replicated cache between akka-cluster nodes it could be generalize to any cluster. Also the ReplicatedMap data structure from embedded hazelcast is used as the replicated cache.

This example assume that you have a basic understand of how akka cluster work and how to use docker-compose

## Running
We need to run each node runs on a docker container. Hence let's build our docker image. This can be achieve using the command "sbt docker:publishLocal", this will result to the creation of image mohamadly/distributed-cache-demo. You can run "docker images" to verify if the image had been create

## API
In order to interact with the system, a Akka http server had been created that listen at port 800x where x correspond to the requested node, 
### Interacting with node
* Bring up seed node
  * docker-compose up seed
```
CONTAINER ID        IMAGE                                  COMMAND                  CREATED             STATUS              PORTS                                            NAMES
a2b889324ead        mohamadly/distributed-cache-demo:0.1   "/opt/docker/bin/dis…"   2 days ago          Up 10 seconds       0.0.0.0:2552->2552/tcp, 0.0.0.0:8000->8000/tcp   replicated-cache-example_seed_1
```

hazelcast cluster is formed but only have one node on it since only the seed node is started now 
```
seed_1   |
seed_1   | Members {size:1, ver:1} [
seed_1   |      Member [172.20.0.2]:5701 - f8d5deb4-01b6-423f-89da-f219c36b4949 this
seed_1   | ]
seed_1   |

```

Seed running on listening on port 2552 and 8000
** 
The port for the nodes are the following:
*a
