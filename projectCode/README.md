#### Project 6: Temporal Graph Queries and Analysis
This is the source code for project 6.

#### Installation and instructions to run
* from the root directory of the source code, run `cd projectCode` to go into the actual source directory (if you are already inside the projectCode directory, you can skip this step)
* run `make` to build and run the stateful functions
* open Docker Desktop and click `graph-analytics` to see messages being sent and received

#### Running Queries
Currently, the queries have to be sent through http requests manually. We will provide easier ways to run queries in the future.
To retrieve the number of incoming edges of a vertex, send a request like this:
```bash
curl -X PUT -H "Content-Type: application/vnd.graph-analytics.types/execute" -d '{"task": "GET_IN_EDGES", "src": 2, "dst": 3, "t": 12344}' localhost:8090/graph-analytics.fns/filter/1
```
In the above query, the `src` field will be ignored, and the `dst` field is the vertex the query will be performed on.

To retrieve the number of outgoing edges of a vertex, send a request like this:
```bash
curl -X PUT -H "Content-Type: application/vnd.graph-analytics.types/execute" -d '{"task": "GET_OUT_EDGES", "src": 2, "dst": 3, "t": 12344}' localhost:8090/graph-analytics.fns/filter/1
```
In the above query, the `dst` field will be ignored, and the `src` field is the vertex the query will be performed on.

__Note__: the timestamp field `t` in both queries has no effect on query results now because we currently do not support time-based queries. We will support it in the future.

Finally, to get the query result from egress, use the following command:
```bash
curl -X GET localhost:8091/<query topic>
```
The query topic can be `incoming-edges` or `outgoing-edges` depending on what query results you are looking for.


#### Project Structure and file explanations:
* `src/` contains all the source code, and `src/.../types` contains the types we use (eg. `CustomTuple2` and `Vertex` classes)
* `GraphAnalyticsFilesApp.java` contains code to read from an ingress file rather than requiring us to manually input `CURL` commands from the terminal
* `src/.../GraphAnalyticsAppServer.java`: contains the `Undertow` server that listens for requests
* `src/.../InEdgesQueryFn.java`: contains the query code for counting incoming edges
* `src/.../OutEdgesQueryFn.java`: contains the query code for counting outgoing edges
* `src/.../EventsFilterFn`: contains the code of our main event handler function

#### Apache Kafka Broker
The Kafka is set up according to this [guide](https://developer.confluent.io/quickstart/kafka-docker/), which is set up through `docker-compose`; therefore, by running `docker-compose`, it will automatically set up the broker. After `docker-compose up -d`, topics have to be created since at the moment, automatic topics creation during start up is not set up yet. Run the follow commands to manually create topics:
```
docker exec broker \
kafka-topics --bootstrap-server broker:9092 \
             --create \
             --topic quickstart
```

**To write to Kafka:** <br>
To write message to Kafka topic, we'll done it through kafka-console-producer command line tool. This is good for testing it out but during simulation, we'll be using Kafka Connect/Producer API to read textfiles to send graph edges to Flink Application. For sending single message, we can write the following in the terminal:
```
docker exec --interactive --tty broker \
kafka-console-producer --bootstrap-server broker:9092 \
                       --topic quickstart
```
Then you can write messages:
```
this is my first kafka message
hello world!
this is my third kafka message. I’m on a roll :-D
```

**To read messages from Kafka on terminal:**<br>
To read messages from a "quickstart" topic:
```
docker exec --interactive --tty broker \
kafka-console-consumer --bootstrap-server broker:9092 \
                       --topic quickstart \
                       --from-beginning
```

**To list the topics in Kafka:** <br>
```
docker exec broker \
kafka-topics --bootstrap-server broker:9092 \
             --list
```
