package com.company;

import org.apache.commons.lang3.StringUtils;
import sun.security.acl.WorldGroupImpl;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;

public class SimpleSortedInsert {

    LinkedList<String> dictionary;

    public SimpleSortedInsert(){
        this.dictionary = new LinkedList<String>();
    }

    public SimpleSortedInsert(String[] args){
        this();
        for (String element: search_word_or_number(args)){
            System.out.println(element);
        }
    }

    public void run() throws IOException {
        // Get absolute path to unsorted dictionary
        Path root = Paths.get("").normalize().toAbsolutePath();

        if (root.endsWith("src")){
            root = root.getParent();
        }

        read_file(root);
        write_to_file(root);
    }

    public void read_file(Path root) throws FileNotFoundException {

        String dictionary_path = root + "/Files/unsorteddict.txt";

        File f = new File(dictionary_path);
        Scanner scan = new Scanner(f);

        int lines = 0;
        long startingTime = System.currentTimeMillis();

        while(scan.hasNextLine() && lines<10000000){
            String word = scan.nextLine();
            sorted_insert(word, lines);
            lines++;
            //System.out.println(lines);
        }

        long elapsedTime = (System.currentTimeMillis() - startingTime);
        /*
        for(String word: dictionary){
            System.out.println(word);
        }*/
        System.out.println(elapsedTime);

    }

    // Works 45058 when reading from start or end depending
    // With 'l'
    /*
    public void sorted_insert(String word, int lines){

        if (lines == 0){
            dictionary.add(word);
        }else {
            ListIterator<String> iterator;

            if(word.charAt(0) > 'l'){
                iterator = dictionary.listIterator(lines);
                while(iterator.hasPrevious()){
                    String element = iterator.previous();
                    if (word.compareToIgnoreCase(element) > 0) {
                        iterator.next();
                        break;
                    }
                }
            }else{
                iterator = dictionary.listIterator(0);
                while(iterator.hasNext()){
                    String element = iterator.next();
                    if (word.compareToIgnoreCase(element) < 0) {
                        iterator.previous();
                        break;
                    }
                }
            }
            iterator.add(word);
        }
    }

     */



    public void sorted_insert(String word, int lines){

        if (lines == 0){
            dictionary.add(word);
        }else {
            ListIterator<String> iterator;

            int distance_first = Math.abs(word.compareToIgnoreCase(dictionary.getFirst()));
            int distance_last = Math.abs(word.compareToIgnoreCase(dictionary.getLast()));

            // solution so the algorithm works by ignoring all accents - 40000

            if(!StringUtils.stripAccents(word).equals(word)){
                word = StringUtils.stripAccents(word);
            }

            if(distance_last < distance_first){
                iterator = dictionary.listIterator(lines);
                while(iterator.hasPrevious()){
                    String element = iterator.previous();
                    if (word.compareToIgnoreCase(element) > 0) {
                        iterator.next();
                        break;
                    }
                }
            }else{
                iterator = dictionary.listIterator(0);
                while(iterator.hasNext()){
                    String element = iterator.next();
                    if (word.compareToIgnoreCase(element) < 0) {
                        iterator.previous();
                        break;
                    }
                }
            }
            iterator.add(word);
        }
    }



/*
public void sorted_insert(String to_insert, int lines){

        String word = StringUtils.stripAccents(to_insert);

        if (lines == 0){
            dictionary.add(to_insert);
        }else {
            ListIterator<String> iterator;

            int distance_first = word.compareToIgnoreCase(StringUtils.stripAccents(dictionary.getFirst()));
            int distance_last = word.compareToIgnoreCase(StringUtils.stripAccents(dictionary.getLast()));

            if(distance_last < distance_first){
                iterator = dictionary.listIterator(lines - 1);
                while(iterator.hasPrevious()){
                    String element = StringUtils.stripAccents(iterator.previous());
                    if (word.compareToIgnoreCase(element) > 0) {
                        iterator.next();
                        break;
                    }
                }
            }else{
                iterator = dictionary.listIterator(0);
                while(iterator.hasNext()){
                    String element = StringUtils.stripAccents(iterator.next());
                    if (word.compareToIgnoreCase(element) < 0) {
                        iterator.previous();
                        break;
                    }
                }

            }
            iterator.add(to_insert);
        }
    }
 */
/*
public void sorted_insert(String word, int lines){

    if (lines == 0){
        dictionary.add(word);
    }else {
        ListIterator<String> iterator = dictionary.listIterator();
        while(iterator.hasNext()){
            String element = iterator.next();
            if (word.compareToIgnoreCase(element) < 0) {
                iterator.previous();
                break;
            }
        }
        iterator.add(word);
    }
}

 */
        private void write_to_file (Path root) throws IOException {

            PrintWriter outputStream = new PrintWriter(root + "/Files/sorteddict.txt", "UTF-8");

            for (String element : dictionary) {
                outputStream.println(element);
            }

            outputStream.close();

        }

        public String[] search_word_or_number (String[] input) {

            int size = input.length;
            if (size > 10) {
                System.out.println("The maximum number of inputs is 10");
                System.out.print("Inputs read: ");
                size = 10;
                for (int i = 0; i < 10; i++) {
                    System.out.print(input[i] + " ");
                }
                System.out.println();

            }
            String regex = "\\d+";
            String[] result = new String[size];

            for (int i = 0; i < size; i++) {
                if(input[i].matches(regex)){
                    int word_position = Integer.parseInt(input[i]);
                    try {
                        String word = dictionary.get(word_position - 1);
                        result[i] = word;
                    } catch (IndexOutOfBoundsException e) {
                        result[i] = "Index not in dictionary";
                    }
                }else{
                    int word_position = dictionary.indexOf(input[i]);
                    if (word_position != -1) result[i] = Integer.toString(word_position + 1);
                    else result[i] = "-1";
                }
            }
            return result;
        }
}