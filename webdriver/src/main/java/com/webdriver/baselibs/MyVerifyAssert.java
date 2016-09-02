package com.webdriver.baselibs;

import static com.webdriver.baselibs.PrintTestCases.print;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.webdriver.baseapp.BaseTestSuite;

/**
 * @author Anthony 
 * 公用检查点
 *
 */
public class MyVerifyAssert {

	private static Map<ITestResult, List<Throwable>> verificationFailuresMap = new HashMap<ITestResult, List<Throwable>>();

	public static void assertTrue(String message, boolean condition) {
		print(message);
		Assert.assertTrue(condition, message);
	}

	public static void assertFalse(String message, boolean condition) {
		print(message);
		Assert.assertFalse(condition, message);
	}

	public static void assertEquals(String message, Object actual, Object expected) {
		print(message);
		Assert.assertEquals(actual, expected, message);
	}

	public static void verifyTrue(String message, boolean condition) {
		try {
			assertTrue(message, condition);
		} catch (Throwable e) {
			addVerificationFailure(e);
		}
	}

	public static void verifyTrue(WebDriver driver, String message, boolean condition) {
		try {
			assertTrue(message, condition);
		} catch (Throwable e) {
			addVerificationFailure(e, driver);
		}
	}

	public static void verifyMatches(String message, String actual, String expected) {
		try {
			assertTrue(message, actual.matches(expected));
		} catch (Throwable e) {
			addVerificationFailure(e);
		}
	}

	public static void verifyMatches(WebDriver driver, String message, String actual, String expected) {
		try {
			assertTrue(message, actual.matches(expected));
		} catch (Throwable e) {
			addVerificationFailure(e, driver);
		}
	}

	public static void verifyContains(String message, String actual, String expected) {
		try {
			assertTrue(message, actual.contains(expected));
		} catch (Throwable e) {
			addVerificationFailure(e);
		}
	}

	public static void verifyContains(WebDriver driver, String message, String actual, String expected) {
		try {
			assertTrue(message, actual.contains(expected));
		} catch (Throwable e) {
			addVerificationFailure(e, driver);
		}
	}

	public static void verifyNotContains(String message, String actual, String expected) {
		try {
			assertFalse(message, actual.contains(expected));
		} catch (Throwable e) {
			addVerificationFailure(e);
		}
	}

	public static void verifyNotContains(WebDriver driver, String message, String actual, String expected) {
		try {
			assertFalse(message, actual.contains(expected));
		} catch (Throwable e) {
			addVerificationFailure(e, driver);
		}
	}

	public static void verifyEquals(WebDriver driver, String message, Object actual, Object expected) {
		try {
			assertEquals(message, actual, expected);
		} catch (Throwable e) {
			addVerificationFailure(e, driver);
		}
	}

	public static void verifyEquals(String message, Object actual, Object expected) {
		try {
			assertEquals(message, actual, expected);
		} catch (Throwable e) {
			addVerificationFailure(e);
		}
	}

	public static void verifyArrayEquals(String message, Object actual, Object expected) {
		try {
			assertEquals(message, actual, expected);
		} catch (Throwable e) {
			addVerificationFailure(e);
		}
	}

	public static void verifyFalse(String message, boolean condition) {
		try {
			assertFalse(message, condition);
		} catch (Throwable e) {
			addVerificationFailure(e);
		}
	}

	public static void verifyFalse(WebDriver driver, String message, boolean condition) {
		try {
			assertFalse(message, condition);
		} catch (Throwable e) {
			addVerificationFailure(e, driver);
		}
	}

	public static void fail(String message) {
		Assert.fail(message);
	}

	public static List<Throwable> getVerificationFailures() {
		List<Throwable> verificationFailures = verificationFailuresMap.get(Reporter.getCurrentTestResult());
		return verificationFailures == null ? new ArrayList<Throwable>() : verificationFailures;
	}

	private static void doScreenShoot(WebDriver driver) {
		SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
		if (driver != null) {
			driver.switchTo().defaultContent();
			File f = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			try {
				String fileName = formater.format(Calendar.getInstance().getTime()) + ".jpg";
				FileUtils.copyFile(f, new File("./test-output/screenshot/" + fileName));
				BaseTestSuite.testInfo = BaseTestSuite.testInfo + "<a href=\"" + "screenshot/" + fileName
						+ "\" target=\"_blank\">Screenshot</a>";
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	private static void addVerificationFailure(Throwable e, WebDriver driver) {
		doScreenShoot(driver);
		List<Throwable> verificationFailures = getVerificationFailures();
		verificationFailuresMap.put(Reporter.getCurrentTestResult(), verificationFailures);
		verificationFailures.add(e);
	}

	private static void addVerificationFailure(Throwable e) {
		List<Throwable> verificationFailures = getVerificationFailures();
		verificationFailuresMap.put(Reporter.getCurrentTestResult(), verificationFailures);
		verificationFailures.add(e);
	}

}
