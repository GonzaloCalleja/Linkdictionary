package com.company;

//import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Scanner;

public class WordSorterWithSkipList {

    SkipList dictionary;
    private String[] userArgs;
    private int maxUserArgsAllowed = 10;
    Path projectFolderPath;

    public WordSorterWithSkipList(){
        projectFolderPath = Paths.get("").normalize().toAbsolutePath();

        if (projectFolderPath.endsWith("src"))
            projectFolderPath = projectFolderPath.getParent();
    }

    public WordSorterWithSkipList(String[] args){
        this();
        this.userArgs = args;
    }

    public void run() throws IOException {

        String fileNameUnsorted = "unsorteddict"; // OTHER FILES: "millionwords"
        String fileNameSorted = "sorteddict";

        checkIfTestNecessaryAndPerform();

        readFileAndSortedInsertInSkipList(fileNameUnsorted);
        writeListIntoFile(fileNameSorted);

        printResultsFromUserArguments();
    }

    private void printResultsFromUserArguments() {
        if(userArgs !=null && userArgs.length > 0)
            System.out.print("Arguments: ");

            for (String arg: userArgs)
                System.out.print(arg + "  ");

            System.out.println("\n");

            for (String[] element: searchWordOrIndexInDictionary(userArgs, this.dictionary))
                System.out.println(element[0] + " " + element[1]);
    }
    private void checkIfTestNecessaryAndPerform() throws IOException {
        if(userArgs !=null && userArgs.length > 0) {
            String[] temp = new String[userArgs.length -1];

            for (String arg : userArgs)
                if (arg.equals("-1")) {
                    int j = 0;

                    for (String argument: userArgs)
                        if (!argument.equals("-1"))
                            temp[j] = argument;
                            j++;

                    this.maxUserArgsAllowed = 9;
                    userArgs = temp;
                    TestForWordSorterWithSkipList tester = new TestForWordSorterWithSkipList();
                    tester.allowUserToPerformTests(userArgs);
                    break;
                }
        }
    }

    public void readFileAndSortedInsertInSkipList(String fileName) throws FileNotFoundException {

        dictionary = new SkipList();
        String dictionaryPath = projectFolderPath + "/" + fileName + ".txt";

        File f = new File(dictionaryPath);
        Scanner scan = new Scanner(f);

        while(scan.hasNextLine()){
            String word = scan.nextLine();
            dictionary.sortedInsert(word);
        }
    }

    public void writeListIntoFile(String file_name) throws IOException {

        PrintWriter outputStream = new PrintWriter(projectFolderPath +"/"+ file_name + ".txt", "UTF-8");

        while(dictionary.hasNext())
            outputStream.println(dictionary.next().value);

        outputStream.close();
    }

    public String[][] searchWordOrIndexInDictionary(String[] input, LinkedList<String> dictionary) {

        int inputSize = input.length;
        String regex = "\\d+";
        String[][] result = new String[inputSize][2];

        if (inputSize > maxUserArgsAllowed)

            System.out.println("The maximum number of inputs is 10 or 9 on top of '-1'");
            System.out.print("Inputs read: ");
            inputSize = 10;

            for (int i = 0; i < 10; i++)
                System.out.print(input[i] + " ");

            System.out.println();

        for (int i = 0; i < inputSize; i++)
            if(input[i].matches(regex)){
                int word_position = Integer.parseInt(input[i]);

                if (word_position < 0)
                    System.out.println("Negative index invalid");

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

                if (word_position != -1)
                    result[i][0] = Integer.toString(word_position + 1);

                else result[i][0] = "-1";

                result[i][1] = input[i];
            }

        return result;
    }
}