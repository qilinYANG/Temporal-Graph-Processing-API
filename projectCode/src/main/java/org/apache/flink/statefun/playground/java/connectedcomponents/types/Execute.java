package org.apache.flink.statefun.playground.java.connectedcomponents.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.flink.api.java.tuple.Tuple3;

import java.util.List;

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

    public Execute(){}

    private Execute(String task, int src, int dst, long timestamp) {
        this.task = task;
        this.src = src;
        this.dst = dst;
        this.timestamp = timestamp;
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

    public static Execute create(String task, int src, int dst, long timestamp) {
        return new Execute(task, src, dst, timestamp);
    }
}