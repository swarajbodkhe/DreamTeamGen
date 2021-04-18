package DreamTeams;


import Forms.TeamSelectionForm;
import com.opencsv.CSVWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 91935
 */
public class SeleniumTeamCreator extends Browser {
    
    
    
    public void getPlayersListFromSelectedTeam(String SelectedMatch,String Uname,String Password,String sCookie) throws Exception{
        WebDriver driver=null;
        try{
            driver=openDream11Page(null,Uname,Password,sCookie);
            List<WebElement> matches = driver
                    .findElements(By.xpath("//div[contains(@class,'matchMainContainer')]//a[contains(@class,'js--match-card')]"
                            + "/div[contains(@class,'card')]"));
            int upmatches=matches.size();
//            String[][] upcomingMatches=new String[upmatches][];
            List<String[]> upcomingMatches=new ArrayList<>();
            String selectedTeam1=SelectedMatch.split(" ")[0];
            String selectedTeam2=SelectedMatch.split(" ")[2];
            
            for(int i=1;i<=upmatches;i++)
            {
                List<WebElement> upmatchTeams= getWebElementsByXpath(driver, "(//div[contains(@class,'matchMainContainer')]//a[contains(@class,'js--match-card')]/div[contains(@class,'card')])["+String.valueOf(i)+"]//div[contains(@class,'squadShortName')]");
                if(upmatchTeams.get(0).getText().equals(selectedTeam1) && upmatchTeams.get(1).getText().equals(selectedTeam2)){
                    getElementByXpath(driver, "(//div[contains(@class,'matchMainContainer')]//a[contains(@class,'js--match-card')]/div[contains(@class,'card')])["+String.valueOf(i)+"]").click();
                    break;
                }
            }
        
//            WaitUntil(driver, 3, "//div[text()='Skip']/..").click();
        
            List<WebElement> createTeams = driver.findElements(By.xpath("//div[contains(text(),'Create Team')]"));
            if (createTeams.size() > 0) {
                createTeams.get(0).click();
            } else {
                getElementByXpath(driver, "//div[contains(text(),'My Teams')]").click();
                WaitUntil(driver, 5, "//span[contains(text(),'Create Team')]").click();
            }

            List<String[]> PlayerRatings = new ArrayList<>();

            List<WebElement> Players = getWebElementsByXpath(driver, "//div[contains(@class,'createTeamTeamSelectorPlayerList_')]/div/div[contains(@class,'Info')]");

            List<WebElement> Category = getWebElementsByXpath(driver, "//div[contains(@class,'createTeamTabsContainer')]//div[contains(@class,'createTeam')][1]");

            for (WebElement cat
                    : Category) {
                cat.click();
                Players = getWebElementsByXpath(driver, "//div[contains(@class,'createTeamTeamSelectorPlayerList_')]/div/div[contains(@class,'Info')]");
                //WebElement e=    getElementByXpath(driver, "//div[contains(@class,'playerListContainerWithoutOnBoardingWithBanner')]");
                JavascriptExecutor js = (JavascriptExecutor) driver;
                 js.executeScript("window.scrollBy(0,1000)");
                for (int i = 1; i <= Players.size(); i++) {
                    String xpName = "//div[contains(@class,'createTeamTeamSelectorPlayerList_')]/div[" + String.valueOf(i) + "]/div[contains(@class,'Info')]/div[contains(@class,'Name')]//div[contains(@class,'Name')]";
                    String xpCredits = "//div[contains(@class,'createTeamTeamSelectorPlayerList_')]/div[" + String.valueOf(i) + "]/div[contains(@class,'Info')]/div[contains(@class,'playerPoints')][2]";
                    String xpPoints = "//div[contains(@class,'createTeamTeamSelectorPlayerList_')]/div[" + String.valueOf(i) + "]/div[contains(@class,'Info')]/div[contains(@class,'playerPoints')][1]";
                    String squadName = "//div[contains(@class,'createTeamTeamSelectorPlayerList_')]/div[" + String.valueOf(i) + "]//div[contains(@class,'imageContainer')]//div[contains(@class,'squadName')]";
                    String xpStatusInfo = "//div[contains(@class,'createTeamTeamSelectorPlayerList_')]/div[" + String.valueOf(i) + "]/div[contains(@class,'Info')]//div[contains(@class,'statusInfo')]//div[contains(@style,'55')]";
                    String Name = getElementByXpath(driver, xpName).getText();
                    String Credit = getElementByXpath(driver, xpCredits).getText();
                    String Points = getElementByXpath(driver, xpPoints).getText();
                    String lastPlayed = getWebElementsByXpath(driver, xpStatusInfo).size() > 0 ? "Y" : "N";
                    List<String> lst = new ArrayList<>();
                    lst.add(getElementByXpath(driver, squadName).getText());
                    lst.add(Name);
                    lst.add(Credit);
                    lst.add(cat.getText());
                    lst.add(Points);
                    lst.add(lastPlayed);
                    PlayerRatings.add(lst.toArray(new String[0]));
                }
            }
            System.out.println(PlayerRatings);
            CSVWriter writer = new CSVWriter(new FileWriter("output.csv"), ',', CSVWriter.NO_QUOTE_CHARACTER);
            writer.writeAll(PlayerRatings);
            writer.flush();

            BufferedReader br = new BufferedReader(new FileReader(new File("output.csv")));
            List<String[]> listModel = new ArrayList<>();
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] arr = line.split(",");
                if(arr[5].equals("Y")){
                   listModel.add(arr); 
                }
                
            }
            br.close();
            CSVWriter writer2 = new CSVWriter(new FileWriter("output2.csv"), ',', CSVWriter.NO_QUOTE_CHARACTER);
            writer2.writeAll(listModel);
            writer2.flush();
        }finally{
            driver.quit();
        }
        
          
    }
    
}
