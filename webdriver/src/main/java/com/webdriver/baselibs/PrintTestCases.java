package com.webdriver.baselibs;

import com.webdriver.baseapp.BaseTestSuite;

/**
 * This class is created to log information in testng report
 */
/**
 * @author Anthony
 *
 */
public class PrintTestCases {

	private static boolean functionPrintFlag = false;

	public static void funcPrintOn(String s) {
		functionPrintFlag = true;
	}

	public static void funcPrintOff() {
		functionPrintFlag = false;

	}

	public static void print(String s) {
		if (!functionPrintFlag)
			BaseTestSuite.testInfo = BaseTestSuite.testInfo + "<pre>" + s + "</pre>";// "<br/>";

	}

	public static String charSequence2String(CharSequence... keysToSend) {
		StringBuilder sb = new StringBuilder();
		for (CharSequence c : keysToSend)
			sb.append(c);
		return sb.toString();
	}
}
