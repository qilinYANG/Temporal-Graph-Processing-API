package org.cs551.project6.types;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.statefun.sdk.java.ValueSpec;

import java.util.ArrayList;
import java.util.List;

interface IChronosEdge {
    /**
     * A public method that adds edges to the current vertex
     * @param edgeActivity activity to be added to edge
     */
    void addEdgeActivity(Tuple3<ValueSpec<String>, ChronosEdge, Float> edgeActivity);
}

public class ChronosEdge implements IChronosEdge {
    ArrayList<Tuple3<Integer, Integer, Float>> checkpoint;
    // LinkedList in Chronos
    private List<Tuple3<ValueSpec<String>, ChronosEdge, Float>> activityEdges;

    ChronosEdge(ArrayList<Tuple3<Integer, Integer, Float>> ch){
        checkpoint = ch;
    }

    public void addEdgeActivity(Tuple3<ValueSpec<String>, ChronosEdge, Float> edgeActivity) {
    }
}
