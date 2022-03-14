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
import org.apache.flink.statefun.playground.java.connectedcomponents.types.Execute;
import org.apache.flink.statefun.playground.java.connectedcomponents.types.InEdgesQuery;
import org.apache.flink.statefun.playground.java.connectedcomponents.types.OutEdgesQuery;
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
 * A function for handling incoming requests 
 */
final class ConnectedComponentsFn implements StatefulFunction {
  static final TypeName TYPE_NAME = TypeName.typeNameOf("connected-components.fns", "vertex");
  static final StatefulFunctionSpec SPEC =
      StatefulFunctionSpec.builder(TYPE_NAME)
          .withSupplier(ConnectedComponentsFn::new)
          //.withValueSpecs(VERTEX_MAP)
          .build();

  static final TypeName PLAYGROUND_EGRESS = TypeName.typeNameOf("io.statefun.playground", "egress");

  @Override
  public CompletableFuture<Void> apply(Context context, Message message) {
    if (message.is(Types.EXECUTE_TYPE)) {
      System.out.println("Received Request");
      final Execute request = message.as(Types.EXECUTE_TYPE);

      if (request.getTask().equals("ADD")) {
        System.out.println("Adding Vertex");

        Vertex v = new Vertex(
                request.getSrc(),
                request.getDst(),
                request.getTimestamp()
        );

        context.send(
                MessageBuilder.forAddress(InEdgesQueryFn.TYPE_NAME, String.valueOf(v.getDst()))
                        .withCustomType(Types.Add_IN_EDGE_TYPE, v)
                        .build()
        );
        context.send(
                MessageBuilder.forAddress(OutEdgesQueryFn.TYPE_NAME, String.valueOf(v.getSrc()))
                        .withCustomType(Types.Add_OUT_EDGE_TYPE, v)
                        .build()
        );
      } else if (request.getTask().equals("GET_IN_EDGES")) {
        System.out.println("Fetching IN Edges");
        InEdgesQuery inQuery = InEdgesQuery.create(request.getDst(), request.getTimestamp());

        context.send(
                MessageBuilder.forAddress(InEdgesQueryFn.TYPE_NAME, String.valueOf(inQuery.getVertexId()))
                        .withCustomType(Types.IN_EDGES_QUERY_TYPE, inQuery)
                        .build()
        );
      } else if (request.getTask().equals("GET_OUT_EDGES")) {
        System.out.println("Fetching OUT Edges");
        OutEdgesQuery outQuery = OutEdgesQuery.create(request.getSrc(), request.getTimestamp());

        context.send(
                MessageBuilder.forAddress(OutEdgesQueryFn.TYPE_NAME, String.valueOf(outQuery.getVertexId()))
                        .withCustomType(Types.OUT_EDGES_QUERY_TYPE, outQuery)
                        .build()
        );
      } else {
        System.out.println("Unknown Query Type");
      }
    }

    return context.done();
  }
}
