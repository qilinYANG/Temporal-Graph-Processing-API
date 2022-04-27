package org.apache.flink.statefun.playground.java.graphanalytics;

import org.apache.flink.statefun.playground.java.graphanalytics.types.*;
import org.apache.flink.statefun.sdk.java.*;
import org.apache.flink.statefun.sdk.java.io.KafkaEgressMessage;
import org.apache.flink.statefun.sdk.java.message.EgressMessageBuilder;
import org.apache.flink.statefun.sdk.java.message.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This function processes the query for counting the number of incoming edges of a vertex
 * In practice, there will be multiple logical instances of the InEdgesQueryFn, and the number of logical
 * instances will be equal to the number of vertices in the graph. Each logical instance will be identified by the
 * address (InEdgesQueryFn.TYPE_NAME, vertex_id). In this case, each logical instance only needs to store the incoming
 * edges for a specific vertex.
 * To send a query message to this function, please build a message with the IN_EDGES_QUERY_TYPE in {@link Types} and
 * send to the address described above, where the vertex_id is the vertex we want to query
 */
public class InEdgesQueryFn implements StatefulFunction {

  private static final ValueSpec<List<CustomTuple2<Integer, Long>>> IN_NEIGHBORS =
      ValueSpec.named("inNeighbors").withCustomType(Types.IN_NEIGHBORS_TYPE);
  private static ValueSpec<Integer> Add_Edge_Count = ValueSpec.named("addEdgeCount").withIntType();
  private static ValueSpec<Integer> Query_Count = ValueSpec.named("queryCount").withIntType();

  static final TypeName TYPE_NAME = TypeName.typeNameOf("graph-analytics.fns", "inEdges");
  static final StatefulFunctionSpec SPEC =
      StatefulFunctionSpec.builder(TYPE_NAME)
          .withSupplier(InEdgesQueryFn::new)
          .withValueSpecs(IN_NEIGHBORS, Add_Edge_Count, Query_Count)
          .build();

  static final TypeName EGRESS_TYPE = TypeName.typeNameOf("graph-analytics.io", "egress");

  @Override
  public CompletableFuture<Void> apply(Context context, Message message) throws Throwable {
    if (message.is(Types.Add_IN_EDGE_TYPE)) {
      Vertex vertex = message.as(Types.Add_IN_EDGE_TYPE);
      List<CustomTuple2<Integer, Long>> currentInNeighbors = getCurrentInNeighbors(context);
      updateInNeighbors(context, vertex, currentInNeighbors);
      // updateEdgeAddCount(context);
      long timeElapsed = System.currentTimeMillis() - vertex.getStart();
      // System.out.printf("vertex %s processed in-neighbor %s in %s ms\n", vertex.getDst(), vertex.getSrc(), timeElapsed);
      logInNeighbors(vertex.getDst(), context);
      // outputAddLatency(context, vertex.getDst(), timeElapsed);
    } else if (message.is(Types.IN_EDGES_QUERY_TYPE)) {
      InEdgesQuery query = message.as(Types.IN_EDGES_QUERY_TYPE);
      // updateQueryCount(context);
      long timeElapsed = System.currentTimeMillis() - query.getStart();
      // the query we are implementing now is simple; it is only asking for all the incoming edges, so we can
      // just return the entire IN_NEIGHBORS list
      outputResult(context, query.getVertexId());

    }
    return context.done();
  }

  /**
   * This method returns the current incoming neighbors of a vertex
   * @param context
   * @return IN_NEIGHBORS
   */
  public List<CustomTuple2<Integer, Long>> getCurrentInNeighbors(Context context) {
    return context.storage().get(IN_NEIGHBORS).orElse(new ArrayList<CustomTuple2<Integer, Long>>());
  }

  /**
   * This method update the IN_NEIGHBORS list by adding a new incoming neighbor to the list
   * while ensuring that all the neighbors in the list are sorted by timestamp value
   * @param context
   * @param vertex
   * @param currentInNeighbors
   */
  public void updateInNeighbors(Context context, Vertex vertex, List<CustomTuple2<Integer, Long>> currentInNeighbors) {
    CustomTuple2<Integer, Long> newInNeighbor = CustomTuple2.createTuple2(vertex.getSrc(), vertex.getTimestamp());
    // perform binary search to add incoming neighbor to the correct index, so that the IN_NEIGHBORS list remains
    // sorted by timestamp
    int left = 0, right = currentInNeighbors.size() - 1;
    int insertIdx = 0;
    while (left <= right) {
      int mid = left + (right-left)/2;
      Long t1 = currentInNeighbors.get(mid).getField(1);
      Long t2 = newInNeighbor.getField(1);
      int comparison = t1.compareTo(t2);
      if (comparison == 0) {
        insertIdx = mid;
        break;
      } else if (comparison < 0) {
        left = mid + 1;
        insertIdx = left;
      } else {
        right = mid - 1;
      }
    }
    currentInNeighbors.add(insertIdx, newInNeighbor);
    context.storage().set(IN_NEIGHBORS, currentInNeighbors);
  }

  /**
   * update the number of in-edge addition received for the current vertex
   *
   * @param context
   */
  private void updateEdgeAddCount(Context context) {
    int curCount = context.storage().get(Add_Edge_Count).orElse(0);
    context.storage().set(Add_Edge_Count, curCount + 1);
  }

  /**
   * update the number of in-edge query received for the current vertex
   *
   * @param context
   */
  private void updateQueryCount(Context context) {
    int curCount = context.storage().get(Query_Count).orElse(0);
    context.storage().set(Query_Count, curCount + 1);
  }

  private void outputAddLatency(Context context, int vertexId, long latency) {
    int edgeCount = context.storage().get(Add_Edge_Count).orElse(0);
    context.send(
        KafkaEgressMessage.forEgress(EGRESS_TYPE)
            .withTopic("add-edge-latency")
            .withUtf8Key(String.valueOf(vertexId))
            .withUtf8Value(String.format("latency after adding %d edges to vertex %d is %d\n", edgeCount, vertexId, latency))
            .build()
    );
  }

  /**
   * This method outputs query result to egress.
   * @param context
   * @param vertexId
   */
  private void outputResult(Context context, int vertexId) {
    List<CustomTuple2<Integer, Long>> currentInNeighbors =
        context.storage().get(IN_NEIGHBORS).orElse(Collections.emptyList());
    context.send(
        KafkaEgressMessage.forEgress(EGRESS_TYPE)
            .withTopic("incoming-edges")
            .withUtf8Key(String.valueOf(vertexId))
            .withUtf8Value(String.format("the incoming edges of vertex %d are %s\n", vertexId, currentInNeighbors))
            .build()
    );
  }

  /**
   * This methods prints out the current incoming edges/neighbors of a vertex
   * @param vertex
   * @param context
   */
  private void logInNeighbors(int vertex, Context context) {
    List<CustomTuple2<Integer, Long>> currentInNeighbors = context.storage().get(IN_NEIGHBORS).orElse(Collections.emptyList());

    System.out.printf("vertex %d currently has these incoming neighbors: %s\n", vertex, currentInNeighbors);
  }
}
