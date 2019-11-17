package com.company;

import java.util.LinkedList;

class SkipList extends LinkedList {

    private int MaxLevel;
    private int numOfElementsWithMaxLevel;
    // for more consistent results than with 0.25
    private double p = 0.5;

    private SkipNode iterator;

    private SkipNode head;
    private int level;
    private int size;

    public SkipList(){
        String smallest_string = "";
        this.level = 1;
        this.head= new SkipNode(level, smallest_string);
        this.iterator = this.head;
        this.size = 0;
        this.MaxLevel = 0;
        newFrontier();
    }

    private void newFrontier(){
        MaxLevel++;
        numOfElementsWithMaxLevel = (int) Math.pow(2, MaxLevel);
    }

    private int randomLevel(){
        int lvl = 1;
        while (Math.random()<p && lvl < MaxLevel)
            lvl++;

        return lvl;
    }

    public String get(int index){
        SkipNode current = head;

        if (index > size)
            return null;

        for (int i=0; i<index; i++)
            current = current.forward[0];

        return current.forward[0].value;
    }

    public void insert(String data){
        SkipNode current = head;
        SkipNode[] nodesToUpdate = new SkipNode[MaxLevel];
        int newNodeLevel = randomLevel();
        SkipNode newNode = new SkipNode(newNodeLevel, data);

        size++;

        if(size >= numOfElementsWithMaxLevel)
            newFrontier();

        for (int i = current.level-1; i >= 0; i--) {
            boolean terminate = false;
            while(!terminate){
                if(current.forward[i] == null)
                    break;

                if(data.compareToIgnoreCase(current.forward[i].value) > 0)
                    current = current.forward[i];
                else
                    terminate = true;
            }
            nodesToUpdate[i] = current;
        }

        if(newNodeLevel > this.level){
            SkipNode[] new_head_forwards = new SkipNode[newNodeLevel];
            for(int i=0; i<this.level; i++)
                new_head_forwards[i] = head.forward[i];

            for(int i=this.level; i<newNodeLevel; i++)
                nodesToUpdate[i] = head;

            head.level = newNodeLevel;
            head.forward = new_head_forwards;
            this.level = newNodeLevel;
        }

        for(int i=0; i<newNodeLevel; i++){
            newNode.forward[i] = nodesToUpdate[i].forward[i];
            nodesToUpdate[i].forward[i] = newNode;
        }
    }

    public void reset_iterator(){
        iterator = head;
    }

    public boolean hasNext() {
        if(iterator.forward[0] != null) return true;
        else return false;
    }

    public SkipNode next() {
        iterator = iterator.forward[0];
        return iterator;
    }

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
}