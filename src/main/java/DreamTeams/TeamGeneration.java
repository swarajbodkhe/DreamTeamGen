package DreamTeams;

import com.opencsv.CSVWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.ListModel;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author 91935
 */
public class TeamGeneration {

    static long combinationCount = 0;
    static List<List<String[]>> TeamCombinations = new ArrayList<>();

    public static void Generate1614Combination(int Team1PlayersTobeSelected, int Team2PlayerToSelected, int NoWK, int NoBat, int NoAR, int NoBowl,boolean ByCredits) throws Exception {
        HashMap<String, Double> WKs = new HashMap<>();
        HashMap<String, Double> BATs = new HashMap<>();
        HashMap<String, Double> ARs = new HashMap<>();
        HashMap<String, Double> BOWLs = new HashMap<>();

        BufferedReader br = new BufferedReader(new FileReader("output2.csv"));
        String line = null;
        List<String> lst = new ArrayList<>();
        HashMap<String, String> map = new HashMap<String, String>();
        lst.add("WK");
        lst.add("BAT");
        lst.add("AR");
        lst.add("BOWL");

        //get wks of both teams in list
        List<String[]> Team1 = new ArrayList<>();
        List<String[]> Team2 = new ArrayList<>();
        String Team1Name = "";
        int wk = 0, bat = 0, ar = 0, bowl = 0;

        while ((line = br.readLine()) != null) {
            //String str[] = line.split(",");
            String arr[] = line.split(",");
            if (Team1Name.equals("")) {
                Team1Name = arr[0];

            }
            if (Team1Name.equals(arr[0])) {
                Team1.add(arr);
                if (arr[3].equals("WK")) {
                    wk = +1;
                }
                if (arr[3].equals("BAT")) {
                    bat = +1;
                }
                if (arr[3].equals("AR")) {
                    ar = +1;
                }
                if (arr[3].equals("BOWL")) {
                    bowl = +1;
                }
            } else {
                Team2.add(arr);
                if (arr[3].equals("WK")) {
                    wk = +1;
                }
                if (arr[3].equals("BAT")) {
                    bat = +1;
                }
                if (arr[3].equals("AR")) {
                    ar = +1;
                }
                if (arr[3].equals("BOWL")) {
                    bowl = +1;
                }
            }
        }
        List<List<String[]>> CreatedTeams = new ArrayList<>();
        boolean bwk = false, bbat = false, bar = false, bbowl = false;
        List<String[]> wklist = new ArrayList<>();
        List<String[]> batList = new ArrayList<>();
        List<String[]> arList = new ArrayList<>();
        List<String[]> bowlList = new ArrayList<>();
        boolean bComplete = false;
        String[][] Team1Array = convertListToArray(Team1);
        printCombination(Team1Array, Team1Array.length, Team1PlayersTobeSelected);
//       List<List<String[]>> AllTeam1_7Combinations=new ArrayList<>();
        List<List<String[]>> AllTeam1_7Combinations = ((List) ((ArrayList) TeamCombinations).clone());
        TeamCombinations.clear();
        String[][] Team2Array = convertListToArray(Team2);
        printCombination(Team2Array, Team2Array.length, Team2PlayerToSelected);
        List<List<String[]>> AllTeam2_4Combinations = ((List) ((ArrayList) TeamCombinations).clone());
        List<List<String[]>> combinedTeamCombinations = CombineTeams(AllTeam1_7Combinations, AllTeam2_4Combinations, Team1PlayersTobeSelected, Team2PlayerToSelected, NoWK, NoBat, NoAR, NoBowl);
        List<List<String[]>> SortedTeamCombinations = null;
        if(!ByCredits){
            SortedTeamCombinations = SortCombinedTeams(combinedTeamCombinations);
        }else{
            SortedTeamCombinations = SortCombinedTeamsByCredits(combinedTeamCombinations);
        }

        String DirName = "T1_" + String.valueOf(Team1PlayersTobeSelected) + "_T2_" + String.valueOf(Team2PlayerToSelected) + "Players_" + String.valueOf(NoWK) + "_" + String.valueOf(NoBat) + "_" + String.valueOf(NoAR) + "_" + String.valueOf(NoBowl) + "_combination";
        File index = new File(DirName);
        if (!index.exists()) {
            index.mkdir();
        } else {
            String[] entries = index.list();
            for (String s : entries) {
                File currentFile = new File(index.getPath(), s);
                currentFile.delete();
            }
            if (!index.exists()) {
                index.mkdir();
            }
        }

        try {
            for (int i = 0; i < SortedTeamCombinations.size(); i++) {
                CSVWriter writer = new CSVWriter(new FileWriter(DirName + "/Team" + String.valueOf(i) + ".csv"), ',', CSVWriter.NO_QUOTE_CHARACTER);
                writer.writeAll(SortedTeamCombinations.get(i));
                writer.flush();
            }
            createCombinedTeamWithCaptainVC(DirName);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    static List<List<String[]>> SortCombinedTeamsByCredits(List<List<String[]>> combinedTeamCombinations) {
        List<List<String[]>> sortedCombinedTeams = new ArrayList<>();

        double highestCredits = 0;
        List<String[]> HighestCreditsTeam = new ArrayList<>();
        int index = 0;
        int actualLocation = 0;
        int Max_Limit = combinedTeamCombinations.size();
        if (Max_Limit >= 150) {
            Max_Limit = 150;
        }
        for (int i = 0; i < Max_Limit; i++) {
            for (List<String[]> t : combinedTeamCombinations) {
                double credits = 0;
                for (String[] player : t) {
                    credits = credits + Double.parseDouble(player[2]);
                }
                if (credits > highestCredits) {
                    highestCredits = credits;
                    HighestCreditsTeam = ((List) ((ArrayList) t).clone());
                    actualLocation = index;
                }
                index++;
            }
            index = 0;
            sortedCombinedTeams.add(HighestCreditsTeam);
            combinedTeamCombinations.remove(actualLocation);
            highestCredits = 0;
        }
        return sortedCombinedTeams;
    }

    static List<List<String[]>> SortCombinedTeams(List<List<String[]>> combinedTeamCombinations) {
        List<List<String[]>> sortedCombinedTeams = new ArrayList<>();

        double highestPoints = 0;
        List<String[]> HighestPointsTeam = new ArrayList<>();
        int index = 0;
        int actualLocation = 0;
        int Max_Limit = combinedTeamCombinations.size();
        if (Max_Limit >= 150) {
            Max_Limit = 150;
        }
        for (int i = 0; i < Max_Limit; i++) {
            for (List<String[]> t : combinedTeamCombinations) {
                double points = 0;
                for (String[] player : t) {
                    points = points + Double.parseDouble(player[4]);
                }
                if (points > highestPoints) {
                    highestPoints = points;
                    HighestPointsTeam = ((List) ((ArrayList) t).clone());
                    actualLocation = index;
                }
                index++;
            }
            index = 0;
            sortedCombinedTeams.add(HighestPointsTeam);
            combinedTeamCombinations.remove(actualLocation);
            highestPoints = 0;
        }
        return sortedCombinedTeams;
    }
    
    
    
    
    static List<List<String[]>> CombineTeams(List<List<String[]>> Team1, List<List<String[]>> Team2, int Team1PlayersTobeSelected, int Team2PlayerToSelected, int NoWK, int NoBat, int NoAR, int NoBowl) {
        List<List<String[]>> NewTeam = new ArrayList<>();
        int lessthan95 = 0, lessthan90 = 0, equalto100 = 0, greaterThan95 = 0;
        for (List<String[]> TeamOfTeam1 : Team1) {
            List<String[]> TempInnerTeam = new ArrayList<>();
            List<String[]> Temp2InnerTeam = new ArrayList<>();
            for (int i = 0; i < TeamOfTeam1.size(); i++) {
                TempInnerTeam.add(TeamOfTeam1.get(i));
                Temp2InnerTeam.add(TeamOfTeam1.get(i));
            }
            for (List<String[]> TeamOfTeam2 : Team2) {
                for (int cnt = 0; cnt < TeamOfTeam2.size(); cnt++) {
                    TempInnerTeam.add(TeamOfTeam2.get(cnt));
                }
                List<String[]> NewInnerTeam = ((List) ((ArrayList) TempInnerTeam).clone());

                int wk = 0, bat = 0, ar = 0, bowl = 0, t1 = 0, t2 = 0;
                String Team1Name = "", Team2Name = "";
                double credit = 0.0;
                for (String[] player : NewInnerTeam) {
                    credit = credit + Double.parseDouble(player[2]);
                    if (credit > 100) {
                        break;
                    }
                    if (t1 == 0 && t2 == 0) {
                        Team1Name = player[0];
                        t1++;
                    } else {
                        if (player[0].equals(Team1Name)) {
                            t1++;
                        } else {
                            t2++;
                        }
                    }

                    if (player[3].equals("WK")) {
                        wk++;
                    }
                    if (player[3].equals("BAT")) {
                        bat++;
                    }
                    if (player[3].equals("AR")) {
                        ar++;
                    }
                    if (player[3].equals("BOWL")) {
                        bowl++;
                    }
                }

                if (t1 == Team1PlayersTobeSelected && t2 == Team2PlayerToSelected) {
                    //Combination of 2-3-3-3 and credit;
                    if (credit <= 100) {
                        if (wk == NoWK && bat == NoBat && ar == NoAR && bowl == NoBowl) {
                            if ((credit < 95)) {
                                lessthan95++;
                            }
                            if ((credit < 90)) {
                                lessthan90++;
                            }
                            if ((credit >= 95)) {
                                greaterThan95++;
                            }
                            if ((credit == 100)) {
                                equalto100++;
                            }
                            NewTeam.add(NewInnerTeam);
                        }
                    }
                }
                TempInnerTeam = ((List) ((ArrayList) Temp2InnerTeam).clone());
            }
        }
        return NewTeam;
    }

    static void printCombination(String[][] arr, int n, int r) {
        // A temporary array to store all combination one by one
        String data[][] = new String[r][];
        for (int i = 0; i < r; i++) {
            data[i] = new String[6];
        }

        // Print all combination using temprary array 'data[]'
        combinationUtil(arr, data, 0, n - 1, 0, r);
    }

    static void combinationUtil(String arr[][], String data[][], int start,
            int end, int index, int r) {
        combinationCount++;
        // Current combination is ready to be printed, print it
        if (index == r) {
            List<String[]> FilteredPlayersList = new ArrayList<>();
            for (int j = 0; j < r; j++) {
                String[] Player = new String[6];
                for (int k = 0; k < 6; k++) {
                    System.out.print(data[j][k] + " ");
                    Player[k] = data[j][k];
                }
                FilteredPlayersList.add(Player);
            }
            TeamCombinations.add(FilteredPlayersList);
            System.out.println("");
            return;
        }

        // replace index with all possible elements. The condition
        // "end-i+1 >= r-index" makes sure that including one element
        // at index will make a combination with remaining elements
        // at remaining positions
        for (int i = start; i <= end && end - i + 1 >= r - index; i++) {
            for (int j = 0; j < 6; j++) {
                data[index][j] = arr[i][j];
            }
            combinationUtil(arr, data, i + 1, end, index + 1, r);
        }
    }

    private static String[][] convertListToArray(List<String[]> lst) {
        String arr[][] = new String[lst.size()][];
        for (int i = 0; i < lst.size(); i++) {
            arr[i] = new String[6];
        }

        for (int i = 0; i < lst.size(); i++) {
            for (int j = 0; j < lst.get(0).length; j++) {
                arr[i][j] = lst.get(i)[j];
            }

        }

        return arr;

    }

    private static void createCombinedTeamWithCaptainVC(String sFolderName) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(new File("output2.csv")));
        int MyPlayerTeams = 1;
        List<String> myEntries = new ArrayList<>();
        String line = null;
        while ((line = br.readLine()) != null) {
            myEntries.add(line.split(",")[1]);
        }
        br.close();
        File index = new File("C_VC_Teams");
        if (!index.exists()) {
            index.mkdir();
        } else {
            String[] entries = index.list();
            for (String s : entries) {
                File currentFile = new File(index.getPath(), s);
                currentFile.delete();
            }
            if (!index.exists()) {
                index.mkdir();
            }
        }

        Path rootPath = Paths.get("C_VC_Teams");
        try {
            Files.walk(rootPath, FileVisitOption.FOLLOW_LINKS)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .peek(System.out::println)
                    .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
        index = new File("C_VC_Teams");
        if (!index.exists()) {
            index.mkdir();
        }
        int createdMyTeam = 0;
        Set<String> CaptainSet = new HashSet<>();
        for (int i = 0; i < myEntries.size() - 1; i++) {

            String Captain = null;
            if (!CaptainSet.contains(myEntries.get(i))) {
                Captain = myEntries.get(i);
                CaptainSet.add(Captain);
            } else {
                continue;
            }
            for (int j = 0; j < myEntries.size() - 1; j++) {
                if (myEntries.get(i).equals(myEntries.get(j))) {
                    continue;
                }
                String VC = myEntries.get(j);

                boolean cFound = false;
                boolean vcFound = false;
                int Max_Limit = myEntries.size();
                if (Max_Limit >= 150) {
                    Max_Limit = 150;
                }
                for (int k = 0; k < Max_Limit; k++) {
                    br = new BufferedReader(new FileReader(new File(sFolderName + "/Team" + String.valueOf(k) + ".csv")));
                    List<String[]> Team = new ArrayList<>();
                    line = null;
                    while ((line = br.readLine()) != null) {
                        Team.add(line.split(","));
                    }
                    br.close();

                    for (String[] arr : Team) {
                        if (arr[1].equals(Captain)) {
                            cFound = true;
                        }
                        if (arr[1].equals(VC)) {
                            vcFound = true;
                        }
                        if (cFound && vcFound) {
                            break;
                        }
                    }

                    if (cFound && vcFound) {
                        Team.add(new String[]{Captain});
                        Team.add(new String[]{VC});

                        CSVWriter writer;
                        try {
                            if (createdMyTeam % 11 == 0 && createdMyTeam > 0) {
                                MyPlayerTeams++;
                            }
                            index=new File("C_VC_Teams/"+ String.valueOf(MyPlayerTeams));
                            if (!index.exists()) {
                                index.mkdir();
                            }
                            writer = new CSVWriter(new FileWriter("C_VC_Teams/" + String.valueOf(MyPlayerTeams) + "/Team" + String.valueOf(createdMyTeam) + ".csv"), ',', CSVWriter.NO_QUOTE_CHARACTER);
                            createdMyTeam++;
                            writer.writeAll(Team);
                            writer.flush();
                            cFound = false;
                            vcFound = false;
                            break;

                        } catch (Exception ex) {

                        }
                    }
                    cFound = false;
                    vcFound = false;
                }
            }
        }

    }

}
