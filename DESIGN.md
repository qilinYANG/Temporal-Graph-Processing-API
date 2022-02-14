# 1

# 2

# 3) Expectations

The expected effect of the proposed solution is an unbounded/bounded temporal graph processing library that are able to do the following:
- Efficiently maintain state/snapshot of an ever-changing graph
- Able to update unbounded temporal graphs
- Able do temporal graph queries
In conclusion, our solution is a working API library that helps developers to process unbounded temporal graph efficiently by utilizing Flink stateful functions.
The alternative approaches we are considering is implementing library after analyzing other temporal graph processing libraries and focus on one problem/flaw of those libraries and improve upon it.

# 4

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
# 6
