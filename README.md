# Linkdictionary

The important code in this program is in the src/com/company/SimpleSortedInsert.java, the other classes in the repository are to be able to execute it from IntelliJ (src/com/company/Main.java) and to be able to execute from the command prompt (src/LinkedList.java).

### Program explanation
This program reads an usorted file (Files/unsorteddict.txt) of words and uses an algorithm to sort these words into a LinkedList and then writes a file names "sorteddict.txt" in the Files directory. When executed from the command line, the program takes up to 10 arguments of type int or String.

If it is of type int, the program will print the word that has that index in the dictionary, starting at 1, and if it is a String the index of that word in the dictionary will be printed, or ```-1```.

### Code description

The SimpleSortedInsert class has three instance variables: root, input and dictionary. 
input and dictionary are initialized in the two constructor methods, the only difference is that one of the constructors requires a String array as an argument for when the code is executed from the command line.


The run method allows you to see the basic structure of the program and easily understand it. Firstly, the unsorted file is read (and then sorted into a LinkedList called dictionary in another method). Secondly, an output file is written with the sorted dictionary, and finally, if there are any arguments from the command line, they are taken into account in search_word_or_number() and printed and the output is printed in the terminal.

```java
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
 ```
#### read_file() method
This method simply finds the current absolute path of the main class and then backtracks to find the Files forder accoding to the expected directory structure. Because of intelliJ's way of working, the absolute path obtained is not the same when executed from teh terminal than when executed from IntelliJ, so we had to create a conditional statement to control for that.
Then this method uses a Scanner to read the file line by line, and execute the method '''sorted_insert(String word)''' on every word so that it is inserted into is correct possition in the dictionary.
In this method, there is some commented code that allowed us to measure how long the program takes to order the array.

##### sorted_insert(String word) method
Since we wanted to use one single LinkedList to store all the words, the insertion algorithm needed to be relatively fast. To do this we thought of the following:
1) If the word is smaller than the first word, directly place it the first.
2) If the word is bigger than the last word, directly place it the last.
3) If not compare it with all the words in the dictionary one by one an place it infront of the first that is larger than it

Whether a word is smaller or larger is obtained using the compareToIgnoreCase() method of the String class.

However the 

#### write_to_file() method
#### search_word_or_number() method
