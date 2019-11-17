import com.company.WordSorterWithSkipList;

import java.io.*;

public class WordSorterCommandPromptMain {

    public static void main(String[] args){

        WordSorterWithSkipList app = new WordSorterWithSkipList(args);
        try {
            app.run();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}