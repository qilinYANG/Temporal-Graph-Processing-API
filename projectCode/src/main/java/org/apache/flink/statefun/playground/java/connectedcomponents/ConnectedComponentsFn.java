package org.apache.flink.statefun.playground.java.connectedcomponents;

import static org.apache.flink.statefun.playground.java.connectedcomponents.types.Types.EGRESS_RECORD_JSON_TYPE;

import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.apache.flink.statefun.playground.java.connectedcomponents.types.EgressRecord;
import org.apache.flink.statefun.playground.java.connectedcomponents.types.Types;
import org.apache.flink.statefun.playground.java.connectedcomponents.types.Vertex;
import org.apache.flink.statefun.sdk.java.Context;
import org.apache.flink.statefun.sdk.java.StatefulFunction;
import org.apache.flink.statefun.sdk.java.StatefulFunctionSpec;
import org.apache.flink.statefun.sdk.java.TypeName;
import org.apache.flink.statefun.sdk.java.ValueSpec;
import org.apache.flink.statefun.sdk.java.message.EgressMessageBuilder;
import org.apache.flink.statefun.sdk.java.message.Message;
import org.apache.flink.statefun.sdk.java.message.MessageBuilder;
import org.apache.flink.api.java.tuple.Tuple2;

/**
 * A stateful function that computes the connected component for a stream of vertices.
 *
 * <p>The underlying algorithm is a form of label propagation and works by recording for every
 * vertex its component id. Whenever a vertex is created or its component id changes, it will send
 * this update to all of its neighbours. Every neighbour will compare the broadcast component id
 * with its own id. If the id is lower than its own, then it will accept this component id and
 * broadcast this change to its neighbours. If the own component id is smaller, then it answers to
 * the broadcaster by sending its own component id.
 *
 * <p>That way, the minimum component id of each connected component will be broadcast throughout
 * the whole connected component. Eventually, every vertex will have heard of the minimum component
 * id and have accepted it.
 *
 * <p>Every component id change will be output to the {@link #PLAYGROUND_EGRESS} as a connected
 * component change.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Label_propagation_algorithm">Label propagation
 *     algorithm</a>
 */
final class ConnectedComponentsFn implements StatefulFunction {

  /** {vertex: [<neighbor, timestamp>, ...]} */
  private static final ValueSpec<Map<Integer, List<Tuple2<Vertex, Long>>>> VERTEX_MAP =
      ValueSpec.named("vertexMap").withCustomType(Types.VERTEXMAP_TYPE);

  static final TypeName TYPE_NAME = TypeName.typeNameOf("connected-components.fns", "vertex");
  static final StatefulFunctionSpec SPEC =
      StatefulFunctionSpec.builder(TYPE_NAME)
          .withSupplier(ConnectedComponentsFn::new)
          .withValueSpecs(VERTEX_MAP)
          .build();

  static final TypeName PLAYGROUND_EGRESS = TypeName.typeNameOf("io.statefun.playground", "egress");

  @Override
  public CompletableFuture<Void> apply(Context context, Message message) {
    // initialize a new vertex
    if (message.is(Types.VERTEX_INIT_TYPE)) {
      final Vertex vertex = message.as(Types.VERTEX_INIT_TYPE);
      String inputTimestamp = Instant.ofEpochMilli(vertex.getTimestamp()).atZone(ZoneId.of("America/New_York")).toString();
      System.out.println("Received: " + vertex.getSrc() + "->" + vertex.getDst() + " at t=" + vertex.getTimestamp() + " (" + inputTimestamp + ")");

      sendIncomingEdge(context, vertex);
      outputResult(
          context, vertex, inputTimestamp);
    }

    return context.done();
  }

  private Map<Integer, List<Tuple2<Vertex, Long>>> getCurrentVertexMap(Context context) {
    return context.storage().get(VERTEX_MAP).orElse(new HashMap<Integer, List<Tuple2<Vertex, Long>>>());
  }

  /**
   * Send a message to InEdgesQueryFn so that InEdgesQueryFn can keep track of the incoming
   * edges of a vertex.
   * @param context
   * @param v
   */
  private void sendIncomingEdge(Context context, Vertex v) {
    context.send(
        MessageBuilder.forAddress(InEdgesQueryFn.TYPE_NAME, String.valueOf(v.getDst()))
            .withCustomType(Types.Add_IN_EDGE_TYPE, v)
            .build()
    );
  }

  private void outputResult(Context context, Vertex v, String timestamp) {
    context.send(
        EgressMessageBuilder.forEgress(PLAYGROUND_EGRESS)
            .withCustomType(
                EGRESS_RECORD_JSON_TYPE,
                new EgressRecord(
                    "connected-component-changes",
                    String.format("\nVertex %s->%s has the following timestamp: %s (%s)\n", v.getSrc(), v.getDst(), v.getTimestamp(), timestamp)))
            .build());
  }
}
