package com.company;

import java.io.*;

public class Main {

    public static void main(String[] args){

        WordSorterWithSkipList app = new WordSorterWithSkipList();
        try {
            app.run();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}