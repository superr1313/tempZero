package ConnexusTests;

import java.util.Properties;
//import ConnexusTests.configFile;
import org.openqa.selenium.*;

public abstract class PageObject {

	protected static Properties configFile = ConnexusMain.getInstance().getConfigFile();
	
	protected WebDriver source; 	
     
    protected PageObject(WebDriver source)
    {
        this.source = source; 
    }     
	
}
