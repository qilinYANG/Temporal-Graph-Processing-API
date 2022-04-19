package org.apache.flink.statefun.playground.java.graphanalytics;

import org.apache.flink.statefun.playground.java.graphanalytics.types.*;
import org.apache.flink.statefun.sdk.java.*;
import org.apache.flink.statefun.sdk.java.message.EgressMessageBuilder;
import org.apache.flink.statefun.sdk.java.message.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TwoHopQueryFn implements StatefulFunction {
    private InEdgesQueryFn inEdgesQueryFn = new InEdgesQueryFn();
    private OutEdgesQueryFn outEdgesQueryFn = new OutEdgesQueryFn();

    private static final ValueSpec<List<CustomTuple2<Integer, Long>>> TWOHOP_NEIGHBORS =
            ValueSpec.named("twohopNeighbors").withCustomType(Types.TwoHop_NEIGHBORS_TYPE);

    static final TypeName TYPE_NAME = TypeName.typeNameOf("graph-analytics.fns", "inEdges");
    static final StatefulFunctionSpec SPEC =
            StatefulFunctionSpec.builder(TYPE_NAME)
                    .withSupplier(InEdgesQueryFn::new)
                    .withValueSpecs(TWOHOP_NEIGHBORS)
                    .build();

    static final TypeName EGRESS_TYPE = TypeName.typeNameOf("io.statefun.playground", "egress");

    @Override
    public CompletableFuture<Void> apply(Context context, Message message) throws Throwable {
        if (message.is(Types.VERTEX_INIT_TYPE)) {
            Vertex vertex = message.as(Types.VERTEX_INIT_TYPE);
            List<CustomTuple2<Integer,Long>> currentTwoHopNeighbors = context.storage().get(TWOHOP_NEIGHBORS).orElse(new ArrayList<CustomTuple2<Integer, Long>>());
            updateTwoHopNeighbors(context,vertex,currentTwoHopNeighbors);
        } else if (message.is(Types.Two_Hop_QUERY_TYPE)){
            TwoHopQuery hopQuery = message.as(Types.Two_Hop_QUERY_TYPE);
            outputResult(context,hopQuery.getVertexId());
        }

        return context.done();
    }


    public void updateTwoHopNeighbors(Context context, Vertex vertex, List<CustomTuple2<Integer, Long>> currentTwoHopNeighbors) {
        List<CustomTuple2<Integer, Long>> currentInNeighbors = inEdgesQueryFn.getCurrentInNeighbors(context);
        inEdgesQueryFn.updateInNeighbors(context, vertex, currentInNeighbors);
        List<CustomTuple2<Integer,Long>> currentOutNeighbors = outEdgesQueryFn.getCurrentOutNeighbors(context);


        for (CustomTuple2<Integer, Long> each : currentInNeighbors){
            Integer src = each.getField(0);
            Long tsp = each.getField(1);
            if (src != vertex.getSrc()){
                Vertex v = new Vertex(src,0,tsp);
                outEdgesQueryFn.updateOutNeighbors(context,v,currentOutNeighbors);
                currentTwoHopNeighbors.addAll(currentOutNeighbors);
                context.storage().set(TWOHOP_NEIGHBORS,currentTwoHopNeighbors);
            }


        }

    }
    private void outputResult(Context context, int vertexId) {
        List<CustomTuple2<Integer, Long>> TwoHopNeighbors =
                context.storage().get(TWOHOP_NEIGHBORS).orElse(Collections.emptyList());

        context.send(
                EgressMessageBuilder.forEgress(EGRESS_TYPE)
                        .withCustomType(Types.EGRESS_RECORD_JSON_TYPE,
                                new EgressRecord("TwoHop-Recommendation",
                                        String.format("Recommended node connection for vertex %s are %s", vertexId, TwoHopNeighbors)))
                        .build()
        );
    }
}
