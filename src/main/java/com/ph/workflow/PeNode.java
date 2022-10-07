package com.ph.workflow;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class PeNode {
    //id
    public String id;

    //类型
    public String type;

    //入边
    public List<PeEdge> in = new ArrayList<>();

    //出边
    public List<PeEdge> out = new ArrayList<>();

    public Node xmlNode;

    public PeNode(String id) {
        this.id = id;
    }

    public PeEdge onlyOneOut() {
        return out.get(0);
    }

    public PeEdge outWithID(String nextPeEdgeID) {
        return out.stream().filter(e -> e.id.equals(nextPeEdgeID)).findFirst().orElse(null);
    }

    public PeEdge outWithOutID(String nextPeEdgeID) {
        return out.stream().filter(e -> !e.id.equals(nextPeEdgeID)).findFirst().orElse(null);
    }

}
