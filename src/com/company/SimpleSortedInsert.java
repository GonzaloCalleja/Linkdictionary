package com.company;

//import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Scanner;


public class SimpleSortedInsert {

    SkipList dictionary;
    String[] args;
    int maxArgs;
    Path root;

    public SimpleSortedInsert(){
        this.dictionary = new SkipList();
        root = Paths.get("").normalize().toAbsolutePath();

        if (root.endsWith("src")){
            root = root.getParent();
        }
        this.maxArgs = 10;
    }

    public SimpleSortedInsert(String[] args){
        this();
        this.args = args;
    }

    public void run() throws IOException {

        if(args !=null && args.length > 0) {
            String[] temp = new String[args.length -1];
            for (String arg : args) {
                if (arg.equals("-1")) {
                    int j = 0;
                    for (int i=0; i<args.length; i++) {
                        if (!args[i].equals("-1")){
                            temp[j] = args[i];
                            j++;
                        }
                    }
                    this.maxArgs = 9;
                    args = temp;
                    testDictionary(args);
                    break;
                }
            }
        }

        String file_name_unsorted = "unsorteddict"; // OTHER FILES: "millionwords"
        read_file(file_name_unsorted);
        String file_name_sorted = "sorteddict";
        write_to_file(file_name_sorted);

        if(args !=null && args.length > 0){
            System.out.print("Arguments: ");
            for (String arg: args){
                System.out.print(arg + "  ");
            }
            System.out.println("\n");
            for (String[] element: search_word_or_number(args)){
                System.out.println(element[0] + " " + element[1]);
            }
        }
    }

    private void testDictionary(String[] args) throws IOException {
        TestForDictionarySorter tester = new TestForDictionarySorter();
        tester.perform_tests(args);
    }

    public void read_file(String file_name) throws FileNotFoundException {

        dictionary = new SkipList();
        String dictionary_path = root + "/Files/" + file_name + ".txt";

        File f = new File(dictionary_path);
        Scanner scan = new Scanner(f);

        while(scan.hasNextLine()){
            String word = scan.nextLine();
            dictionary.insert(word);
        }
    }

    void write_to_file(String file_name) throws IOException {

        PrintWriter outputStream = new PrintWriter(root + "/Files/" + file_name + ".txt", "UTF-8");
        while(dictionary.hasNext()){
            outputStream.println(dictionary.next().value);
        }
        outputStream.close();
    }

    public String[][] search_word_or_number (String[] input) {

        int size = input.length;
        if (size > maxArgs) {
            System.out.println("The maximum number of inputs is 10 or 9 on top of '-1'");
            System.out.print("Inputs read: ");
            size = 10;
            for (int i = 0; i < 10; i++) {
                System.out.print(input[i] + " ");
            }
            System.out.println();
        }
        String regex = "\\d+";
        String[][] result = new String[size][2];

        for (int i = 0; i < size; i++) {
            if(input[i].matches(regex)){
                int word_position = Integer.parseInt(input[i]) ;

                try {
                    String word = dictionary.getValue(word_position - 1);
                    result[i][0] = Integer.toString(word_position);
                    result[i][1] = word;
                } catch (IndexOutOfBoundsException e) {
                    result[i][0] = Integer.toString(word_position);
                    result[i][1] = "Index not in dictionary";
                }
            }else{
                int word_position = dictionary.indexOf(input[i]);
                if (word_position != -1) result[i][0] = Integer.toString(word_position);
                else result[i][0] = "-1";

                result[i][1] = input[i];
            }
        }
        return result;
    }

    public String[][] search_word_or_number (String[] input, LinkedList<String> dictionary) {

        int size = input.length;
        if (size > maxArgs) {
            System.out.println("The maximum number of inputs is 10 or 9 on top of '-1'");
            System.out.print("Inputs read: ");
            size = 10;
            for (int i = 0; i < 10; i++) {
                System.out.print(input[i] + " ");
            }
            System.out.println();
        }
        String regex = "\\d+";
        String[][] result = new String[size][2];

        for (int i = 0; i < size; i++) {
            if(input[i].matches(regex)){
                int word_position = Integer.parseInt(input[i]);

                try {
                    String word = dictionary.get(word_position-1);
                    result[i][0] = Integer.toString(word_position);
                    result[i][1] = word;
                } catch (IndexOutOfBoundsException e) {
                    result[i][0] = Integer.toString(word_position);
                    result[i][1] = "Index not in dictionary";
                }
            }else{
                int word_position = dictionary.indexOf(input[i]);
                if (word_position != -1) result[i][0] = Integer.toString(word_position + 1);
                else result[i][0] = "-1";

                result[i][1] = input[i];
            }
        }
        return result;
    }
}