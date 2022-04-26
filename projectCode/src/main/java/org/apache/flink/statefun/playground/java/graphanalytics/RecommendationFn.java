package org.apache.flink.statefun.playground.java.graphanalytics;

import org.apache.flink.statefun.playground.java.graphanalytics.types.*;
import org.apache.flink.statefun.sdk.java.*;
import org.apache.flink.statefun.sdk.java.io.KafkaEgressMessage;
import org.apache.flink.statefun.sdk.java.message.EgressMessageBuilder;
import org.apache.flink.statefun.sdk.java.message.Message;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class RecommendationFn implements StatefulFunction {




  private static final ValueSpec<Set<Integer>> RECOMMEND_SET =
    ValueSpec.named("recommendSet").withCustomType(Types.RECOMMEND_SET_TYPE);

  static final TypeName TYPE_NAME = TypeName.typeNameOf("graph-analytics.fns", "twoHopEdges");

  static final StatefulFunctionSpec SPEC =
      StatefulFunctionSpec.builder(TYPE_NAME)
          .withSupplier(RecommendationFn::new)
          .withValueSpecs(RECOMMEND_SET)
          .build();


    static final TypeName EGRESS_TYPE = TypeName.typeNameOf("io.statefun.playground", "egress");


  @Override
  public CompletableFuture<Void> apply(Context context, Message message) throws Throwable {
    if (message.is(Types.RECOMMEND_QUERY_TYPE)){
      RecommendQuery query = message.as(Types.RECOMMEND_QUERY_TYPE);
      // since we've already stored a set of possible recommendation candidate, we can directly output the result
      outputResult(context, query.getVertexId());
    } else if (message.isInt()) {
      // the OutEdgesQuery function has forwarded a potential recommendation candidate to the recommendation function
      int candidate = message.asInt();
      if (candidate != -1) {
        updateRecommendSet(context, candidate);
      }
    }
    return context.done();
  }

  public Set<Integer> getRecommendationSet(Context context) {
    return context.storage().get(RECOMMEND_SET).orElse(Collections.emptySet());
  }

  public void updateRecommendSet(Context context, int candidate) {
    Set<Integer> curRecommendSet = getRecommendationSet(context);
    // check if candidate is already in the recommendation set
    if (!curRecommendSet.contains(candidate)) {
      curRecommendSet.add(candidate);
      context.storage().set(RECOMMEND_SET, curRecommendSet);
    }
  }


  private void outputResult(Context context, int vertexId) {
    Set<Integer> recommendSet = getRecommendationSet(context);

    context.send(

            EgressMessageBuilder.forEgress(EGRESS_TYPE)
                    .withCustomType(Types.EGRESS_RECORD_JSON_TYPE,
                            new EgressRecord("TwoHop-Recommendation",
                                    String.format("Recommenddation for vertex %s are %s", vertexId, recommendSet)))
                    .build()
    );
  }
}
