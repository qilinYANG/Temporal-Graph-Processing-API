# 1) Problem Statement
**There are a few problems we are trying to solve**:

* The ability to efficiently run queries(e.g number of friends a user has that are also friends with each other) and updates (edge additions and deletions) upon an evolving graph (eg. social graph, where new friends are constantly being added)
* Adding support for cross-graph analysis through the creation of this suite of functions that can serve as an API for evolving graphs
    * This is an important problem because graphs are extremely relevant in our increasingly socially connected world, and having this API would allow us to deduce relationships between these social networks easily and extract useful information to perform tasks like content recommendations
* This problem will benefit anybody who maintains large graphs where data is ever-changing and analytics on specific instances of the graph is required

# 2) Solution
**Proposed Solution.**
_What is the basic approach, method, idea or tool that’s being suggested to solve the problem? Try to be as specific as possible and mention concrete tools / programming languages / frameworks. Try to reason why these tools are suitable to solve the problem at hand._

**Tech stacks (subject to change)**<br />
- Use Apache Flink Stateful Function to handle queries and graph operations (Java)<br />
- Docker (decide later if we need to use Docker)<br />
- Kafka for event ingress and egress (decide later)<br />

**Basic approach**<br />
- We will implement a statefun application consisting of several stateful functions that perform specific tasks (graph updates or temporal queries) based on incoming events.<br />
**For first half of the project, we’ll focus on:<br />**
- data ingestion (include graph updates)<br />
- Architecture<br />
**For the second half of the project, we’ll focus on the temporal queries.<br />**
# 3) Expectations

The expected effect of the proposed solution is an unbounded/bounded temporal graph processing library that are able to do the following:

1. Able to update bounded/unbounded directed, temporal graphs
 - Add new node to the graph
 - Modifying graph structure after adding the new node
 - Search for specific node on the graph (eg. Did user u comment on user v’s question)
2. Able to query bounded/unbounded directed, temporal graphs. For example:
 - How many outgoing edges does node v have?
 - How many incoming edges does node v have?
 - How many incoming edges does node v have within a specific time period?
 - At time t, how many nodes have x edges?
 - At time t, what is the maximum number of edges a node has?
3. Able to give edges attribute (an abstract data type for edge)
4. Support graph algorithms (bfs, dfs, shortest path, topological sorting) to be used for graph queries


# 4) Experimental Plan

Series of steps required to evaluate the correctness and efficacy of the proposed design. Including, but not limited to:
- Using Docker for environment simulations and setup.
- Building an array of unit tests for testing each module under different circumstances/workloads, and comparing actual and predicted results after running them through the API.
- Datasets: Yelp/Snap provided in the project description (starting with the mathoverflow dataset).
- Measuring the performance difference between embedded functions and remote functions.

# 5) Success Indicators
__Outcome__:
The final outcome of this project is an usable API library built on top of Flink Stateful functions that is able to effieicntly process temporal graphs, perform graph updates and modifications, and also run specific graph algorithms to produce analytical results based on user queries.
__Success Measurement__:
Our primary goal is to implement a working API library. We will also perform unit test on each function to ensure correctness and run simulations if possible. We also plan to compare performance difference among different stateful function architectures(e.g. embedded and remote functions).
__Milestones__:
1. Overall Architecture design (what type of I/O components to use and the overall dataflow within our statefun application) => will produce an architecture diagram
2. Complete skeleton code(abstractions/interface/customized data types). Implement the architecture(write configuration in module.yaml and simple web server for serving the fucntions)
3. Create unit tests to measure the correctness/performance of our statefun application (__Important__: leave comments on what the function does and its parameters)
4. Complete the logic for converting raw data into appropriate temporal graph representation (using states in individual functions to create an abstraction of a graph)
5. Complete stateful functions that perform basic graph update operations (e.g. edge addition/deletion)
6. Complete functions that handle queries and functions that perform advanced operations on temporal graph based on the queries.
7. Run experiments/tests and gather performance data
8. Make adjustments/experiments based on experiments conducted before

# 6) Tasks
Tasks:
1. API Architecture Part 1 (first week)
  - Configuration files (module.yaml) for specifying API endpoints and ingress/egress
  - Diagram of the whole architecture (tools & modules) (By Thur 2.17.22) [Aaron]
  - Docker image (set up the environment) [Ardarsh]
2. API Architecture Part 2 (second week)
  - Interfacing our functions to simplify development (graph update, graph query, and data transformation functions) [Divide tasks]
  - Graph representation (how to represent and store the temporal graphs) [Do together]

All of the tasks above are required to be done before starting the rest:
3. Data ingestion Function (2): (by March 15th)
  - Read data from file and process it in Flink [Qilin]
4. Graph updates functions (2)
  - Addition [Touch]
  - Delete [Mike]
  - Modification [Ardarsh]
5. Graph queries functions (3) [Aaron]
  - Dataset: https://snap.stanford.edu/data/sx-mathoverflow.html

