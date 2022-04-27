package org.apache.flink.statefun.playground.java.graphanalytics.types;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class defines the type of the query for counting the number of outgoing edges of a vertex.
 * The function that is responsible for dispatching queries should send a message of this type to the InEdgesQueryFn.
 */
public class OutEdgesQuery {

    @JsonProperty("vertex_id")
    private int vertexId;

    @JsonProperty("t")
    private long timestamp;

    // this variable is only used for latency experiment
    @JsonProperty("start")
    private long start;

    public OutEdgesQuery() {}

    /**
     * overloaded constructor
     * @param vertexId
     * @param timestamp
     * @param start
     */
    private OutEdgesQuery(int vertexId, long timestamp, long start) {
        this.vertexId = vertexId;
        this.timestamp = timestamp;
        this.start = start;
    }

    public int getVertexId() { return vertexId; }

    public long getTimestamp() { return timestamp; }

    public long getStart() {return start;}

    /**
     * This method is used for creating a new InEdgesQuery, please call InEdgesQuery.create(vertex_id, timestamp)
     * to create an object of this class
     *
     * @param vertexId
     * @param timestamp
     * @param start
     * @return InEdgesQuery
     */
    public static OutEdgesQuery create(int vertexId, long timestamp, long start) {
        return new OutEdgesQuery(vertexId, timestamp, start);
    }
}
