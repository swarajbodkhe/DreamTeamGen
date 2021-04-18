package DreamTeams;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author 91935
 */
public class Browser {

    public static WebDriver openDream11Page(WebDriver driver, String username, String Password,String sCookie) throws Exception {
        if (driver == null) {
            System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
            driver = new ChromeDriver();

        }

        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        driver.manage().window().maximize();

        driver.get("https://dream11.com/leagues");
        Cookie ck = new Cookie("connect.sid", sCookie, ".dream11.com", "/", new SimpleDateFormat("dd/MM/yyyy").parse("09/10/2021"));
        driver.manage().addCookie(ck);
        driver.navigate().refresh();
        return driver;
    }

    public static List<WebElement> getWebElementsByXpath(WebDriver driver, String xpath) {
        return driver.findElements(By.xpath(xpath));
    }

    public static WebElement getElementByXpath(WebDriver driver, String xpath) {
        return driver.findElement(By.xpath(xpath));
    }

    public static WebElement WaitUntil(WebDriver driver, int TimeInSeconds, String xpath) {
        WebDriverWait wt = new WebDriverWait(driver, TimeInSeconds);
        return wt.until(ExpectedConditions
                .visibilityOfElementLocated(By
                        .xpath(xpath)));

    }

}
