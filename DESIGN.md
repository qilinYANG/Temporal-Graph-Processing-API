# 1

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
- Efficiently maintain state/snapshot of an ever-changing graph
- Able to update unbounded temporal graphs
- Able do temporal graph queries
In conclusion, our solution is a working API library that helps developers to process unbounded temporal graph efficiently by utilizing Flink stateful functions.
The alternative approaches we are considering is implementing library after analyzing other temporal graph processing libraries and focus on one problem/flaw of those libraries and improve upon it.

# 4

# 5

# 6
