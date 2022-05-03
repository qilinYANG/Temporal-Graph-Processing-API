package org.apache.flink.statefun.playground.java.graphanalytics.types;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class defines the type of the query for counting the number of incoming
 * edges of a vertex.
 * The function that is responsible for dispatching queries should send a
 * message of this type to the InEdgesQueryFn.
 */
public class TimeWindowRangeQuery {
    @JsonProperty("src")
    private int src;

    @JsonProperty("startTime")
    private long startTime;

    @JsonProperty("endTime")
    private long endTime;

    /**
     * overloaded constructor
     * 
     * @param vertexId
     * @param timestamp
     */
    private TimeWindowRangeQuery(int vertexId, long startTime, long endTime) {
        this.src = vertexId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getSrc() {
        return src;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    /**
     * This method is used for creating a new TimeWindowRangeQuery, please call
     * TimeWindowRangeQuery.create(src, startTime, endTime)
     * to create an object of this class
     *
     * @param src
     * @param startTime
     * @param endTime
     * @return TimeWindowRangeQuery
     */
    public static TimeWindowRangeQuery create(int src, long startTime, long endTime) {
        return new TimeWindowRangeQuery(src, startTime, endTime);
    }
}
