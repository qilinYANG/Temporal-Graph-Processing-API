package org.apache.flink.statefun.playground.java.graphanalytics.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

/**
 * This class defines the type of the query for counting the number of incoming edges of a vertex.
 * The function that is responsible for dispatching queries should send a message of this type to the InEdgesQueryFn/OutEdgesQueryFn.
 */
public class KHopQuery {

    @JsonProperty("vertex_id")
    private int vertexId;

    @JsonProperty("current_id")
    private int current_id;

    @JsonProperty("k")
    private int k;

    @JsonProperty("n")
    private int n;

    @JsonProperty("trace")
    private ArrayList<Integer> trace;

    // this variable is only used for latency experiment
    @JsonProperty("start")
    private long start;

    public KHopQuery() {}

    /**
     * overloaded constructor
     * @param vertexId
     * @param start
     */
    private KHopQuery(int vertexId, int current_id, int k, int n, ArrayList<Integer> trace, long start) {
        this.vertexId = vertexId;
        this.current_id = current_id;
        this.k = k;
        this.n = n;
        this.trace = trace;
        this.start = start;
    }

    public int getVertexId() { return vertexId; }

    public int getCurrentId() { return current_id; }

    public int getK() { return k; }

    public int getN() { return n; }

    public ArrayList<Integer> getTrace() { return trace; }

    public long getStart() {return start;}

    /**
     * This method is used for creating a new KHopQuery, please call KHopQuery.create(vertex_id, timestamp)
     * to create an object of this class
     *
     * @param vertexId
     * @param k
     * @param n
     * @param trace
     * @param start
     * @return KHopQuery
     */
    public static KHopQuery create(int vertexId, int current_id, int k, int n, ArrayList<Integer> trace, long start) {
        return new KHopQuery(vertexId, current_id, k, n, trace, start);
    }
}
