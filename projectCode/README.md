# Project 6: Temporal Graph Queries and Analysis
This is the source code for project 6.

# Project Structure and file explanations:
* `src/` contains all the source code, and `src/.../types` contains the types we use (eg. `CustomTuple2` and `Vertex` classes)
* `GraphAnalyticsFilesApp.java` contains code to read from an ingress file rather than requiring us to manually input `CURL` commands from the terminal
* `src/.../GraphAnalyticsAppServer.java`: contains the `Undertow` server that listens for requests
* `src/.../InEdgbroker:esQueryFn.java`: contains the query code for counting incoming edges
* `src/.../OutEdgesQueryFn.java`: contains the query code for counting outgoing edges
* `src/.../EventsFilterFn`: contains the code of our main event handler function

# Build project
* from the root directory of the source code, run `cd projectCode` to go into the actual source directory (if you are already inside the projectCode directory, you can skip this step)
* run `make` to build and run the stateful functions
* open Docker Desktop and click `graph-analytics` to see messages being sent and received

# Run project
## Running Queries with HTTP Requests
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

## Running Queries through Apache Kafka Broker
The Kafka is set up according to this [guide](https://developer.confluent.io/quickstart/kafka-docker/), which is set up through `docker-compose`; therefore, by running `docker-compose`, it will automatically set up the broker. After `docker-compose up -d`, topics have to be created since at the moment, automatic topics creation during start up is not set up yet. Run the follow commands to manually create topics:
```
docker exec broker \
kafka-topics --bootstrap-server broker:29092 \
             --create \
             --topic tasks
```
If you have kafka installed on your device, then you can enter this instead:
```
kafka-topics --bootstrap-server localhost:9092 \
             --create \
             --topic tasks
```
<br>

**To write to Kafka:** <br>
To write message to Kafka topic, we'll done it through kafka-console-producer command line tool. This is good for testing it out but during simulation, we'll be using Kafka Connect/Producer API to read textfiles to send graph edges to Flink Application. For sending single message, we can write the following in the terminal:
```
docker exec --interactive --tty broker \
kafka-console-producer --bootstrap-server broker:29092 \
                       --topic tasks \
                       --property parse.key=true \
                       --property key.separator="|"
```
If you have kafka installed on your device, then you can enter this instead:
```
kafka-console-producer --bootstrap-server localhost:9092 \
                       --topic tasks \
                       --property parse.key=true \
                       --property key.separator="|"
```
Then you can write messages:
```
3|{"task": "ADD", "src": "3", "dst": "4", "t": "1254194656", "k": "0"}
1|{"task": "ADD", "src": "1", "dst": "4", "t": "1254192988", "k": "0"}
```
<br>

Recommendation Query 

```
{"task": "recommendation", "src": "1", "dst": "2", "t": "1254194656", "k": "0"}
```

**To read messages from Kafka on terminal:**<br>
To read messages from a "quickstart" topic:
```
docker exec --interactive --tty broker \
kafka-console-consumer --bootstrap-server broker:29092 \
                       --topic tasks \
                       --from-beginning
```

If you have kafka installed on your device, then you can enter this instead:
```
kafka-console-consumer --bootstrap-server localhost:9092 \
                       --topic tasks \
                       --from-beginning
```
<br>

**To list the topics in Kafka:** <br>
```
docker exec broker \
kafka-topics --bootstrap-server broker:29092 \
             --list
```
If you have kafka installed on your device, then you can enter this instead:
```
kafka-topics --bootstrap-server localhost:9092 \
             --list
```

**To request for metadata from the broker inside the docker:**
```
docker run -it --rm --network=projectcode_default edenhill/kcat:1.7.1 -b broker:29092 -L
```