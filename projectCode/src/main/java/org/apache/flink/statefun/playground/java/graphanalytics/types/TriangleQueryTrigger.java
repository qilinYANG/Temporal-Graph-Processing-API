package org.apache.flink.statefun.playground.java.graphanalytics.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TriangleQueryTrigger {
    @JsonProperty("vertex_id")
    private int vertexId;

    // this variable is only used for latency experiment
    @JsonProperty("start")
    private long start;

    public TriangleQueryTrigger() {}

    /**
     * overloaded constructor
     * @param vertexId
     * @param start
     */
    private TriangleQueryTrigger(int vertexId, long start) {
        this.vertexId = vertexId;
        this.start = start;
    }

    public int getVertexId() { return vertexId; }

    public long getStart() { return start; }
    
    /**
     * This method is used for creating a new TriangleQueryTrigger, please call TriangleQueryTrigger.create(vertex_id, start)
     * to create an object of this class
     *
     * @param vertexId
     * @param start
     * @return TriangleQueryTrigger
     */
    public static TriangleQueryTrigger create(int vertexId, long start) {
        return new TriangleQueryTrigger(vertexId, start);
    }
}
