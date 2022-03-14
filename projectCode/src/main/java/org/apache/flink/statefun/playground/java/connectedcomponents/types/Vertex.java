package org.apache.flink.statefun.playground.java.connectedcomponents.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.flink.api.java.tuple.Tuple3;

import java.util.List;

public class Vertex {

  @JsonProperty("src")
  private int src;

  @JsonProperty("dst")
  private int dst;

  @JsonProperty("t")
  private long timestamp;

  public Vertex() {}

  public Vertex(int src, int dst, long timestamp) {
    this.src = src;
    this.dst = dst;
    this.timestamp = timestamp;
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
}
