package com.company;

import com.sun.deploy.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

public class SimpleSortedInsert {

    LinkedList<String> dictionary;
    Path root;
    String[] input;

    public void SimpleSortedInsert(String[] args){
        this.dictionary = new LinkedList<String>();
        this.input = args;
    }

    public void run() throws IOException {
        read_file();
        write_to_file();
        String[] output = search_word_or_number();

        for (String element: output){
            System.out.println(element);
        }
    }

    public void read_file() throws FileNotFoundException {
        // Get absolute path to unsorted dictionary
        root = Paths.get("").normalize().toAbsolutePath();
        if (root.endsWith("src")){
            root = root.getParent();
        }
        String dictionary_path = root + "/Files/unsorteddict.txt";

        File f = new File(dictionary_path);
        Scanner scan = new Scanner(f);

        long startingTime = System.currentTimeMillis();
        int index = 0;
        while(scan.hasNextLine() && index<1000){
            String word = scan.nextLine();
            sorted_insert(word);
            index++;
            System.out.println(index);
        }

        long elapsedTime = (System.currentTimeMillis() - startingTime);
        /*
        for(String word: dictionary){
            System.out.println(word);
        }
        System.out.println(elapsedTime);
         */
    }

    public void sorted_insert(String word){
        int pos = 0;
        String temp = Normalizer.normalize(word, Normalizer.Form.NFD);
        if (dictionary.size()==0){
            dictionary.add(pos, word);
        }else if (temp.compareToIgnoreCase(Normalizer.normalize(dictionary.getFirst(), Normalizer.Form.NFD))<0){
            dictionary.add(pos, word);
        }else if (temp.compareToIgnoreCase(Normalizer.normalize(dictionary.getLast(), Normalizer.Form.NFD))>0) {
            pos = dictionary.size();
            dictionary.add(pos, word);
        }else {

            for (String element : dictionary) {
                if (Normalizer.normalize(word, Normalizer.Form.NFD).compareToIgnoreCase(element) < 0) {
                    pos = dictionary.indexOf(element);
                    break;
                }
            }
            dictionary.add(pos, word);
        }
    }

    private void write_to_file() throws IOException {

        PrintWriter outputStream = null;
        outputStream = new PrintWriter(root + "/Files/sorteddict.txt","UTF-8");

        for(String element: dictionary){
            outputStream.println(element);
        }

        outputStream.close();

    }

    public String[] search_word_or_number() {

        int size = this.input.length;
        if (size>10){
            System.out.println("The maximum number of inputs is 10");
            size = 10;
        }
        String[] result = new String[size];

        for (int i=0; i<size; i++){
            try{
                int word_position = Integer.parseInt(this.input[i]);
                String word = dictionary.get(word_position);
                result[i] = word;
            }catch (NumberFormatException e){
                int word_position = dictionary.indexOf(this.input[i]);
                result[i] = Integer.toString(word_position);
            }catch (IndexOutOfBoundsException ignored){
                ignored.printStackTrace();
            }
        }
        return result;
    }
}