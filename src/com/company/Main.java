package com.company;

import java.io.*;

public class Main {

    public static void main(String[] args){


        SimpleSortedInsert app = new SimpleSortedInsert();
        try {
            app.run();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
