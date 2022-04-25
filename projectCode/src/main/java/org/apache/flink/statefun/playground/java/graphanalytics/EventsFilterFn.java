package org.apache.flink.statefun.playground.java.graphanalytics;

import org.apache.flink.statefun.playground.java.graphanalytics.types.*;
import org.apache.flink.statefun.sdk.java.Context;
import org.apache.flink.statefun.sdk.java.StatefulFunction;
import org.apache.flink.statefun.sdk.java.StatefulFunctionSpec;
import org.apache.flink.statefun.sdk.java.TypeName;
import org.apache.flink.statefun.sdk.java.message.Message;
import org.apache.flink.statefun.sdk.java.message.MessageBuilder;

import java.util.concurrent.CompletableFuture;


/**
 * A function for handling incoming requests.
 * All requests are routed via the vertex route.
 * Different stateful function are dispatched depending on the task within the execute method.
 */
final class EventsFilterFn implements StatefulFunction {
  static final TypeName TYPE_NAME = TypeName.typeNameOf("graph-analytics.fns", "filter");
  static final StatefulFunctionSpec SPEC =
      StatefulFunctionSpec.builder(TYPE_NAME)
          .withSupplier(EventsFilterFn::new)
          .build();


  @Override
  public CompletableFuture<Void> apply(Context context, Message message) {
    if (message.is(Types.EXECUTE_TYPE)) {
      System.out.println("Received Request");
      final Execute request = message.as(Types.EXECUTE_TYPE);
      // record the time the system started processing current event
      long start = System.currentTimeMillis();

      if (request.getTask().equals("ADD")) {
        System.out.println("Adding Vertex");
        Vertex v = new Vertex(
                request.getSrc(),
                request.getDst(),
                request.getTimestamp(),
                start
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
        InEdgesQuery inQuery = InEdgesQuery.create(request.getDst(), request.getTimestamp(), start);

        context.send(
                MessageBuilder.forAddress(InEdgesQueryFn.TYPE_NAME, String.valueOf(inQuery.getVertexId()))
                        .withCustomType(Types.IN_EDGES_QUERY_TYPE, inQuery)
                        .build()
        );
      } else if (request.getTask().equals("GET_OUT_EDGES")) {
        System.out.println("Fetching OUT Edges");
        OutEdgesQuery outQuery = OutEdgesQuery.create(request.getSrc(), request.getTimestamp(), start);

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
