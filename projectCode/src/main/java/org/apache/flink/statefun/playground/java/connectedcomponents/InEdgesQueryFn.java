package org.apache.flink.statefun.playground.java.connectedcomponents;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.statefun.playground.java.connectedcomponents.types.Types;
import org.apache.flink.statefun.sdk.java.*;
import org.apache.flink.statefun.sdk.java.message.Message;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class InEdgesQueryFn implements StatefulFunction {

  private static final ValueSpec<List<Tuple2<Integer, Long>>> IN_NEIGHBORS =
      ValueSpec.named("inNeighbors").withCustomType(Types.IN_NEIGHBORS_TYPE);

  static final TypeName TYPE_NAME = TypeName.typeNameOf("connected-components.fns", "inEdges");
  static final StatefulFunctionSpec SPEC =
      StatefulFunctionSpec.builder(TYPE_NAME)
          .withSupplier(InEdgesQueryFn::new)
          .withValueSpecs()
          .build();

  @Override
  public CompletableFuture<Void> apply(Context context, Message message) throws Throwable {
    return null;
  }
}
