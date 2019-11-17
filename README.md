# Replicated Cache Example using Akka and Hazelcast
This is example that shows how to use replicated cache between akka-cluster nodes it could be generalize to any cluster. Also the ReplicatedMap data structure from embedded hazelcast is used as the replicated cache.

This example assume that you have a basic understand of how akka cluster work and how to use docker-compose

## Running
We need to run each node runs on a docker container. Hence let's build our docker image. This can be achieve using the command 
* "sbt docker:publishLocal"  
 This will result to the creation of image mohamadly/distributed-cache-demo. You can run 
* "docker images"   
 to verify if the image had been create

## API
In order to interact with the system, a Akka http server had been created that listen at port 800x where x correspond to the requested node, 
### Interacting with node
* Bring up seed node
  * docker-compose up seed(note we are only going to use one seed node)
  Seed node is up and running, listening on 2 ports 2552 and 8000
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
* Bring up node #1
  * docker-compose up node1
node #1 is up and running listening internally on port 8000 and map to port 8001 on the host
```
 ~ docker container ls
CONTAINER ID        IMAGE                                  COMMAND                  CREATED             STATUS              PORTS                                            NAMES
a2b889324ead        mohamadly/distributed-cache-demo:0.1   "/opt/docker/bin/dis…"   2 days ago          Up 15 minutes       0.0.0.0:2552->2552/tcp, 0.0.0.0:8000->8000/tcp   replicated-cache-example_seed_1
faa842fdb6e7        mohamadly/distributed-cache-demo:0.1   "/opt/docker/bin/dis…"   2 days ago          Up About a minute   0.0.0.0:8001->8000/tcp                           replicated-cache-example_node1_1
```

Node #1 just join the hazelcast cluster
```
node1_1  | 
node1_1  | Members {size:2, ver:2} [
node1_1  |      Member [172.20.0.2]:5701 - f8d5deb4-01b6-423f-89da-f219c36b4949
node1_1  |      Member [172.20.0.3]:5701 - 80720d62-2d53-463e-9450-cab3a9191ed7 this
node1_1  | ]
node1_1  | 
```
 * Add an employee to the cache
```
➜ ~ http POST localhost:8000/insert/employee first_name="Mike" last_name="Bleue" id=5                                                                                                                       [35/52]
HTTP/1.1 200 OK                                                                                                                                                                                                    
Content-Length: 79                                                                                                                                                                                                 
Content-Type: application/json                                                                                                                                                                                     
Date: Thu, 14 Nov 2019 16:28:23 GMT                                                                                                                                                                                
Server: akka-http/10.1.7                                                                                                                                                                                           
                                                                                                                                                                                                                   
{                                                                                                                                                                                                                  
    "employee": {                                                                                                                                                                                                  
        "first_name": "Mike",                                                                                                                                                                                      
        "id": "5",                                                                                                                                                                                                 
        "last_name": "Bleue"                                                                                                                                                                                       
    },                                                                                                                                                                                                             
    "nodeId": "seed"
}
```
 * Get an employee from cache using the seed node
 ```
~ http localhost:8000/employee/5                                                                                                                                                                          [19/52]
HTTP/1.1 200 OK
Content-Length: 79
Content-Type: application/json
Date: Thu, 14 Nov 2019 16:28:36 GMT
Server: akka-http/10.1.7

{
    "employee": {
        "first_name": "Mike",
        "id": "5",
        "last_name": "Bleue"
    },
    "nodeId": "seed"
}
```
 * Get an employee from cache using the node1
 ```
~ http localhost:8001/employee/5                                                                                                                                                                          HTTP/1.1 200 OK                                                                                                                                                                                              [2/52]
Content-Length: 80
Content-Type: application/json
Date: Thu, 14 Nov 2019 16:28:42 GMT
Server: akka-http/10.1.7

{
    "employee": {
        "first_name": "Mike",
        "id": "5",
        "last_name": "Bleue"
    },
    "nodeId": "node1"
}
```
