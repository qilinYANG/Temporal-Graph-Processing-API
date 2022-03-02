package org.apache.flink.statefun.playground.java.connectedcomponents.types;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.apache.flink.statefun.sdk.java.TypeName;
import org.apache.flink.statefun.sdk.java.types.SimpleType;
import org.apache.flink.statefun.sdk.java.types.Type;
import org.apache.flink.api.java.tuple.Tuple2;

public final class Types {

  private Types() {}

  private static final ObjectMapper JSON_OBJ_MAPPER = new ObjectMapper();
  private static final String TYPES_NAMESPACE = "connected-components.types";

  /** Type denoting a new vertex coming from the input source. */
  public static final Type<Vertex> VERTEX_INIT_TYPE =
      SimpleType.simpleImmutableTypeFrom(
          TypeName.typeNameOf(TYPES_NAMESPACE, "vertex"),
          JSON_OBJ_MAPPER::writeValueAsBytes,
          bytes -> JSON_OBJ_MAPPER.readValue(bytes, Vertex.class));

//   @SuppressWarnings("unchecked")
//   public static final Type<Set<Integer>> NEIGHBOURS_TYPE =
//       SimpleType.simpleImmutableTypeFrom(
//           TypeName.typeNameOf(TYPES_NAMESPACE, "neighbours"),
//           JSON_OBJ_MAPPER::writeValueAsBytes,
//           bytes -> JSON_OBJ_MAPPER.readValue(bytes, Set.class));

@SuppressWarnings("unchecked")
public static final Type<Map<Integer, List<Tuple2<Vertex, Long>>>> VERTEXMAP_TYPE =
    SimpleType.simpleImmutableTypeFrom(
        TypeName.typeNameOf(TYPES_NAMESPACE, "vertexMap"),
        JSON_OBJ_MAPPER::writeValueAsBytes,
        bytes -> JSON_OBJ_MAPPER.readValue(bytes, Map.class));

  public static final Type<EgressRecord> EGRESS_RECORD_JSON_TYPE =
      SimpleType.simpleImmutableTypeFrom(
          TypeName.typeNameOf("io.statefun.playground", "EgressRecord"),
          JSON_OBJ_MAPPER::writeValueAsBytes,
          bytes -> JSON_OBJ_MAPPER.readValue(bytes, EgressRecord.class));
}
