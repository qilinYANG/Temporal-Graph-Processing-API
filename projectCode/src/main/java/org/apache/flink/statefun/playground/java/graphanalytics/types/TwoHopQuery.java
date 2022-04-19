package org.apache.flink.statefun.playground.java.graphanalytics.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TwoHopQuery {

    @JsonProperty("vertex_id")
    private int vertexId;

    @JsonProperty("t")
    private long timestamp;

    public TwoHopQuery() {}

    /**
     * overloaded constructor
     * @param vertexId
     * @param timestamp
     */
    private TwoHopQuery(int vertexId, long timestamp) {
        this.vertexId = vertexId;
        this.timestamp = timestamp;
    }

    public int getVertexId() { return vertexId; }

    public long getTimestamp() { return timestamp; }

    /**
     * This method is used for creating a new InEdgesQuery, please call InEdgesQuery.create(vertex_id, timestamp)
     * to create an object of this class
     *
     * @param vertexId
     * @param timestamp
     * @return InEdgesQuery
     */
    public static TwoHopQuery create(int vertexId, long timestamp) {
        return new TwoHopQuery(vertexId, timestamp);
    }
}
