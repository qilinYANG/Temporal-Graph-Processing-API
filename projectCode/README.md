#### Project 6: Temporal Graph Queries and Analysis
This is the source code for project 6.

#### Installation and instructions to run
* from the root directory of the source code, run `cd projectCode` to go into the actual source directory
* run `make` to build and run the stateful functions
* to test the automatic ingress feature, open a new terminal window and run `make ingress` to compile and run the feature
* type `sx-mathoverflow-test.txt` to automatically read and import the test ingress events from the `../data` folder , note the changes in the terminal on the Flink app

#### Project Structure and file explanations:
* `src/` contains all the source code, and `src/.../types` contains the types we use (eg. `CustomTuple2` and `Vertex` classes)
* `ConnectedComponentFilesApp.java` contains code to read from an ingress file rather than requiring us to manually input `CURL` commands from the terminal
* `src/.../ConnectedComponentsAppServer.java`: contains the `Undertow` server that listens for requests
* `src/.../InEdgesQueryFn.java`: contains the query code for counting incoming edges
* `src/.../OutEdgesQueryFn.java`: contains the query code for counting outgoing edges