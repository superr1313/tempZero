package ConnexusTests;

import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommonElements extends PageObject {

    public CommonElements(WebDriver driver) 
    {
        super(driver);        
    }   
    
    public class classInfo {
        public String className;
        public String classURL;
        public String classSchool;
        public double classPercent;
        public String classEmailText;
        public Boolean enterStudentGrades = false;
    }
    public List <classInfo> classList = new ArrayList<classInfo>();
    
    /**
     * Identify elements on the page.
     */

    public WebElement getUsernameTextbox() {
        try {
            return source.findElement(By.id("loginFormEmail"));
        }
        catch (NoSuchElementException ex) {
            return null;
        }   
    }

    public WebElement getPassTextbox() {
        return source.findElement(By.id("loginFormPassword"));
    }

    public WebElement getLoginButton() {
        return source.findElement(By.id("loginPortallet_loginFormButton"));
    }

    public WebElement getSectionDropDown() {
        try {
            return source.findElement(By.id("mySections_ctl00_portalletHeaderLeft"));
        }
        catch (NoSuchElementException ex) {
            return null;
        }   
    }

    public WebElement getSectionLoadingSpinner() {
        return source.findElement(By.id("mySections_ctl00_loadingPanel"));
    }

    public static char[] getPass() {
        JLabel jl = new JLabel("Enter Your Password: ");
        JPasswordField jpf = new JPasswordField(24);
        Box box = Box.createHorizontalBox();
        box.add(jl);
        box.add(jpf);
        int x = JOptionPane.showConfirmDialog(null, box, "Password Entry", JOptionPane.OK_CANCEL_OPTION);

        if (x == JOptionPane.OK_OPTION) {
          return jpf.getPassword();
        }
        return null;
    }

    public WebElement getSectionTable() {
        WebElement tableContainer = source.findElement(By.id("mySections"));
        return tableContainer.findElement(By.cssSelector("table.datagridCondensed"));
    }

    public List <WebElement> getSectionTableRows() {
        return getSectionTable().findElements(By.cssSelector("tr.portalletEnhancedpurpleCell"));
    }

    public void getClassList(int index) {
        for(WebElement sectionCell : getSectionTableRows()) {
                List<WebElement> columnsInRow = sectionCell.findElements(By.cssSelector("td"));
                String className = columnsInRow.get(1).getText();   //1 = name column index
                String classSchool = columnsInRow.get(2).getText();  //2 = school column index
                String classURL = columnsInRow.get(5).findElements(By.cssSelector("a")).get(index).getAttribute("href");
                
                classInfo newClass = new classInfo();
                newClass.className = className;
                newClass.classSchool = classSchool;
                newClass.classURL = classURL;
                classList.add(newClass);
            }
    }
    
    /**
     * Actions on elements on the page.
     * @throws InterruptedException 
     */

    public void waitForElementToShow(WebElement element)   {  }

    public void goToRootURL() throws InterruptedException {
        //goes to homepage, waits for username textbox to load
        source.get(ConnexusMain.getProperty("ROOT"));
        waitForElementToShow(getUsernameTextbox());
    }

    public void clickLogin() {
        getLoginButton().click();
    }

    public void login() throws InterruptedException  {  //use this function to ask for user login info
        String usernameInput = JOptionPane.showInputDialog ( "Enter username:" ); 
        getUsernameTextbox().sendKeys(usernameInput);
        getPassTextbox().sendKeys(new String(getPass()));
        clickLogin();
        waitForElementToShow(getSectionDropDown());
    }

    public boolean ElementIsDisplayed(WebElement element) {
        return element.isDisplayed();
    }

    public void clickSectionDropDown() throws InterruptedException {
        getSectionDropDown().click();
        //the function below looks for the loading spinner, if it is still present it waits 1 second and tries again.  will try for 30 seconds.
        for(int i=0; i<30; i++) {
            if(ElementIsDisplayed(getSectionLoadingSpinner())) {
                Thread.sleep(1000);
            }
            else {
                break;
            }
        }
    }

    public List<Integer> displaySectionOptions() {
        List <String> displayOptions = new ArrayList<String>();
        for(int i = 0; i<classList.size(); i++) {
            displayOptions.add(classList.get(i).className + " : " + classList.get(i).classSchool);
        }
        JFrame j=new JFrame();
        j.setAlwaysOnTop(true);
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        j.setVisible(true);
        j.setVisible(false);
        
        JList list = new JList(displayOptions.toArray());
        JScrollPane scrollPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JOptionPane.showMessageDialog(j, scrollPane, "Choose classes to modify:", JOptionPane.PLAIN_MESSAGE);

        return Arrays.asList(ArrayUtils.toObject(list.getSelectedIndices()));
    }
    
}
