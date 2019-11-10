package com.company;

//import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;


public class SimpleSortedInsert {

    SkipList dictionary;
    String[] args;

    public SimpleSortedInsert(){
        this.dictionary = new SkipList();
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
                    args = temp;
                    testDictionary(root, args);
                    dictionary = new SkipList();
                    break;
                }
            }
        }

        String file_name_unsorted = "unsorteddict";
        read_file(root, file_name_unsorted);
        String file_name_sorted = "sorteddict";
        write_to_file(root, file_name_sorted);

        if(args !=null && args.length > 0){
            System.out.print("Arguments: ");
            for (String arg: args){
                System.out.print(arg + "  ");
            }
            System.out.println("\n");
            for (String[] element: search_word_or_number(args, dictionary)){
                System.out.println(element[0] + " " + element[1]);
            }
        }

    }

    private void testDictionary(Path root, String[] args) throws IOException {
        System.out.println();
        System.out.println("***");
        System.out.println("*****");
        System.out.println("***********");
        System.out.println("******************");
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
            System.out.println("5) Check program execution times with 10,000 words & 100,000 words");
            System.out.println("  -- To exit & continue with the program type '-1' --");
            Scanner user = new Scanner(System.in);
            String test = user.next();

            switch (test){
                case "1":

                    // Check 10,000 words of one dictionary to the other
                    System.out.println("TEST 1: Checking all 10,000 words are equal...\n");
                    boolean test1 = true;
                    int line = 0;
                    while(scan_reference.hasNext() || scan_sorted_by_program.hasNext()){
                        line++;
                        String reference = "null";
                        if(!scan_reference.hasNext()){
                            System.out.println("The dictionary created by the program has more words than the test dictionary.");
                            System.out.println("Following are the missing words:");
                            test1 = false;
                        }else{
                            reference = scan_reference.next();
                        }

                        String in_dictionary = "null";
                        if(!scan_sorted_by_program.hasNext()){
                            System.out.println("The dictionary created by the program is shorter than the test dictionary.");
                            System.out.println("Following are the missing words:");
                            test1 = false;
                        }else{
                            in_dictionary = scan_sorted_by_program.next();
                        }

                        // To allow for incorrectly ordered caps - in reference file they are ordered inconsistently
                        //if (!reference.equals(in_dictionary)){
                        if (reference.compareToIgnoreCase(in_dictionary) != 0){
                            System.out.print("Error");
                            System.out.println(" -> Correct:" + reference + " - Incorrect:" + in_dictionary + " index: " + line);
                            test1 = false;
                        }
                    }
                    if (test1) System.out.println("\nTEST 1: Completed successfully");
                    else System.out.println("\nTEST 1: Not completed successfully, check the code to see where the mistake is");

                    break;
                case "2":

                    // Check original user input
                    boolean test2 = true;
                    System.out.println("TEST 2: Checking whether the arguments (excluding -1) you inputted are correctly positioned in the dictionary");

                    String[][] in_dictionary_test2 = search_word_or_number(args, dictionary);

                    LinkedList<String> reference_list_test2 = new LinkedList<String>();
                    while(scan_reference.hasNext()){
                        reference_list_test2.add(scan_reference.next());
                    }
                    String[][] reference_test2 = search_word_or_number(args, reference_list_test2);

                    System.out.print("\nArguments: ");
                    for (String arg: args){
                        System.out.print(arg + "  ");
                    }
                    System.out.println("\n");

                    for (int i=0; i<args.length ; i++){
                        // To allow for incorrectly ordered caps - in reference file they are ordered inconsistently
                        if (!reference_test2[i][1].equals(in_dictionary_test2[i][1]) || !reference_test2[i][0].equals(in_dictionary_test2[i][0]) ){
                        //if (reference[i][1].compareToIgnoreCase(in_dictionary[i][1]) != 0){
                            System.out.print("Error");
                            System.out.println(" -> Correct: " + reference_test2[i][0] + " - " + reference_test2[i][1] + " Incorrect: " + in_dictionary_test2[i][0] + " - " +  in_dictionary_test2[i][1]);
                            test2 = false;
                        }
                        else{
                            System.out.println(reference_test2[i][0] + " - " + reference_test2[i][1] + " is correctly sorted");
                        }
                    }

                    if (test2) System.out.println("\nTEST 2: Completed successfully");
                    else System.out.println("\nTEST 2: Not completed successfully, check the code to see where the mistake is");

                    break;
                case "3":

                    // Check specific values the user is interested in
                    boolean test3 = true;
                    System.out.println("TEST 3: Please input some additional specific words or indexes separated by whitespaces that you want checked");
                    System.out.println("Type '-1' to skip this test");

                    user.nextLine();
                    String[] input = user.nextLine().split(" ");

                    String[][] in_dictionary_test3 = search_word_or_number(input, dictionary);

                    LinkedList<String> reference_list_test3 = new LinkedList<String>();
                    while(scan_reference.hasNext()){
                        reference_list_test3.add(scan_reference.next());
                    }
                    String[][] reference_test3 = search_word_or_number(input, reference_list_test3);

                    System.out.print("\nArguments: ");
                    for (String arg: input){
                        System.out.print(arg + "  ");
                    }
                    System.out.println("\n");

                    for (int i=0; i<input.length ; i++){
                        // To allow for incorrectly ordered caps - in reference file they are ordered inconsistently
                        if (!reference_test3[i][1].equals(in_dictionary_test3[i][1])){
                            //if (reference[i][1].compareToIgnoreCase(in_dictionary[i][1]) != 0){
                            System.out.print("Error");
                            System.out.println(" -> Correct: " + reference_test3[i][0] + " - " + reference_test3[i][1] + " Incorrect: " + in_dictionary_test3[i][0] + " - " +  in_dictionary_test3[i][1]);
                            test3 = false;
                        }
                        else{
                            System.out.println(reference_test3[i][0] + " - " + reference_test3[i][1] + " is correctly sorted");
                        }
                    }

                    if (test3) System.out.println("\nTEST 3: Completed successfully");
                    else System.out.println("\nTEST 3: Not completed successfully, check the code to see where the mistake is");

                    break;
                case "4":

                    // Check random values with the user inputting the number to search
                    boolean test4 = true;
                    System.out.println("TEST 4: Please input a number of random words you want to be checked.");
                    System.out.println("Type '-1' to skip this test");
                    String number_input = user.next();
                    String regex = "\\d+";
                    int number = 0;
                    if (number_input.matches(regex)) number = Integer.parseInt(number_input);
                    else{
                        System.out.println("Please introduce a positive number");
                        continue;
                    }

                    LinkedList<String> reference_list_test4 = new LinkedList<String>();
                    while(scan_reference.hasNext()){
                        reference_list_test4.add(scan_reference.next());
                    }

                    Random random = new Random();
                    String[] to_check = new String[number];
                    for(int i=0; i<number; i++){
                        String index = Integer.toString(random.nextInt(reference_list_test4.size()));
                        to_check[i] = index;
                    }

                    String[][] in_dictionary_test4 = search_word_or_number(to_check, dictionary);
                    String[][] reference_test4 = search_word_or_number(to_check, reference_list_test4);

                    System.out.print("\nArguments: ");
                    for (String arg: to_check){
                        System.out.print(arg + "  ");
                    }
                    System.out.println("\n");

                    for (int i=0; i<to_check.length ; i++){
                        // To allow for incorrectly ordered caps - in reference file they are ordered inconsistently
                        if (!reference_test4[i][1].equals(in_dictionary_test4[i][1])){
                            //if (reference[i][1].compareToIgnoreCase(in_dictionary[i][1]) != 0){
                            System.out.print("Error");
                            System.out.println(" -> Correct: " + reference_test4[i][0] + " - " + reference_test4[i][1] + " Incorrect: " + in_dictionary_test4[i][0] + " - " +  in_dictionary_test4[i][1]);
                            test4 = false;
                        }
                        else{
                            System.out.println(reference_test4[i][0] + " - " + reference_test4[i][1] + " is correctly sorted");
                        }
                    }


                    if (test4) System.out.println("\nTEST 4: Completed successfully");
                    else System.out.println("\nTEST 4: Not completed successfully, check the code to see where the mistake is");

                    break;
                case "5":
                    System.out.println("Test execution time:");
                    dictionary = new SkipList();
                    long start1 = System.currentTimeMillis();
                    // reading input from the file vs just surrounding the algorithm with timers is not noticeabl
                    read_file(root, "unsorteddict");
                    //write_to_file(root, "sorteddict"); // ads an average of 10 miliseconds
                    long time1 = System.currentTimeMillis() - start1;
                    System.out.println("In sorting 100,000 words: " + time1 + " miliseconds");

                    dictionary = new SkipList();
                    long start2 = System.currentTimeMillis();
                    // reading input from the file vs just surrounding the algorithm with timers is not noticeable
                    read_file(root, "unsortedDictTest");
                    //write_to_file(root, "sorted_to_test"); // ads an average of 10 miliseconds
                    long time2 = System.currentTimeMillis() - start2;
                    System.out.println("In sorting 10,000 words: " + time2 + " miliseconds");
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
        System.out.println("******************");
        System.out.println("***********");
        System.out.println("*****");
        System.out.println("***");
    }

    public void read_file(Path root, String file_name) throws FileNotFoundException {

        String dictionary_path = root + "/Files/" + file_name + ".txt";

        File f = new File(dictionary_path);
        Scanner scan = new Scanner(f);

        while(scan.hasNextLine()){
            String word = scan.nextLine();
            sorted_insert(word);
        }

    }


    public void sorted_insert(String word){
        dictionary.insert(word);
    }

    private void write_to_file (Path root, String file_name) throws IOException {

        PrintWriter outputStream = new PrintWriter(root + "/Files/" + file_name + ".txt", "UTF-8");
        while(dictionary.hasNext()){
            outputStream.println(dictionary.next().value);
        }
        outputStream.close();
    }

    public String[][] search_word_or_number (String[] input, SkipList dictionary) {

        int max_size = 10;
        for (String arg : args) {
            if (arg.equals("-1")) {
                max_size = 9;
            }
        }

        int size = input.length;
        if (size > max_size) {
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

        int max_size = 10;
        for (String arg : args) {
            if (arg.equals("-1")) {
                max_size = 9;
            }
        }

        int size = input.length;
        if (size > max_size) {
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