package com.webdriver.baseapp;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

/**
 * @author Anthony
 *
 */
public class BaseTestSuite {

	/*
	 * 打印测试用例信息所用
	 */
	public static String testInfo = "";
	/*
	 * 主要用来控制外部Excel的测试用例数据，可以灵活决定从那行数据执行
	 */

	protected static int executeRow = 0;

	@Parameters({ "executeRow" })
	@BeforeClass(alwaysRun = true)
	protected void setUpBeforeClass(int executeRow) {
		BaseTestSuite.executeRow = executeRow;
	}

}
