# Linkdictionary

### Instructions 
- Create a JAVA project that creates a sorted dictionary of words using linked lists and that can be executed from the command prompt accepting arguments, one of which will trigger a test using a previously sorted file as a reference.

* **Functionalities** :
  * The application opens and reads line by line a .txt file.
  * Each line read is inserted into the dictionary in it's correct possition 
   * Accounting uniformly for Caps instead of ignoring them
   * Ignoring diacritics when sorting
  * After the file has been fully read, the application will create another file in which it will introduce line by line the dictionary created
  * When executed from the command line, the program accepts 10 arguments:
   * Integers as arguments will output the word corresponding to that index
   * Strings as arguments will output the index corresponding to that word
   * '-1' as an argument will begin a testing process (once it is finalized, the program will resume as normal)
      "test" is not used because it can be a word in the dictionary, and so it is a valid argument

* **Program Details** :
* The program uses the concept of SkipList instead of LinkedList for performance purposes. (This concept will be explained in a follwing section

  EXPLANATION: The requirements ask for the dictionary's data structure to be a LinkedList but due to this objects structure this requirement limits the maximum speed the program can execute in, to aproximatelly 30 seconds for 100,000 lines with 1 LinkedList and a relatively efficient insert and ListIterator objects to read and write to the LinkList. The execution time is so slow because using algorithms that would have a O(Log(n)) time complexity in ArrayList and other data structures take O(n * Log(n)) time complexity in LinkedLists because to perform a "jump" in a LinkedList traversing through all previous nodes and takes O(N) time.

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

The SkipList implementation in this code is based on the explanation of a SkipList in [this](shorturl.at/fhwJ1) document. In it, SkipLists are defined as a _"Skip lists are a probabilistic alternative to balanced trees"_, this means that despite them having a bad worst case performance, since they balance probabilistically, it is very unlikely for it to happen.

The idea behind this list is to have the nodes in it have pointers to nodes further ahead, so that to insert a new value, there is no need to examine every node in the list, you can "skip" ahead in the least until you find the right position. The number of pointers a node has is chosen randomly depending on a probability p=0.25. This is because if for example every (2^i)th node had a pointer 2^i ahead, the number of nodes that must be examined can be reduced to log2 while only doubling the number of pointers. This data structure could be used for fast searching, but insertion and deletion would be impractical. So, defining a "level" as the number of foward pointers a node has, the solution to this is to randomize the level so that it maintains the proportions of 50% of the nodes being level 1, 25% of the nodes being level 2, 12,5% of the nodes being level 3 and so on. This way, insertions only require local modifications, since the level of a node once an insertion has been made does not need to change. Some arrangements of levels would give poor execution times, but we will see that such arrangements are rare.

SkipList Visualization:

![](https://media.geeksforgeeks.org/wp-content/uploads/Skip-List-3-4.jpg)

### Program progress - improved search algorithm using timers

By placing timers, we noticed that there were two actions that slowed down the program: Firslty, accesses to the LinkedList, especially when not using iterators, so we eliminated as many of these as possible. Secondly, the insertion algorithm due to the fact that it had to be executed as many times as there are words, so we concentrated on making it as efficient as possible, here is how:

1) Iteration 1: 100 - 120 secs:  
   Find if the word should be at the begining or the end of the LinkedList and insert it directly.  
   Iterate through LinkedList with a for loop from the first element onwards and insert it in the correct place.  
   
2) Iteration 2: 70 - 90 secs :  
   Find if the word should be at the end of the LinkedList and insert it directly.  
   Reduced number of times the dictionary is accessed in the program to a minimum.  
   Iterate through LinkedList with an iterator.  
   
3) Iteration 3: 40 - 50 secs : 
   Check the words distance from the bigining and the end of the LinkedList - to choose from where to begin iterating.  
   Iterate through LinkedList with an iterator in the direction chosen before.  
   Use iterator to insert into LinkedList.
   
4) Iteration 4: FAIL to complete because time over 200 secs :   
   Binary Search implemented on LinkedList -> Inefficient due to need to examine every previous node to perform a "jump".

5) Iteration 5: 0.294 - 0.400 secs
   SkipList implementd in program (the insert algorithm will be explained further down).  
   Using the insert method within the SkipList, there is no need for any additional operations.

### Code description

The program is run when a SimpleSortedInsert object is instantiated, and the run() method is executed.

The SimpleSortedInsert class has three instance variables: 
+ root
+ input
+ dictionary

_input_, _dictionary_ and _root_ are initialized in the two constructor methods, the only difference is that one of the constructors requires a String array as an argument for when the code is executed from the command line.


The run is the basic structure of the program:
1. If '-1' is contained in the arguments, take it out of the array and execute the testdictionary() method.
2. Given a file name, the unsorted file is read (and then sorted into a LinkedList called dictionary in another method). 
3. Given an output file name, the output file is written with the sorted dictionary.
4. If there are any arguments from the command line, they are taken into account in search_word_or_number() and the returned array is printed and the output is printed in the terminal.

Following is an explanation of the methods:

#### ```testDictionary()``` method

This method runs the program for the test file and offers the user 5 different tests to check whether the program works correctly.  
1) Test 1: Checks all words in the test file against the file generated by the program, it uses Scanners to read both files
2) Test 2: Checks the test file and the dictionary generated by the program using the original user input excluding the '-1', it reads  
           the test file into a LinkedList, performs a search_user_input on both it and the dictionary and checks if they are equal.
3) Test 3: Asks the user for some input and performs the same process as the Test 2
4) Test 4: Asks the user for a random number and checks that amount of random positions. It it reads the test file into a LinkedList and  
           then checks whether the words in those random indexes of the LinkedList are the same as the words in those positions in the  
           SkipList generated by the program

#### ```read_file()``` method
This method simply **finds the current absolute path** of the main class and then backtracks to find the Files forder accoding to the expected directory structure. 

Because of intelliJ's way of working, the absolute path obtained is not the same when executed from the terminal than when executed from IntelliJ, so we had to create a **conditional statement** to control for that.

Then this method uses a **Scanner** to read the file line by line, and execute the method ```sorted_insert(String word)``` on every word so that it is inserted into is correct possition in the dictionary.

In this method, there is some **commented code** that allowed us to measure how long the program takes to order the array.

##### ```sorted_insert(String word)``` method
Executes the insert() method in the SkipList();


#### ```write_to_file()``` method
#### ```search_word_or_number()``` method
