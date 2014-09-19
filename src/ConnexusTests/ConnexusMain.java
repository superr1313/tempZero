package ConnexusTests;


import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.util.Properties;


public class ConnexusMain {

public static String ROOT = "https://www.connexus.com/";
public static String getProperty(String string) {
	return ROOT;
}

private WebDriver driver;
private Properties configFile;

private static ConnexusMain INSTANCE = new ConnexusMain();

protected static WebDriver getNewDriver()
{
	return new ChromeDriver();
}   

@Before
public void Before() throws InterruptedException, IOException {
    if(null==System.getProperty("webdriver.chrome.driver")) {
        System.setProperty("webdriver.chrome.driver", "/Applications/chromedriver");
    }        
	driver = getNewDriver();
	startPage();
}
    
@After
public void After() {
	//driver.quit();
}

public void startPage() throws InterruptedException, IOException 
{    	
	driver.get(ConnexusMain.getProperty("ROOT"));
}

public Properties getConfigFile() {
    return configFile;
}

public static ConnexusMain getInstance() {
    return INSTANCE;
}

public WebDriver getDriver(){
    return driver;
}

}
