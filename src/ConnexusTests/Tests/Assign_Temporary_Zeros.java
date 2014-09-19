package ConnexusTests.Tests;

import ConnexusTests.ConnexusMain;
import ConnexusTests.Pages.GradeElements;
import org.junit.Test;

import java.io.IOException;

public class Assign_Temporary_Zeros extends ConnexusMain {
	
	@Test
	public void enterTempZeros() throws IOException, InterruptedException {
		
	    int gradeIconIndex = 4;
		GradeElements page = new GradeElements(getDriver());
		page.login();
		page.clickSectionDropDown();
		page.enterGradesForWholeClassFirst(gradeIconIndex);


	}
}
