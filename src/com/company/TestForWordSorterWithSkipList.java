package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class TestForWordSorterWithSkipList {

    WordSorterWithSkipList sorter;
    LinkedList<String> correct_dictionary;

    public TestForWordSorterWithSkipList() throws IOException {
        this("unsortedDictTest", "sortedDictTest", "sorted_to_test");
    }

    public TestForWordSorterWithSkipList(String unsortedDictTest_name, String sortedDictTest_name, String sorted_to_test_name) throws IOException {

        /*
        Scanner scan = new Scanner(System.in);
        System.out.println("Please indicate which files you want to use for the test:");
        System.out.println(" - input '1' to use the default tests ( 'sortedDictTest.txt' and 'unsortedDictTest.txt' ). - ");
        String input = scan.next();

         */

        this.sorter = new WordSorterWithSkipList();
        sorter.readFileAndSortedInsertInSkipList(unsortedDictTest_name);
        sorter.writeListIntoFile(sorted_to_test_name);

        this.correct_dictionary = new LinkedList<String>();
        Scanner read_file = new Scanner(new File(sorter.projectFolderPath + "/" + sortedDictTest_name + ".txt"));
        while(read_file.hasNext())
            this.correct_dictionary.add(read_file.nextLine());

    }

    public void allowUserToPerformTests(String[] args) throws FileNotFoundException {

        printStartTestModeMessage();

        boolean testing = true;
        Scanner user = new Scanner(System.in);
        while (testing) {

            printTestMenu();
            String test = user.next();

            switch (test) {
                case "1":
                    test1CompareAllWords();
                    break;
                case "2":
                    test2CompareWithPreviousUserInput(args);
                    break;
                case "3":
                    test3CompareWithNewUserInput(user);
                    break;
                case "4":
                    test4CompareRandomPositions(user);
                    break;
                case "5":
                    test5CompareExecutionTimesForFiles();
                    break;
                case "6":
                    test6AverageCompareExecutionTimes();
                    break;
                default:
                    System.out.println("Please input a number between 1 and 4.");
            }
        }
        user.close();
        printEndTestModeMessage();
    }

    private void printTestMenu() {
        System.out.println();
        System.out.println("There are 4 possible tests, press the number to indicate which one you want:");
        System.out.println("1) Check entire test file word by word");
        System.out.println("2) Check with all arguments you already inputted");
        System.out.println("3) Check with specific words or indexes you are interested in - user input wil be requested");
        System.out.println("4) Check random words - the number of words will be given by the user");
        System.out.println("5) Check program execution times with 10,000 words & 100,000 words");
        System.out.println("6) Check program execution times for 100 iterations of the program");
        System.out.println("  -- To exit & continue with the program type '-1' --");
    }

    private void printEndTestModeMessage() {
        System.out.println("TEST MODE FINISHED");
        System.out.println("The program will now execute as normal - sorting the full unsorted dictionary - without '-1' as an argument");
        System.out.println("*********************");
        System.out.println("******************");
        System.out.println("***********");
        System.out.println("*****");
        System.out.println("***");
    }
    private void printStartTestModeMessage() {
        System.out.println();
        System.out.println("***");
        System.out.println("*****");
        System.out.println("***********");
        System.out.println("******************");
        System.out.println("*********************");
        System.out.println("TEST MODE ACTIVATED");
    }

    private boolean compareGivenArguments(String[] args) {
        boolean test = true;
        String[][] in_dictionary_test2 = sorter.searchWordOrIndexInDictionary(args, this.sorter.dictionary);
        String[][] reference_test2 = sorter.searchWordOrIndexInDictionary(args, correct_dictionary);

        System.out.print("\nArguments: ");
        for (String arg: args)
            System.out.print(arg + "  ");

        System.out.println("\n");

        for (int i=0; i<args.length ; i++){
            // To allow for incorrectly ordered caps - in reference file they are ordered inconsistently
            if (!reference_test2[i][1].equals(in_dictionary_test2[i][1]) || !reference_test2[i][0].equals(in_dictionary_test2[i][0]) ){
                //if (reference[i][1].compareToIgnoreCase(in_dictionary[i][1]) != 0){
                System.out.print("Error");
                System.out.println(" -> Correct: " + reference_test2[i][0] + " - " + reference_test2[i][1] + " Incorrect: " + in_dictionary_test2[i][0] + " - " +  in_dictionary_test2[i][1]);
                test = false;
            }
            else{
                System.out.println(reference_test2[i][0] + " - " + reference_test2[i][1] + " is correctly sorted");
            }
        }
        return test;
    }

    private boolean test1CompareAllWords() {

        // Check 10,000 words of one dictionary to the other
        System.out.println("TEST 1: Checking all words in both files are equal...\n");
        boolean test = true;
        int line = 0;
        sorter.dictionary.resetIterator();

        ListIterator<String> correct_iterator = correct_dictionary.listIterator();

        while (true) {

            boolean correct_next = correct_iterator.hasNext();
            boolean to_test_next = sorter.dictionary.hasNext();

            if(!correct_next && !to_test_next) break;

            line++;
            String reference;
            if (!correct_next) {
                System.out.println("The dictionary created by the program has more words than the test dictionary.");
                System.out.println("Following are the extra words:");
                while(sorter.dictionary.hasNext()){
                    System.out.println(sorter.dictionary.next().value);
                }
                test = false;
                break;
            }

            String in_dictionary;
            if (!to_test_next) {
                System.out.println("The dictionary created by the program is shorter than the test dictionary.");
                System.out.println("Following are the missing words:");
                while(correct_iterator.hasNext()){
                    System.out.println(correct_iterator.next());
                }
                test = false;
                break;
            }

            in_dictionary = sorter.dictionary.next().value;
            reference = correct_iterator.next();

            // To allow for incorrectly ordered caps - in reference file they are ordered inconsistently
            if (!reference.toLowerCase().equals(in_dictionary.toLowerCase())){
                //if (reference.compareToIgnoreCase(in_dictionary) != 0) {
                System.out.print("Error");
                System.out.println(" -> Correct: " + reference + " - Incorrect: " + in_dictionary + " index: " + line);
                test = false;
            }
        }
        if (test) System.out.println("\nTEST 1: Completed successfully");
        else System.out.println("\nTEST 1: Not completed successfully, check the code to see where the mistake is");

        return test;
    }

    private boolean test2CompareWithPreviousUserInput(String[] args){

        // Check original user input
        System.out.println("TEST 2: Checking whether the arguments (excluding -1) you inputted are correctly positioned in the dictionary");

        boolean test = compareGivenArguments(args);

        if (test) System.out.println("\nTEST 2: Completed successfully");
        else System.out.println("\nTEST 2: Not completed successfully, check the code to see where the mistake is");

        return test;
    }

    private boolean test3CompareWithNewUserInput(Scanner user){
        // Check specific values the user is interested in
        System.out.println("TEST 3: Please input some additional specific words or indexes separated by whitespaces that you want checked");
        System.out.println("Type '-1' to skip this test");

        user.nextLine();
        String[] input = user.nextLine().split(" ");

        boolean test = compareGivenArguments(input);

        if (test) System.out.println("\nTEST 3: Completed successfully");
        else System.out.println("\nTEST 3: Not completed successfully, check the code to see where the mistake is");

        return test;
    }

    private boolean test4CompareRandomPositions(Scanner user) {
        // Check random values with the user inputting the number to search
        boolean test = true;
        System.out.println("TEST 4: Please input a number of random words you want to be checked.");
        System.out.println("Type '-1' to skip this test");
        String[] to_check;
        while(true) {
            String number_input = user.next();
            String regex = "\\d+";
            if (number_input.matches(regex)) {
                to_check = new String[Integer.parseInt(number_input)];
                break;
            }
            else System.out.println("Please introduce a positive number");
        }
        for(int i=0; i<to_check.length; i++){
            String index = Integer.toString((int) (Math.random() * correct_dictionary.size()));
            to_check[i] = index;
        }

        test = compareGivenArguments(to_check);
        if (test) System.out.println("\nTEST 4: Completed successfully");
        else System.out.println("\nTEST 4: Not completed successfully, check the code to see where the mistake is");

        return test;

    }

    // Change to accept file names
    private void test5CompareExecutionTimesForFiles() throws FileNotFoundException {
        System.out.println("TEST 5: Test execution time.");

        long start1 = System.currentTimeMillis();
        // reading input from the file vs just surrounding the algorithm with timers is not noticeabl
        sorter.readFileAndSortedInsertInSkipList("unsorteddict");
        //write_to_file("sorteddict"); // ads an average of 10 miliseconds
        long time1 = System.currentTimeMillis() - start1;
        System.out.println("In sorting 100,000 words: " + time1 + " miliseconds");

        long start2 = System.currentTimeMillis();
        // reading input from the file vs just surrounding the algorithm with timers is not noticeable
        sorter.readFileAndSortedInsertInSkipList("unsortedDictTest");
        //write_to_file("sorted_to_test"); // ads an average of 10 miliseconds
        long time2 = System.currentTimeMillis() - start2;
        System.out.println("In sorting 10,000 words: " + time2 + " miliseconds");
    }

    private void test6AverageCompareExecutionTimes() throws FileNotFoundException {
        System.out.println("TEST 5: Test execution time.");

        long average = 0;
        for(int i=0; i< 100; i++){
            long start1 = System.currentTimeMillis();
            sorter.readFileAndSortedInsertInSkipList("unsorteddict");
            long time1 = System.currentTimeMillis() - start1;
            average += time1;
        }
        average = average / 100;
        System.out.println("The average time of 100 tests for 100,000 words is: " + average + " miliseconds");

    }

    public static void main(String[] args){
        TestForWordSorterWithSkipList tester = null;
        try {
            tester = new TestForWordSorterWithSkipList();
            tester.allowUserToPerformTests(args);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}