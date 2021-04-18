/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DreamTeams;

import static DreamTeams.Browser.WaitUntil;
import static DreamTeams.Browser.getElementByXpath;
import static DreamTeams.Browser.getWebElementsByXpath;
import static DreamTeams.Browser.openDream11Page;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.openqa.selenium.WebDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CreateNewTeam {
    public static void ClickUsingJS(WebDriver driver,WebElement e){
        JavascriptExecutor je = (JavascriptExecutor) driver;
                            je.executeScript("arguments[0].click();", e);
    }
    public static List<WebElement> WaitUntilElements(WebDriver driver, int TimeInSeconds, String xpath) {
        WebDriverWait wt = new WebDriverWait(driver, TimeInSeconds);
        return wt.until(ExpectedConditions
                .visibilityOfAllElementsLocatedBy(By
                        .xpath(xpath)));

    }   
    public static void CreateTeam(String UserName, String Password, int FolderName,String sCookie) throws Exception {
        String Team1Name = "";
        String Team2Name = "";
        WebDriver driver = openDream11Page(null, UserName, Password,sCookie);
        File myObj = new File("SelectedMatch.txt");
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            Team1Name = data.split(" vs ")[0].trim();
            Team2Name = data.split(" vs ")[1].trim();
        }
        myReader.close();
        List<WebElement> matches = driver
                .findElements(By.xpath("//div[contains(@class,'matchMainContainer')]//a[contains(@class,'js--match-card')]"
                        + "/div[contains(@class,'card')]"));
        int upmatches = matches.size();
//            String[][] upcomingMatches=new String[upmatches][];
        List<String[]> upcomingMatches = new ArrayList<>();

        for (int i = 1; i <= upmatches; i++) {
            List<WebElement> upmatchTeams = getWebElementsByXpath(driver, "(//div[contains(@class,'matchMainContainer')]//a[contains(@class,'js--match-card')]/div[contains(@class,'card')])[" + String.valueOf(i) + "]//div[contains(@class,'squadShortName')]");
            if (upmatchTeams.get(0).getText().equals(Team1Name) && upmatchTeams.get(1).getText().equals(Team2Name)) {
                WaitUntil(driver, 5, "(//div[contains(@class,'matchMainContainer')]//a[contains(@class,'js--match-card')]/div[contains(@class,'card')])[" + String.valueOf(i) + "]").click();
                break;
            }
        }

        try {
            File files = new File("C_VC_Teams/" + String.valueOf(FolderName));
            for (File file : files.listFiles()) {
                List<String> lines = FileUtils.readLines(file);
                List<WebElement> createTeams = driver.findElements(By.xpath("//div[contains(text(),'Create Team')]"));
                if (createTeams.size() > 0) {
                    createTeams.get(0).click();
                } else {
                    getElementByXpath(driver, "//div[contains(text(),'My Teams')]").click();
                    WaitUntil(driver, 5, "//span[contains(text(),'Create Team')]").click();
                }
                List<String> listOfPlayers = new ArrayList<>();
                for (int x = 0; x < 13; x++) {
                    if (x >= 11) {
                        listOfPlayers.add(lines.get(x));
                    } else {
                        listOfPlayers.add(lines.get(x).split(",")[1]);
                    }
                }
                int q = 0;
                List<WebElement> Category = getWebElementsByXpath(driver, "//div[contains(@class,'createTeamTabsContainer')]//div[contains(@class,'createTeam')][1]");

                for (WebElement cat : Category) {
                    cat.click();
                    int size = getWebElementsByXpath(driver, "(//div[contains(@class,'playerCardInfoCell_') and contains(@class,'playerCardInfoContainer_')])").size();
                    for (int k = 1; k <= size; k++) {
                        WebElement displayedPlayerName = getElementByXpath(driver, "(//div[contains(@class,'playerCardInfoCell_') and contains(@class,'playerCardInfoContainer_')])["
                                + String.valueOf(k) + "]//div[contains(@class,'playerName_')]");
                        if (listOfPlayers.contains(displayedPlayerName.getText())) {
                            WebElement e = WaitUntil(driver,3, "(//div[contains(@class,'playerCardInfoCell_') and contains(@class,'playerCardInfoContainer_')])["
                                    + String.valueOf(k) + "]//div[contains(@class,'playerName_')]/..");
                            JavascriptExecutor je = (JavascriptExecutor) driver;
                            je.executeScript("arguments[0].click();", e);
//                            Actions act= new Actions(driver);
//                            act.moveToElement(e).click().build().perform();
                        }
                    }
                }

                getElementByXpath(driver, "//button[text()='CONTINUE']").click();
                WaitUntil(driver, 2, "//span[contains(text(),'Captain & Vice Captain')]").isDisplayed();
                WebElement CaptainBtn=getElementByXpath(driver, "//div[contains(@class,'playerName') and contains(text(),'" + listOfPlayers.get(11) + "')]/../../following-sibling::div//div[contains(@class,'roleIcon') and text()='C']");
                ClickUsingJS(driver, CaptainBtn);
                WebElement VCBtn=getElementByXpath(driver, "//div[contains(@class,'playerName') and contains(text(),'" + listOfPlayers.get(12) + "')]/../../following-sibling::div//div[contains(@class,'roleIcon') and text()='VC']");
                ClickUsingJS(driver,VCBtn );
                WaitUntil(driver, 2, "//button[text()='SAVE TEAM']").click();
                int msg=getWebElementsByXpath(driver, "//div[contains(@class,'js--message')]").size();
                System.out.println(msg);
            }
            ClickUsingJS(driver, getElementByXpath(driver, "(//div[contains(@class,'js--contest-card__prize-pool')  and text()='Practice Contest'])[2]/..//button"));
            List<WebElement> Teams= WaitUntilElements(driver,5,"//div[contains(@class,'check-box')]/div[contains(@class,'Unselected')]");
            for (int i = 0; i < Teams.size(); i++) {
                ClickUsingJS(driver, Teams.get(i));
            }
            ClickUsingJS(driver, WaitUntil(driver, 3, "//div[text()='Join']/../../button"));
            ClickUsingJS(driver, WaitUntil(driver, 3, "//div[contains(@class,'js--contest-join-confirm')]"));
            if(getWebElementsByXpath(driver, "//div[contains(@class,'js--contest-join-confirm')]").size()==0 )
                    {
                        System.out.print("Contest Joining Complete");
                    }else{
                             System.out.print("Contest Joining Complete");
                            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
