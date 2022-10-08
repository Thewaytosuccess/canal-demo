package com.ph.workflow;

import lombok.Data;

@Data
public class PeEdge {
    public String id;

    //来自于哪个节点
    public PeNode from;

    //指向哪个节点
    public PeNode to;

    public PeEdge(String id) {
        this.id = id;
    }
}
