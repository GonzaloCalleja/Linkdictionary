package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args){

        SimpleSortedInsert app = new SimpleSortedInsert();
        try {
            app.execute();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}

class SimpleSortedInsert {

    LinkedList<String> dictionary;

    public void execute() throws FileNotFoundException {
        // Get absolute path to unsorted dictionary
        Path root = Paths.get(".").normalize().toAbsolutePath();
        String dictionary_path = root + "/unsorteddict.txt";

        File f = new File(dictionary_path);
        Scanner scan = new Scanner(f);

        dictionary = new LinkedList<String>();

        long startingTime = System.currentTimeMillis();
        int index = 0;
        while(scan.hasNextLine() && index<1000000){
            String word = scan.nextLine();
            sorted_insert(word);
            index++;
            System.out.println(index);
        }

        long elapsedTime = (System.currentTimeMillis() - startingTime);
        for(String word: dictionary){
            System.out.println(word);
        }
        System.out.println(elapsedTime);
    }

    public void sorted_insert(String word){
        int pos = 0;
        String temp = Normalizer.normalize(word, Normalizer.Form.NFD);
        if (dictionary.size()==0){
            dictionary.add(pos, word);
            return;
        }
        if (temp.compareToIgnoreCase(dictionary.getFirst())<0){
            dictionary.add(pos, word);
            return;
        }
        if (temp.compareToIgnoreCase(dictionary.getLast())>0) {
            pos = dictionary.size();
            dictionary.add(pos, word);
            return;
        }

        for(String element : dictionary){
            if (temp.compareToIgnoreCase(element)<0){
                pos = dictionary.indexOf(element);
                break;
            }
        }
        dictionary.add(pos, word);
    }
}