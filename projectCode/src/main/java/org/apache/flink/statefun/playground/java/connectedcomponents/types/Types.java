package org.apache.flink.statefun.playground.java.connectedcomponents.types;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

import org.apache.flink.statefun.sdk.java.TypeName;
import org.apache.flink.statefun.sdk.java.types.SimpleType;
import org.apache.flink.statefun.sdk.java.types.Type;
import org.apache.flink.api.java.tuple.Tuple2;

public final class Types {

  private Types() {
  }

  private static final ObjectMapper JSON_OBJ_MAPPER = new ObjectMapper();
  private static final String TYPES_NAMESPACE = "connected-components.types";

  /**
   * Type denoting a new vertex coming from the input source.
   */
  public static final Type<Vertex> VERTEX_INIT_TYPE =
      SimpleType.simpleImmutableTypeFrom(
          TypeName.typeNameOf(TYPES_NAMESPACE, "vertex"),
          JSON_OBJ_MAPPER::writeValueAsBytes,
          bytes -> JSON_OBJ_MAPPER.readValue(bytes, Vertex.class));

  /**
   * type for the message to add new incoming edge of a vertex
   * since we are sending a Vertex in the message, this type is same as the VERTEX_INIT_TYPE,
   * but we are not initializing a vertex with this type
   */
  public static final Type<Vertex> Add_IN_EDGE_TYPE = VERTEX_INIT_TYPE;

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

  @SuppressWarnings("unchecked")
  public static final Type<List<CustomTuple2>> IN_NEIGHBORS_TYPE =
      SimpleType.simpleImmutableTypeFrom(
          TypeName.typeNameOf(TYPES_NAMESPACE, "inNeighbors"),
          JSON_OBJ_MAPPER::writeValueAsBytes,
          bytes -> JSON_OBJ_MAPPER.readValue(bytes, new TypeReference<List<CustomTuple2>>() {})
      );

  public static final Type<InEdgesQuery> IN_EDGES_QUERY_TYPE =
      SimpleType.simpleImmutableTypeFrom(
          TypeName.typeNameOf(TYPES_NAMESPACE, "inEdgesQuery"),
          JSON_OBJ_MAPPER::writeValueAsBytes,
          bytes -> JSON_OBJ_MAPPER.readValue(bytes, InEdgesQuery.class)
      );

  public static final Type<EgressRecord> EGRESS_RECORD_JSON_TYPE =
      SimpleType.simpleImmutableTypeFrom(
          TypeName.typeNameOf("io.statefun.playground", "EgressRecord"),
          JSON_OBJ_MAPPER::writeValueAsBytes,
          bytes -> JSON_OBJ_MAPPER.readValue(bytes, EgressRecord.class));
}
