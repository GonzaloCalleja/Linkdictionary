# Linkdictionary

## Instructions 

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

* The program uses the concept of SkipList instead of LinkedList for performance purposes. (This concept is a type of LinkedList that will be explained in a follwing section)

  EXPLANATION: The requirements ask for the dictionary's data structure to be a LinkedList but due to this objects structure this requirement limits the maximum speed the program can execute in, to aproximatelly 30 seconds for 100,000 lines with 1 LinkedList and a relatively efficient insert and ListIterator objects to read and write to the LinkList. The execution time is so slow because using algorithms that would have a O(Log(n)) time complexity in ArrayList and other data structures take O(n * Log(n)) time complexity in LinkedLists because to perform a "jump" in a LinkedList traversing through all previous nodes and takes O(N) time.

* The File structure:
    * Program Logic : ```src/com/company/SimpleSortedInsert.java```.
    * SkipList implementation: ```src/com/company/SkipList.java``` & ```src/com/company/SkipNode.java```.
    * Main Class to execute from IntelliJ: ```src/com/company/Main.java```.
    * Main Class to execute from command prompt ```src/LinkedList.java```. (this is necessary because of intelliJ's structure)

### Possible Improvements:
* Write test results into a report file & timers into it
* Differentiate in between words with caps and no caps
* Find out if more efficiency is possible
    
## SkipList Explanation

The SkipList implementation in this code is based on the explanation of a SkipList in [this document](shorturl.at/fhwJ1). In it, SkipLists are defined as a _"Skip lists are a probabilistic alternative to balanced trees"_, this means that despite them having a bad worst case performance, since they balance probabilistically, it is very unlikely for it to happen.

The idea behind this list is to have the nodes in it have pointers to nodes further ahead, so that to insert a new value, there is no need to examine every node in the list, you can "skip" ahead in the least until you find the right position. The number of pointers a node has is chosen randomly depending on a probability p=0.25. This is because if for example every (2^i)th node had a pointer 2^i ahead, the number of nodes that must be examined can be reduced to log2 while only doubling the number of pointers. This data structure could be used for fast searching, but insertion and deletion would be impractical. So, defining a "level" as the number of foward pointers a node has, the solution to this is to randomize the level so that it maintains the proportions of 50% of the nodes being level 1, 25% of the nodes being level 2, 12,5% of the nodes being level 3 and so on. This way, insertions only require local modifications, since the level of a node once an insertion has been made does not need to change. Some arrangements of levels would give poor execution times, but we will see that such arrangements are rare.

The main difference with a Linked list is that the insertion method has to update all the pointers which should now connect to the new node. This depends on the new node's level, and it essentially involves creating an array with nodes that need to be updated, adding a node into it when you go down a level when examining the SkipList to find which possition will the new word be introduced into.

SkipList Visualization:

![](https://media.geeksforgeeks.org/wp-content/uploads/Skip-List-3-4.jpg)

### Program progress - improved search algorithm using timers

By placing timers, we noticed that there were two actions that slowed down the program: Firslty, accesses to the LinkedList, especially when not using iterators, so we eliminated as many of these as possible. Secondly, the insertion algorithm due to the fact that it had to be executed as many times as there are words, so we concentrated on making it as efficient as possible, here is how:
(time is calculated on executing the 100,000 word file)
(The variance in time is due to different execution tasks in different computers and because there were other processes running parallel to the program in the computers)

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

## Code description

Following is an explanation of the Classes used:

### ```Main``` and ```WordSorterCommandPrompt``` classes:

These are the two main classes, the Main class is for running the program in IntelliJ and the LinkDictionary class is for running it from the command prompt and accept arguments.
In both, the program is run when a SimpleSortedInsert object is instantiated, and the run() method is executed.


### ```WordSorterWithSkipList``` class:

As explained before, this class is the one that runs most of the program, it is in charge of reading the user input (including instantiating a TestForDictionarySorter object), reading the file given of unsorted words, instantiating the SkipList and inserting all the words into it (the insertion automatically sorts the words) and then of writing the SkipList into an output file.

#### Instance Variables: 

```java
    SkipList dictionary;
    private String[] userArgs;
    private int maxUserArgsAllowed = 10;
    Path projectFolderPath;
```
maxUserArgsAllowed = 10 -> Is to fulfill the requirement which states that the program only accept 10 arguments

#### Constructor methods

There are two constructors, one of them takes the user input as an argument and the other doesn't. In both of them _projectFolderPath_ is initialized.The only difference is that one of the constructors requires a String array as an argument for when the code is executed from the command line, and that string array is stored in the _userArgs_ variable.

The _projectFolderPath_ variable **finds the current absolute path** of the main class and then backtracks to find the Files forder accoding to the expected directory structure. Because of intelliJ's way of working, the absolute path obtained is not the same when executed from the terminal than when executed from IntelliJ, so we had to create a **conditional statement** to control for that.

#### ```run()``` method

The run is the basic structure of the program:
1. If '-1' is contained in the arguments, take it out of the array and execute the testdictionary() method. Since the array is now one item smaller the maxArgs is reduced to 9. This way the program detects 1 '-1' input and the rest just print out "Negative index invalid" - checkIfTestNecessaryAndPerform()
2. Given a file name, the unsorted file is read (and then sorted into a LinkedList called dictionary in another method) - readFileAndSortedInsertInSkipList()
3. Given an output file name, the output file is written with the sorted dictionary - writeListIntoFile()
4. If there are any arguments from the command line, they are taken into account in printResultsFromUserArguments() and the returned array is printed and the output is printed in the terminal - printResultsFromUserArguments()

#### Testing the Dictionary

This method instantiates a TestForDictionarySorter object and executes the performTests() method, to allow the user to test whether the SimpleSortedInsert class works as expected.

This method runs the program for the test file and offers the user 5 different tests to check whether the program works correctly.  
1) Test 1: Checks all words in the test file against the file generated by the program, it uses Scanners to read both files
2) Test 2: Checks the test file and the dictionary generated by the program using the original user input excluding the '-1', it reads  
           the test file into a LinkedList, performs a search_user_input on both it and the dictionary and checks if they are equal.
3) Test 3: Asks the user for some input and performs the same process as the Test 2
4) Test 4: Asks the user for a random number and checks that amount of random positions. It it reads the test file into a LinkedList and 
           then checks whether the words in those random indexes of the LinkedList are the same as the words in those positions in the  
           SkipList generated by the program
5) Test 5: Run the read_file method for the 10,000 word file and for the 100,000 word file and print out the execution times for both.

#### ```readFileAndSortedInsertInSkipList(String file_name)``` method

This method instantiates a SkipList into the instance variable called dictionary. Then it proceeeds to use a **Scanner** to read the file (with the name passed as an argument to the method) line by line, and execute the method ```dictionary.sortedInsert(String word)``` on every word so that it is inserted into is correct possition in the dictionary.

#### ```writeListIntoFile(String file_name)``` method
This method instantiates a PrintWriter object, prints all the contents of the dictionary into it and writes all the output into a file.

#### ```searchWordOrIndexInDictionary(String[] input, LinkedList<String> dictionary)``` method

