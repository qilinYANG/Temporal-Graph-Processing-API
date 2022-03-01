import org.apache.flink.api.java.tuple.Tuple2;

import java.util.ArrayList;

public class Vertex {
    private int vertixID;
    private ArrayList<Tuple2<Float,Vertex>> Neighourconnection;
//    Timestamp
    private ArrayList<Vertex> sf;

    private ArrayList<Vertex> getNeighour(){
        ArrayList<Vertex> neighour = new ArrayList<>();
        for(Tuple2<Float,Vertex> each : Neighourconnection){
            if (!neighour.contains(each.f1)){
                neighour.add(each.f1);
            }
        }
        return neighour;
    }

    private void addNeighour(Tuple2<Float,Vertex> newNeighour){
        Neighourconnection.add(newNeighour);
    }
}
