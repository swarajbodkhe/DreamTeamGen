/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DreamTeams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author 91935
 */
public class Credentials {

    public String[][] getCredentials() throws FileNotFoundException {
        
        File myObj = new File("credentials.csv");
        List<String[]> lines = new ArrayList<String[]>();
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            lines.add(data.split(","));
        }
        myReader.close();
        

// convert our list to a String array.
        String[][] array = new String[lines.size()][0];
        return lines.toArray(array);
    }
}
