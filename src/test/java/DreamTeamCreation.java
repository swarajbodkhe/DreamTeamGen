
import static DreamTeams.Browser.openDream11Page;
import DreamTeams.CreateNewTeam;
import DreamTeams.Credentials;
import java.io.FileNotFoundException;
import org.openqa.selenium.WebDriver;
import static org.testng.Assert.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 *
 * @author 91935
 */
public class DreamTeamCreation {
    
    WebDriver driver=null;
    
    @Test(dataProvider="SearchProvider")
    public void DreamTeamCreation123(String Uname,String Pwd,String Folder,String sCookie) throws Exception {
        System.out.print("Hello I am new test");
        CreateNewTeam.CreateTeam( Uname, Pwd,Integer.parseInt(Folder),sCookie);
    }

    @org.testng.annotations.BeforeClass
    public static void setUpClass() throws Exception {
    }

    @org.testng.annotations.AfterClass
    public static void tearDownClass() throws Exception {
    }

    @org.testng.annotations.BeforeMethod
    public void setUpMethod() throws Exception {
        
    }

    @org.testng.annotations.AfterMethod
    public void tearDownMethod() throws Exception {
    }
    
    @DataProvider(name="SearchProvider")
    public Object[][] getDataFromDataprovider() throws FileNotFoundException{
        Credentials cred=new Credentials();
        return cred.getCredentials();
//        return new Object[][] {
//
//                { "dreamteamd6@gmail.com", "Sanjay@12345" ,2}
////                { "dreamteamd94@gmail.com", "Sanjay@12345" ,3},
////                { "dreamteams956@gmail.com", "Sanjay@12345" ,4}
//            };    
    }
}
