package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;

public class SimpleSortedInsert {

    LinkedList<String> dictionary;
    Path root;
    String[] input;

    public SimpleSortedInsert(){
        this(null);
    }

    public SimpleSortedInsert(String[] args){
        this.dictionary = new LinkedList<String>();
        this.input = args;
    }

    public void run() throws IOException {
        read_file();
        write_to_file();
        if(input != null){
            String[] output = search_word_or_number();

            for (String element: output){
                System.out.println(element);
            }
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
        while(scan.hasNextLine() && index<1000000){
            String word = scan.nextLine();
            sorted_insert(word);
            index++;
            //System.out.println(index);
        }

        long elapsedTime = (System.currentTimeMillis() - startingTime);
        /*
        for(String word: dictionary){
            System.out.println(word);
        }*/
        System.out.println(elapsedTime);

    }

    // WORKS 3 mins plus - 159485
/*
    public void sorted_insert(String word){
        int pos = 0;

        if (dictionary.size()==0){
            dictionary.add(pos, word);
            return;
        }

        int distance_first = word.compareToIgnoreCase(dictionary.getFirst());

        if (distance_first<0){
            dictionary.add(pos, word);
            return;
        }
        int distance_last = word.compareToIgnoreCase(dictionary.getLast());
        if (distance_last>0) {
            pos = dictionary.size();
            dictionary.add(pos, word);
            return;
        }

        if (Math.abs(distance_first) < Math.abs(distance_last)){
            for(String element : dictionary) {
                if (word.compareToIgnoreCase(element) < 0) {
                    pos = dictionary.indexOf(element);
                    break;
                }
            }
        }else {
            for (int i = dictionary.size() - 1; i >= 0; i--) {
                String element = dictionary.get(i);
                if (word.compareToIgnoreCase(element) > 0) {
                    pos = dictionary.indexOf(element);
                    break;
                }
            }
        }

        dictionary.add(pos, word);
    }

 */

        // Works -158414, || 91701, 93562|| 60000

    public void sorted_insert(String word){

        int pos = 0;
        if (this.dictionary.size() == 0){
            dictionary.add(word);

        }else if (word.compareToIgnoreCase(dictionary.getLast())>0) {
            dictionary.addLast(word);
        }else {

            ListIterator<String> iterator = dictionary.listIterator();

            while(iterator.hasNext()){
                String element = iterator.next();
                if (word.compareToIgnoreCase(element) < 0) {
                    pos = iterator.nextIndex() - 1;
                    break;
                }
            }

            dictionary.add(pos, word);
        }
    }

    /*
    public void sorted_insert(String word){

        int pos = 0;
        String temp = Normalizer.normalize(word, Normalizer.Form.NFD);
        if (this.dictionary.size() == 0){
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

     */

        private void write_to_file () throws IOException {

            PrintWriter outputStream = null;
            outputStream = new PrintWriter(root + "/Files/sorteddict.txt", "UTF-8");

            for (String element : dictionary) {
                outputStream.println(element);
            }

            outputStream.close();

        }

        public String[] search_word_or_number () {

            int size = this.input.length;
            if (size > 10) {
                System.out.println("The maximum number of inputs is 10");
                System.out.print("Inputs read: ");
                size = 10;
                for (int i = 0; i < 10; i++) {
                    System.out.print(input[i] + " ");
                }
                System.out.println();

            }
            String[] result = new String[size];

            for (int i = 0; i < size; i++) {
                try {
                    int word_position = Integer.parseInt(this.input[i]);
                    try {
                        String word = dictionary.get(word_position - 1);
                        result[i] = word;
                    } catch (IndexOutOfBoundsException e) {
                        result[i] = "Index not in dictionary";
                    }

                } catch (NumberFormatException e) {
                    int word_position = dictionary.indexOf(this.input[i]);
                    if (word_position != -1) result[i] = Integer.toString(word_position + 1);
                    else result[i] = "-1";

                } catch (IndexOutOfBoundsException ignored) {
                    ignored.printStackTrace();
                }
            }
            return result;
        }
        // 331246 milis 5 mins
    /*
    public void sorted_insert(String word){

        int pos = 0;
        if (this.dictionary.size() == 0){
            dictionary.add(pos, word);
        }else if (compareTo(dictionary.getFirst(), word)[1]>0){
            dictionary.add(pos, word);
        }else if (compareTo(dictionary.getLast(), word)[1]<0) {
            pos = dictionary.size();
            dictionary.add(pos, word);
        }else {

            for (String element : dictionary) {
                if (compareTo(word, element)[1] < 0) {
                    pos = dictionary.indexOf(element);
                    break;
                }
            }
            dictionary.add(pos, word);
        }
    }
     */

}