package com.company;

//import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;

public class SimpleSortedInsert {

    LinkedList<String> dictionary;
    String[] args;

    public SimpleSortedInsert(){
        this.dictionary = new LinkedList<String>();
    }

    public SimpleSortedInsert(String[] args){
        this();
        this.args = args;
    }

    public void run() throws IOException {
        // Get absolute path to unsorted dictionary
        Path root = Paths.get("").normalize().toAbsolutePath();

        if (root.endsWith("src")){
            root = root.getParent();
        }
        if(args !=null) {
            String[] temp = new String[args.length -1];
            for (String arg : args) {
                if (arg.equals("-1")) {
                    for (int i=0; i<temp.length; i++) {
                        if (!args[i].equals("-1"))  temp[i] = arg;
                    }
                    args = temp;
                    testDictionary(root, args);
                    break;
                }
            }
        }

        String file_name_unsorted = "unsorteddict";
        read_file(root, file_name_unsorted);
        String file_name_sorted = "sorteddict";
        write_to_file(root, file_name_sorted);

        if(args !=null){
            for (String element: search_word_or_number(args)){
                System.out.println(element);
            }
        }

    }

    private void testDictionary(Path root, String[] args) throws IOException {
        System.out.println("*********************");
        System.out.println("TEST MODE ACTIVATED");
        boolean testing = true;

        File f1 = new File(root + "/Files/" + "sortedDictTest" + ".txt");

        String file_name_unsorted = "unsortedDictTest";
        read_file(root, file_name_unsorted);
        String file_name_sorted = "sorted_to_test";
        write_to_file(root, file_name_sorted);

        File f2 = new File(root + "/Files/" + file_name_sorted + ".txt");

        while (testing){
            Scanner scan_reference = new Scanner(f1);
            Scanner scan_sorted_by_program = new Scanner(f2);

            System.out.println();
            System.out.println("There are 4 possible tests, press the number to indicate which one you want:");
            System.out.println("1) Check entire test file word by word");
            System.out.println("2) Check with all arguments you already inputted");
            System.out.println("3) Check with specific words or indexes you are interested in - user input wil be requested");
            System.out.println("4) Check random words - the number of words will be given by the user");
            System.out.println("  -- To exit & continue with the program type '-1' --");
            Scanner user = new Scanner(System.in);
            String test = user.next();

            switch (test){
                case "1":

                    // Check 10,000 words of one dictionary to the other
                    System.out.println("TEST 1: Checking all 10000 words are equal...");
                    boolean test1 = true;
                    int line = 0;
                    while(scan_reference.hasNext() || scan_sorted_by_program.hasNext()){
                        String reference = "null";
                        if(!scan_reference.hasNext()){
                            System.out.println("The dictionary created by the program has more words than the test dictionary.");
                            System.out.println("Following are the missing words:");
                            test1 = false;
                        }else{
                            reference = scan_sorted_by_program.nextLine();
                        }

                        String in_dictionary = "null";
                        if(!scan_sorted_by_program.hasNext()){
                            System.out.println("The dictionary created by the program is shorter than the test dictionary.");
                            System.out.println("Following are the missing words:");
                            test1 = false;
                        }else{
                            in_dictionary = scan_sorted_by_program.nextLine();
                        }

                        line++;
                        if (!reference.equals(in_dictionary)){
                            System.out.println("Error");
                            System.out.print(" -> " + reference + " is not equal to: " + in_dictionary + " index: " + line);
                            test1 = false;
                        }
                    }
                    if (test1) System.out.println("TEST 1: Completed successfully");
                    else System.out.println("TEST 1: Not completed successfully, check the code to see where the mistake is");

                    break;
                case "2":

                    // Check original user input
                    boolean test2 = true;
                    System.out.println("TEST 2: Checking whether the arguments (excluding -1) you inputted are correctly positioned in the dictionary");


                    if (test2) System.out.println("TEST 2: Completed successfully");
                    else System.out.println("TEST 2: Not completed successfully, check the code to see where the mistake is");

                    break;
                case "3":

                    // Check specific values the user is interested in
                    boolean test3 = true;
                    System.out.println("TEST 3: Please input some additional specific words or indexed that you want checked");
                    System.out.println("Type '-1' to skip this test");


                    if (test3) System.out.println("TEST 3: Completed successfully");
                    else System.out.println("TEST 2: Not completed successfully, check the code to see where the mistake is");

                    break;
                case "4":

                    // Check specific values the user is interested in
                    boolean test4 = true;
                    System.out.println("TEST 4: Please input a number of random words you want to be checked.");
                    System.out.println("Type '-1' to skip this test");


                    if (test4) System.out.println("TEST 4: Completed successfully");
                    else System.out.println("TEST 4: Not completed successfully, check the code to see where the mistake is");

                    break;
                case "-1":
                    testing = false;
                    break;
                default:
                    System.out.println("Please input a number between 1 and 4.");
            }

        }

        System.out.println("TEST MODE FINISHED");
        System.out.println("The program will now execute as normal - sorting the full unsorted dictionary - without '-1' as an argument");
        System.out.println("*********************");
    }

    public void read_file(Path root, String file_name) throws FileNotFoundException {

        String dictionary_path = root + "/Files/" + file_name + ".txt";

        File f = new File(dictionary_path);
        Scanner scan = new Scanner(f);

        int lines = 0;
        long startingTime = System.currentTimeMillis();

        while(scan.hasNextLine() && lines<100000){
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
        //System.out.println(elapsedTime);

    }


    public void sorted_insert(String word, int lines){

        if (lines == 0){
            dictionary.add(word);
        }else {
            ListIterator<String> iterator;

            int distance_first = Math.abs(word.compareToIgnoreCase(dictionary.getFirst()));
            int distance_last = Math.abs(word.compareToIgnoreCase(dictionary.getLast()));

            // solution so the algorithm works by ignoring all accents - 40000

//            if(!StringUtils.stripAccents(word).equals(word)){
//                word = StringUtils.stripAccents(word);
//            }

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

        private void write_to_file (Path root, String file_name) throws IOException {

            PrintWriter outputStream = new PrintWriter(root + "/Files/" + file_name + ".txt", "UTF-8");

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
                        String word = dictionary.get(word_position -1);
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