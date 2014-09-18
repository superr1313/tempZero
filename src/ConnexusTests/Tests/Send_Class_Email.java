package ConnexusTests.Tests;

import ConnexusTests.ConnexusMain;
import ConnexusTests.Pages.MailElements;
import org.junit.Test;

import java.io.IOException;

public class Send_Class_Email extends ConnexusMain {
	
    
    
	@Test
	public void sendEmails() throws IOException, InterruptedException {
		
	    int emailIconIndex = 0;
	    int recipients = 1;
	    
	    /*
	    * 1 = Students only
	    * 2 = Caretakers only
	    * 3 = Teachers only
	    * 4 = Students and Caretakers
	    * 5 = Students and Teachers
	    * 6 = Caretakers and Students
	    * 7 = All 3
	    */
	    
		MailElements mail = new MailElements(getDriver());
		mail.login();
		mail.clickSectionDropDown();
		mail.sendEmailToEachSection(emailIconIndex, recipients);
		// String newLine = StringEscapeUtils.unescapeJava(rs.getString("column_value"));
	}
}
