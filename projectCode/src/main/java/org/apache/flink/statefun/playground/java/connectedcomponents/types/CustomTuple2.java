package org.apache.flink.statefun.playground.java.connectedcomponents.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomTuple2 {

  @JsonProperty("f0")
  private int f0;

  @JsonProperty("f1")
  private long f1;

  public CustomTuple2() {}

  private CustomTuple2(int field0, long field1) {
    this.f0 = field0;
    this.f1 = field1;
  }

  public int getF0() { return f0; }

  public long getF1() { return f1; }

  public static CustomTuple2 createTuple2(int field0, long field1) {
    return new CustomTuple2(field0, field1);
  }

  public String toString() {
    return "(" + String.valueOf(f0) + "," + String.valueOf(f1) + ")";
  }
}
