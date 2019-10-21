package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Collator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

    // Get absolute path to unsorted dictionary
	Path root = Paths.get(".").normalize().toAbsolutePath();
	String dictionary_path = root + "/unsorteddict.txt";

	File f = new File(dictionary_path);
	Scanner scan = new Scanner(f);

	LinkedList<String> ordered_dictionary = new LinkedList<String>();

	while(scan.hasNextLine()){
	    String word = scan.nextLine();
            if (ordered_dictionary.size() == 0){
                ordered_dictionary.add(word);
            }else{
                Iterator<String> iterator = ordered_dictionary.iterator();
                while (iterator.hasNext()){
                    String temp = iterator.next();
                    Collator usCollator = Collator.getInstance(Locale.US);
                    usCollator.setStrength(Collator.PRIMARY);
                    if( usCollator.compare(word, temp) > 0 ) {
                        continue;
                    }else{
                        int pos = ordered_dictionary.indexOf(temp);
                        ordered_dictionary.add(pos, word);
                    }
                }
            }
        }

        System.out.println(ordered_dictionary);


    }
}
