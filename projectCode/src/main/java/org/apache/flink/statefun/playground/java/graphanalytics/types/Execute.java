package org.apache.flink.statefun.playground.java.graphanalytics.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Simple class for unified command type.
 * All commands to stateful fucntions are encapsulated as an Execute Object
 */
public class Execute {
    @JsonProperty("task")
    private String task;

    @JsonProperty("src")
    private int src;

    @JsonProperty("dst")
    private int dst;

    @JsonProperty("t")
    private long timestamp;

    @JsonProperty("endTime")
    private long endTime;

    public Execute(){}

    private Execute(String task, int src, int dst, long timestamp, long endTime) {
        this.task = task;
        this.src = src;
        this.dst = dst;
        this.timestamp = timestamp;
        this.endTime = endTime;
    }

    public String getTask() {
        return task;
    }

    public int getSrc() {
        return src;
    }

    public int getDst() {
        return dst;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getEndTime() {
        return endTime;
    }

    public static Execute create(String task, int src, int dst, long timestamp, long endTime) {
        return new Execute(task, src, dst, timestamp, endTime);
    }
}