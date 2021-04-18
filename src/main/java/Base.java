
import DreamTeams.Browser;
import DreamTeams.Credentials;
import Forms.TeamSelectionForm;
import com.opencsv.CSVWriter;
import java.io.BufferedReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Base extends Browser {

    public static void main(String[] args) throws InterruptedException, Exception {
        WebDriver driver=null;
        File f = new File("output.csv");           //file to be delete
        if (f.delete()) //returns Boolean value
        {
            System.out.println(f.getName() + " deleted");   //getting and printing the file name
        }
        
        File f1 = new File("upcomingMatches.csv");           //file to be delete
        if (f1.delete()) //returns Boolean value
        {
            System.out.println(f.getName() + " deleted");   //getting and printing the file name
        }
        
      try {
            Credentials cred=new Credentials();
            String Uname=cred.getCredentials()[0][0];
            String Pwd=cred.getCredentials()[0][1];
            String sCookie=cred.getCredentials()[0][3];
            driver=openDream11Page(driver,Uname,Pwd,sCookie);
            
            WaitUntil(driver, 10, "//div[contains(@class,'siteTabText') and contains(text(),'cricket')]");
             List<WebElement> matches = driver
                    .findElements(By.xpath("//div[contains(@class,'matchMainContainer')]//a[contains(@class,'js--match-card')]"
                            + "/div[contains(@class,'card')]"));
            int upmatches=matches.size();

            List<String[]> upcomingMatches=new ArrayList<>();
            for(int i=1;i<=upmatches;i++)
            {
                List<WebElement> upmatchTeams= getWebElementsByXpath(driver, "(//div[contains(@class,'matchMainContainer')]//a[contains(@class,'js--match-card')]/div[contains(@class,'card')])["+String.valueOf(i)+"]//div[contains(@class,'squadShortName')]");
                upcomingMatches.add(new String[]{upmatchTeams.get(0).getText()+" vs "+upmatchTeams.get(1).getText()});
            }
            
            CSVWriter writer1 = new CSVWriter(new FileWriter("upcomingMatches.csv"), ',', CSVWriter.NO_QUOTE_CHARACTER);
            writer1.writeAll(upcomingMatches);
            writer1.flush();
            
            TeamSelectionForm tl= new TeamSelectionForm(Uname,Pwd,sCookie);
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

}
