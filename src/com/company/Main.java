package com.company;

import java.io.*;

public class Main {

    public static void main(String[] args){

        String[] a = {"-1", "1"};

        SimpleSortedInsert app = new SimpleSortedInsert(a);
        try {
            app.run();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}