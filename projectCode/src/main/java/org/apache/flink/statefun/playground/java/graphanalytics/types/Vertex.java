package org.apache.flink.statefun.playground.java.graphanalytics.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Vertex {

  @JsonProperty("src")
  private int src;

  @JsonProperty("dst")
  private int dst;

  @JsonProperty("t")
  private long timestamp;

  // this variable is only used for latency experiment
  @JsonProperty("start")
  private long start;

  public Vertex() {}

  /**
   * overloaded constructor
   *
   * @param src
   * @param dst
   * @param timestamp
   * @param start
   */
  public Vertex(int src, int dst, long timestamp, long start) {
    this.src = src;
    this.dst = dst;
    this.timestamp = timestamp;
    this.start = start;
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

  public long getStart() { return start; }
}
