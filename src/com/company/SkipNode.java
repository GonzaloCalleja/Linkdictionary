package com.company;

public class SkipNode {
    SkipNode[] forward;
    int level;
    String value;

    public SkipNode(int level, String data){
        this.value = data;
        this.level = level;
        this.forward = new SkipNode[this.level];

    }
}
