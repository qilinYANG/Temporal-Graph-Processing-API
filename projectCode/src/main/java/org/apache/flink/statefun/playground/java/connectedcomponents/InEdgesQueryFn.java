package org.apache.flink.statefun.playground.java.connectedcomponents;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.statefun.playground.java.connectedcomponents.types.EgressRecord;
import org.apache.flink.statefun.playground.java.connectedcomponents.types.InEdgesQuery;
import org.apache.flink.statefun.playground.java.connectedcomponents.types.Types;
import org.apache.flink.statefun.playground.java.connectedcomponents.types.Vertex;
import org.apache.flink.statefun.sdk.java.*;
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

  private static final ValueSpec<List<Tuple2<Integer, Long>>> IN_NEIGHBORS =
      ValueSpec.named("inNeighbors").withCustomType(Types.IN_NEIGHBORS_TYPE);

  static final TypeName TYPE_NAME = TypeName.typeNameOf("connected-components.fns", "inEdges");
  static final StatefulFunctionSpec SPEC =
      StatefulFunctionSpec.builder(TYPE_NAME)
          .withSupplier(InEdgesQueryFn::new)
          .withValueSpecs()
          .build();

  static final TypeName EGRESS_TYPE = TypeName.typeNameOf("io.statefun.playground", "egress");

  @Override
  public CompletableFuture<Void> apply(Context context, Message message) throws Throwable {
    if (message.is(Types.Add_IN_EDGE_TYPE)) {
      Vertex vertex = message.as(Types.Add_IN_EDGE_TYPE);
      List<Tuple2<Integer, Long>> currentInNeighbors = getCurrentInNeighbors(context);
      updateInNeighbors(context, vertex, currentInNeighbors);
    } else if (message.is(Types.IN_EDGES_QUERY_TYPE)) {
      InEdgesQuery query = message.as(Types.IN_EDGES_QUERY_TYPE);
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
  private List<Tuple2<Integer, Long>> getCurrentInNeighbors(Context context) {
    return context.storage().get(IN_NEIGHBORS).orElse(new ArrayList<Tuple2<Integer, Long>>());
  }

  /**
   * This method update the IN_NEIGHBORS list by adding a new incoming neighbor to the list
   * while ensuring that all the neighbors in the list are sorted by timestamp value
   * @param context
   * @param vertex
   * @param currentInNeighbors
   */
  private void updateInNeighbors(Context context, Vertex vertex, List<Tuple2<Integer, Long>> currentInNeighbors) {
    Tuple2<Integer, Long> newInNeighbor = new Tuple2<>(vertex.getSrc(), vertex.getTimestamp());
    // perform binary search to add incoming neighbor to the correct index, so that the IN_NEIGHBORS list remains
    // sorted by timestamp
    int left = 0, right = currentInNeighbors.size();
    int insertIdx = -1;
    while (left <= right) {
      int mid = left + (right-left)/2;
      Long t1 = currentInNeighbors.get(mid).getField(1);
      Long t2 = newInNeighbor.getField(1);
      int comparison = t1.compareTo(t2);
      if (comparison == 0) {
        insertIdx = mid;
        break;
      } else if (comparison < 0){
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
   * This method outputs query result to egress.
   * @param context
   * @param vertexId
   */
  private void outputResult(Context context, int vertexId) {
    List<Tuple2<Integer, Long>> currentInNeighbors =
        context.storage().get(IN_NEIGHBORS).orElse(Collections.emptyList());

    context.send(
        EgressMessageBuilder.forEgress(EGRESS_TYPE)
            .withCustomType(Types.EGRESS_RECORD_JSON_TYPE,
                new EgressRecord("incoming-edges",
                    String.format("The incoming edges of vertex %s are %s", vertexId, currentInNeighbors)))
            .build()
    );
  }
}
