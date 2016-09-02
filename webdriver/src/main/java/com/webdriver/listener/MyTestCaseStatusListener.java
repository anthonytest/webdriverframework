package com.webdriver.listener;

import static com.webdriver.baseapp.BasePage.driver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.ScreenshotException;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.webdriver.baseapp.BaseTestSuite;

/**
 * @author anthony
 *
 */
public class MyTestCaseStatusListener implements ITestListener {
	public static boolean status = true;

	public void onTestSuccess(ITestResult result) {
		Reporter.setCurrentTestResult(result);
		Reporter.log(BaseTestSuite.testInfo);
		BaseTestSuite.testInfo = "";
		status = true;
	}

	public void onTestFailure(ITestResult result) {
		Reporter.setCurrentTestResult(result);
		Reporter.log(BaseTestSuite.testInfo);
		BaseTestSuite.testInfo = "";

		captureScreenshot(result);
	}

	public static void captureScreenshot(ITestResult result) {
		if (!result.isSuccess()) {
			try {
				SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
				if (driver != null) {
					driver.switchTo().defaultContent();
					File f = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
					try {
						String fileName = result.getName() + "_" + formater.format(Calendar.getInstance().getTime())
								+ ".jpg";
						FileUtils.copyFile(f, new File("./test-output/screenshot/" + fileName));
						Reporter.setCurrentTestResult(result);
						Reporter.log("<a href=\"../screenshot/" + fileName + "\" target=\"_blank\">Screenshot</a>");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (ScreenshotException se) {
				se.printStackTrace();
			}
		}
	}

	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub

	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub

	}

	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub

	}

	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub

	}

	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub

	}

}
