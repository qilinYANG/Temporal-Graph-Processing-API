#### Project 6: Temporal Graph Queries and Analysis
This is the source code for project 6.

#### Installation and instructions to run
* from the root directory of the source code, run `cd projectCode` to go into the actual source directory (if you are already inside the projectCode directory, you can skip this step)
* run `make` to build and run the stateful functions
* to test the automatic ingress feature, open a new terminal window and run `make ingress` to compile and run the feature
* type `sx-mathoverflow-test.txt` to automatically read and import the test ingress events from the `../data` folder , note the changes in the terminal on the Flink app
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
