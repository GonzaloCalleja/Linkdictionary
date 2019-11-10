package com.company;

class SkipList{

    private int MaxLevel;
    private int maxLevelFrontier;
    // for more consistent results than with 0.25
    private double p = 0.5;

    private SkipNode iterator;

    SkipNode head;
    int level = 1;
    int size;

    public SkipList(){
        String smallest_string = "";
        this.head= new SkipNode(level, smallest_string);
        this.iterator = this.head;
        this.size = 0;
        this.MaxLevel = 0;
        newFrontier();
    }

    private void newFrontier(){
        maxLevelFrontier = (int) Math.pow(2, MaxLevel);
    }

    private int randomLevel(){
        int lvl = 1;

        while (Math.random()<p && lvl < MaxLevel){
            lvl++;
        }

        return lvl;
    }

    public String getValue(int index){
        if (index > size) return null;
        SkipNode current = head;
        for (int i=0; i<index; i++)current = current.forward[0];
        return current.forward[0].value;
    }

    public int indexOf(String data){
        SkipNode current = head;
        for (int i=0; i<size; i++){
            if(data.compareToIgnoreCase(current.value) == 0) return i;
            else current = current.forward[0];
        }
        return -1;
    }

    public SkipNode search(String data){
        SkipNode current = head;
        SkipNode result = null;

        for (int i = current.level-1; i >= 0; i--) {
            boolean terminate = false;
            while(!terminate){
                if(current.forward[i] == null) break;

                if(data.compareToIgnoreCase(current.forward[i].value) == 0){
                    result = current.forward[i];
                }else terminate = true;
            }
        }

        return result;
    }

    public void insert(String data){
        size++;

        if(size >= maxLevelFrontier){
            MaxLevel++;
            newFrontier();
        }

        SkipNode current = head;
        SkipNode[] update = new SkipNode[MaxLevel];

        for (int i = current.level-1; i >= 0; i--) {
            boolean terminate = false;
            while(!terminate){
                if(current.forward[i] == null) break;

                if(data.compareToIgnoreCase(current.forward[i].value) > 0){
                    current = current.forward[i];
                }else terminate = true;
            }
            update[i] = current;
        }

        int level = randomLevel();

        if(level > this.level){
            SkipNode[] new_head_forwards = new SkipNode[level];
            for(int i=0; i<this.level; i++){
                new_head_forwards[i] = head.forward[i];
            }
            for(int i=this.level; i<level; i++){
                update[i] = head;
            }
            head.level = level;
            head.forward = new_head_forwards;
            this.level = level;
        }
        SkipNode toadd = new SkipNode(level, data);

        for(int i=0; i<level; i++){
            toadd.forward[i] = update[i].forward[i];
            update[i].forward[i] = toadd;
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
}