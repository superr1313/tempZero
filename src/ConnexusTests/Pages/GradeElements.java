package ConnexusTests.Pages;

import ConnexusTests.CommonElements;
import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GradeElements extends CommonElements {

	public GradeElements(WebDriver driver) 
	{
		super(driver);		
	}

    public class studentRowInfo {
        public Integer emailStudent;
    }

    public List <studentRowInfo> emailField = new ArrayList<studentRowInfo>();

	/**
	 * Identify elements on the page.
	 */

	public WebElement gradesTable() {
        return source.findElement(By.id("grid"));
	}

	public List <WebElement> gradesTableRows() {
        return gradesTable().findElements(By.cssSelector("tr"));
	}

	public List <String> getPopUpURLList(double percentClassComplete) {
		List <WebElement> tableColumns;
		List <String> gradeURLList = new ArrayList<String>();
		int rowNumber = 0;
		for(WebElement saveRows : gradesTableRows()) {
			if(!saveRows.getAttribute("class").equals("dataGridCondensed dataGridPager") && !saveRows.getAttribute("class").equals("th") && !saveRows.getAttribute("class").equals("averages") && rowNumber > 3) {
				tableColumns = saveRows.findElements(By.cssSelector("td"));
				int columnNumber = 0;
				int numPerRow = 0;
				int maxNumLessons = tableColumns.size() - 3;
				for(WebElement tableCell : tableColumns) {
					if(columnNumber > 2) {
						if(tableCell.getText().equals("G") && columnNumber <= (maxNumLessons*percentClassComplete) + 2) {
								WebElement popupClick = tableCell.findElement(By.cssSelector("a"));
								gradeURLList.add(popupClick.getAttribute("href"));
								numPerRow++;
                                emailField.get(0).emailStudent = 1;
							}
					}
					columnNumber++;
				}
				System.out.println("Found " + numPerRow + " on row " + rowNumber);
			}
			rowNumber++;
		}
		return gradeURLList;
	}

	public WebElement getTempZeroRadioButton() {
		return source.findElement(By.id("temporaryZero"));
	}

	public WebElement getTempZeroSaveButton() {
        return source.findElement(By.id("saveButton"));
	}

	public WebElement getGridTab() {
        return source.findElement(By.id("tabStrip_gridTab"));
	}

	public WebElement getSaveButtonOnGridTab() {
		try {
			return source.findElement(By.id("generateExport_image"));
		}
		catch (NoSuchElementException ex) {
			return null;
		}	
	}
	
	
	/**
	 * Actions on elements on the page.
	 * @throws InterruptedException 
	 */
	public void clickTempZeroRadioButton() {
        getTempZeroRadioButton().click();
	}

	public void clickTempZeroSaveButton() {
        getTempZeroSaveButton().click();
	}

	public void enterTempZeroGrades(double percentClassComplete) throws InterruptedException {
		for(String popupURL : getPopUpURLList(percentClassComplete)) {
			source.get(popupURL);
			waitForElementToShow(getTempZeroRadioButton());
			clickTempZeroRadioButton();
			clickTempZeroSaveButton();
			waitForElementToShow(getTempZeroSaveButton());
		}
	}

	public void goToClassGridURL(String url) {
		String sectionID = url.substring(url.lastIndexOf('=') + 1);
		String gridURL = "https://www.connexus.com/gradebook/section/grid.aspx?idSection="+sectionID+"&activeness=active";
		source.get(gridURL);
		waitForElementToShow(getSaveButtonOnGridTab());
	}

    public class studentClassInfo  {
        public String studentName;
        public double studentPercent;
        public int studentRow;
    }

    public List <studentClassInfo> studentList = new ArrayList<studentClassInfo>();

    public void setClassPercentsWithStudentOption() {
        for(int index : displaySectionOptions()) {
            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(null, "Would you like to complete all students in " + classList.get(index).className + " : "+ classList.get(index).classSchool + "?", "Warning", dialogButton);


            if(dialogResult == JOptionPane.YES_OPTION) {
                String percentClassCompleteString = JOptionPane.showInputDialog ( "Enter percent of class to complete (as whole number) for " + classList.get(index).className + " : " + classList.get(index).classSchool);
                double percentClassComplete = Double.parseDouble(percentClassCompleteString);
                percentClassComplete = (percentClassComplete/100);
                System.out.println(percentClassComplete);
                classList.get(index).classPercent = percentClassComplete;
            }
            else {
                classList.get(index).enterStudentGrades = true;
            }
        }
    }

    public List<WebElement> getStudentRows() {
        //take out the first 3 rows and remove the last 2 for the student names

        WebElement table = source.findElement(By.id("grid"));
        List<WebElement> rows = table.findElements(By.cssSelector("tr"));
        List<WebElement> studentRows = new ArrayList<WebElement>();
        for(int i = 4; i < rows.size()-3; i++ ) {
            studentRows.add(rows.get(i));
        }
        return studentRows;
    }

    public void getStudents () {

        //clear out data from student List, needs to be blank when starting each class
        studentList.clear();

        for(int i = 0; i<getStudentRows().size(); i++) {
            //populate student class
            studentClassInfo newStudent = new studentClassInfo();
            newStudent.studentName = getStudentRows().get(i).findElements(By.cssSelector("td")).get(1).getText();
            newStudent.studentRow = i;
            studentList.add(newStudent);
        }

    }

    public List<Integer> displayStudentOptions() {
        List <String> displayStudents = new ArrayList<String>();
        for(int i = 0; i<studentList.size(); i++) {
            displayStudents.add(studentList.get(i).studentName);
        }
        JFrame j=new JFrame();
        j.setAlwaysOnTop(true);
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        j.setVisible(true);
        j.setVisible(false);

        JList list = new JList(displayStudents.toArray());
        JScrollPane scrollPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JOptionPane.showMessageDialog(j, scrollPane, "Choose students to modify:", JOptionPane.PLAIN_MESSAGE);

        return Arrays.asList(ArrayUtils.toObject(list.getSelectedIndices()));
    }

    public void setStudentPercents() {
        for(int index : displayStudentOptions()) {
            String percentStudentCompleteString = JOptionPane.showInputDialog ( "Enter percent to complete for " + studentList.get(index).studentName + " in " + classList.get(index).className + " : " + classList.get(index).classSchool);
            double percentStudentComplete = Double.parseDouble(percentStudentCompleteString);
            percentStudentComplete = (percentStudentComplete/100);
            System.out.println(percentStudentComplete);
            studentList.get(index).studentPercent = percentStudentComplete;
        }
    }

    public void enterGradesForWholeClassFirst(int index) throws InterruptedException {
        getClassList(index);
        setClassPercentsWithStudentOption();

        for(int i = 0; i<classList.size(); i++) {
            if(classList.get(i).classPercent > 0 && classList.get(i).enterStudentGrades == false) {
                goToClassGridURL(classList.get(i).classURL);
					enterTempZeroGrades(classList.get(i).classPercent);
            }
        }

        for(int i = 0; i<classList.size(); i++) {
            if(classList.get(i).enterStudentGrades == true) {
                goToClassGridURL(classList.get(i).classURL);
                getStudents();
                setStudentPercents();

                for(int studentIndex = 0; studentIndex<studentList.size(); studentIndex++) {
                    if(studentList.get(studentIndex).studentPercent > 0) {
                        //getPopUpURLListForStudents(studentList.get(studentIndex).studentPercent, studentList.get(studentIndex).studentRow);
                        enterTempZeroGradesForStudents(studentList.get(studentIndex).studentPercent, studentList.get(studentIndex).studentRow);
                    }
                }

            }
        }
    }

    public List <String> getPopUpURLListForStudents(double percentStudentComplete, int studentRow) {
        List <WebElement> tableColumns;
        List <String> gradeURLList = new ArrayList<String>();

        WebElement studentRowIndex = getStudentRows().get(studentRow);

        tableColumns = studentRowIndex.findElements(By.cssSelector("td"));
        int columnNumber = 0;
        int numPerRow = 0;
        int maxNumLessons = tableColumns.size() - 3;
        for(WebElement tableCell : tableColumns) {
            if(columnNumber > 2) {
                if(tableCell.getText().equals("G") && columnNumber <= (maxNumLessons*percentStudentComplete) + 2) {
                    WebElement popupClick = tableCell.findElement(By.cssSelector("a"));
                    gradeURLList.add(popupClick.getAttribute("href"));
                    numPerRow++;
                    System.out.println("the url found was: " + popupClick.getAttribute("href"));
                    emailField.get(0).emailStudent = 1;
                }
            }
            columnNumber++;
        }
        System.out.println("Found " + numPerRow + " on row " + studentRow);

        return gradeURLList;
    }

    public void setupEmailField() {
        System.out.println("blah blah blah");
        studentRowInfo newTest = new studentRowInfo();
        newTest.emailStudent = 0;
        emailField.add(newTest);
    }

    public void enterTempZeroGradesForStudents(double percentStudentComplete, int studentRow) throws InterruptedException {
        String classURL = source.getCurrentUrl();

        for(String popupURL : getPopUpURLListForStudents(percentStudentComplete, studentRow)) {
            source.get(popupURL);
            waitForElementToShow(getTempZeroRadioButton());
            clickTempZeroRadioButton();
            clickTempZeroSaveButton();
            waitForElementToShow(getTempZeroSaveButton());
        }

        source.get(classURL);

        System.out.println("did we get to here??");
        System.out.println("this is the value of emailStudent: " + emailField.get(0).emailStudent);
        if(emailField.get(0).emailStudent == 1) {
            System.out.println("this student would have the email thing done:" + studentRow);

            WebElement tableColumn;
            WebElement studentRowIndex = getStudentRows().get(studentRow);
            tableColumn = studentRowIndex.findElements(By.cssSelector("td")).get(2);
            WebElement mailIcon = tableColumn.findElement(By.cssSelector("a"));

            Actions shiftClick = new Actions(source);
            shiftClick.keyDown(Keys.SHIFT).click(mailIcon).keyUp(Keys.SHIFT).perform();

        }

        emailField.get(0).emailStudent = 0;
    }









}
