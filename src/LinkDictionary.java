import com.company.SimpleSortedInsert;

import java.io.*;

public class LinkDictionary {

    public static void main(String[] args){

        SimpleSortedInsert app = new SimpleSortedInsert(args);
        try {
            app.run();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}