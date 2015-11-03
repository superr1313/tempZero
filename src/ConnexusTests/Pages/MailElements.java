package ConnexusTests.Pages;

import ConnexusTests.CommonElements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.swing.*;

public class MailElements extends CommonElements {
    public MailElements(WebDriver driver) 
    {
        super(driver);      
    }   
    
    /**
     * Identify elements on the page.
     */
    public WebElement getNextButton() {
        return source.findElement(By.id("ctl01"));
    }
    public WebElement getStudentCheckbox() {
        return source.findElement(By.id("students"));
    }
    public WebElement getCaretakerCheckbox() {
        return source.findElement(By.id("caretakersAndLearningCoaches"));
    }
    public WebElement getTeacherCheckbox() {
        return source.findElement(By.id("teachers"));
    }
    public WebElement getEmailIFrame() {
        return source.findElement(By.id("body_editor_contentIframe"));
    }
    public WebElement getSendButton() {
        return source.findElement(By.id("send"));
    }
    
    /**
     * Actions on elements on the page.
     * @throws InterruptedException 
     */

    public void setClassEmails() {
        for(int index : displaySectionOptions()) {
            String emailText = JOptionPane.showInputDialog ( "Enter email to send to " + classList.get(index).className + " : " + classList.get(index).classSchool);
            classList.get(index).classEmailText = emailText;
        }
    }

    public void clickNextButton() {
        getNextButton().click();
    }

    public void setRecipientTypeCheckbox(int recipients) {
        if(recipients == 2) {
            getStudentCheckbox().click();
            getCaretakerCheckbox().click();
        }
        else if(recipients == 3) {
            getStudentCheckbox().click();
            getTeacherCheckbox().click();
        }
        else if(recipients == 4) {
            getCaretakerCheckbox().click();
        }
        else if(recipients == 5) {
            getTeacherCheckbox().click();
        }
        else if(recipients == 6) {
            getStudentCheckbox().click();
            getCaretakerCheckbox().click();
            getTeacherCheckbox().click();
        }
        else if(recipients == 7) {
            getCaretakerCheckbox().click();
            getTeacherCheckbox().click();
        }
    }

    public void enterEmailText(String emailText) {
        source.switchTo().frame(getEmailIFrame());
        WebElement editable = source.switchTo().activeElement(); 
        editable.sendKeys(emailText); 
    }

    public void clickSend() {
        getSendButton().click();
    }

    public void sendClassEmails(String emailText, int recipients) throws InterruptedException {
        setRecipientTypeCheckbox(recipients);
        clickNextButton();
        waitForElementToShow(getEmailIFrame());
        enterEmailText(emailText);
//        clickSend();  //critical line that will actually send the emails if you uncomment it
        Thread.sleep(2000);
        //add a getElement() that gets something on the page after you click send
        //then add a waitForElementToShow(thatElement()); here
        //then i think this is done!!
    }

    public void sendEmailToEachSection(int index, int emailRecipients) throws InterruptedException {
        getClassList();
        setClassEmails();
        for(int i = 0; i<classList.size(); i++) {
            if(classList.get(i).classEmailText != null) {
                    System.out.println(classList.get(i).classURL);
                    source.get(classList.get(i).classURL);
                    waitForElementToShow(getNextButton());
                    sendClassEmails(classList.get(i).classEmailText, emailRecipients);
                }
        }
    }

    
}
