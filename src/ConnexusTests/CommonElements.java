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
        return source.findElement(By.id("loginFormButton"));
    }

    public WebElement getSectionDropDown() {
        try {
            return source.findElement(By.id("sectionsAndStudentsLinkIb"));
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
        WebElement tableContainer = source.findElement(By.id("sections-and-students"));
        return tableContainer.findElement(By.cssSelector("table.cxTable"));
    }

    public List <WebElement> getSectionTableRows() {
        return getSectionTable().findElements(By.cssSelector("td.section-name-column"));
    }

    public void getClassList() {
        for(WebElement sectionCell : getSectionTableRows()) {
            try {
                List<WebElement> listItems = sectionCell.findElement(By.cssSelector("ul.contextMenu")).findElements(By.cssSelector("li"));

                for(WebElement listItem : listItems) {
                    WebElement listItemLink = listItem.findElement(By.cssSelector("a"));
                    if(listItemLink.getAttribute("title").contains("View this section's Grade Book.")) {

                        String className = sectionCell.findElements(By.cssSelector("a")).get(0).getText();
                        String classSchool = sectionCell.findElements(By.cssSelector("a")).get(0).getText();
                        String classURL = listItem.findElement(By.cssSelector("a")).getAttribute("href");

                        classInfo newClass = new classInfo();
                        newClass.className = className;
                        newClass.classSchool = classSchool;
                        newClass.classURL = classURL;
                        classList.add(newClass);
                    }
                }
            } catch (NoSuchElementException ex) {
            }
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
        Thread.sleep(3000);
        source.get("https://www.connexus.com/home/pages/teacher.aspx");
        waitForElementToShow(getSectionDropDown());
    }

    public boolean ElementIsDisplayed(WebElement element) {
        return element.isDisplayed();
    }

    public void clickSectionDropDown() throws InterruptedException {
        getSectionDropDown().click();
        Thread.sleep(10000);
        //the function below looks for the loading spinner, if it is still present it waits 1 second and tries again.  will try for 30 seconds.
//        for(int i=0; i<30; i++) {
//            if(ElementIsDisplayed(getSectionLoadingSpinner())) {
//                Thread.sleep(1000);
//            }
//            else {
//                break;
//            }
//        }
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
