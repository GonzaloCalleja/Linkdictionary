# Linkdictionary

### Instructions 
- Create a JAVA project that creates a sorted dictionary of words using linked lists and that can be executed from the command prompt accepting arguments, one of which will trigger a test using a previously sorted file as a reference.

* **Functionalities** :
  * The application opens and reads line by line a .txt file.
  * Each line read is inserted into the dictionary in it's correct possition 
   * accounting uniformly for Caps instead of ignoring them
   * ignoring diacritics when sorting
  * After the file has been fully read, the application will create another file in which it will introduce line by line the dictionary created
  * When executed from the command line, the program accepts 10 arguments:
   * Integers as arguments will output the word corresponding to that index
   * Strings as arguments will output the index corresponding to that word
   * '-1' as an argument will begin a testing process (once it is finalized, the program will resume as normal)
      "test" is not used because it can be a word in the dictionary, and so it is a valid argument

* **Program Details** :
* The program uses the concept of SkipList instead of LinkedList for performance purposes. (This concept will be explained in a follwing section

  EXPLANATION: The requirements ask for the dictionary's data structure to be a LinkedList but due to this objects structure this requirement limits the maximum speed the program can execute in, to aproximatelly 30 seconds for 100,000 lines with 1 LinkedList and a relatively efficient insert and ListIterator objects to read and write to the LinkList. The execution time is so slow because using algorithms that would have a Log(n) execution time in ArrayList and other data structures take n * Log(n) execution time in LinkedLists because to perform a "jump" in a LinkedList involves iterating through all previous nodes.

* The File structure:
    * Program Logic : ```src/com/company/SimpleSortedInsert.java```.
    * SkipList implementation: ```src/com/company/SkipList.java``` & ```src/com/company/SkipNode.java```.
    * Main Class to execute from IntelliJ: ```src/com/company/Main.java```.
    * Main Class to execute from command prompt ```src/LinkedList.java```. (this is necessary because of intelliJ's structure)

### Possible Improvements:
* Place the test() method into a different class and execute it
* Write test results into a report file & timers into it
* Differentiate in between words with caps and no caps & ignore diacritics without eliminating them
* Change SimpleSortedDictionary.java name
* Add the possibility of testing with IntelliJ execution
* Have SkipList Class extend LinkedList
* Reduce the 2 search_user_input() methods into one efficient method
* Find out if more efficiency is possible
    
### SkipList Explanation

A list where each node contains an array of nodes that allow it to skip many nodes. Better than Binary Trees.

### Program explanation
This program reads an unsorted file (Files/unsorteddict.txt) of words and uses an algorithm to sort these words into a LinkedList and then writes a file names _"sorteddict.txt"_ in the Files directory. When executed from the command line, the program takes up to 10 arguments of type int or String.

If it is of type int, the program will print the word that has that index in the dictionary, starting at 1, and if it is a String the index of that word in the dictionary will be printed, or ```-1```.

### Code description

The SimpleSortedInsert class has three instance variables: 
+ root
+ input
+ dictionary

_input_ and _dictionary_ are initialized in the two constructor methods, the only difference is that one of the constructors requires a String array as an argument for when the code is executed from the command line.


The run method allows you to see the basic structure of the program and easily understand it:
1. The unsorted file is read (and then sorted into a LinkedList called dictionary in another method). 
2. An output file is written with the sorted dictionary.
3. If there are any arguments from the command line, they are taken into account in search_word_or_number() and printed and the output is printed in the terminal.

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
#### ```read_file()``` method
This method simply **finds the current absolute path** of the main class and then backtracks to find the Files forder accoding to the expected directory structure. 

Because of intelliJ's way of working, the absolute path obtained is not the same when executed from the terminal than when executed from IntelliJ, so we had to create a **conditional statement** to control for that.

Then this method uses a **Scanner** to read the file line by line, and execute the method ```sorted_insert(String word)``` on every word so that it is inserted into is correct possition in the dictionary.

In this method, there is some **commented code** that allowed us to measure how long the program takes to order the array.

##### ```sorted_insert(String word)``` method
Since we wanted to use one single LinkedList to store all the words, the insertion algorithm needed to be relatively fast. To do this we thought of the following:
1) If the word is smaller than the first word, directly place it the first.
2) If the word is bigger than the last word, directly place it the last.
3) If not compare it with all the words in the dictionary one by one an place it infront of the first that is larger than it

Whether a word is smaller or larger is obtained using the ```compareToIgnoreCase()``` method of the String class.



#### ```write_to_file()``` method
#### ```search_word_or_number()``` method
