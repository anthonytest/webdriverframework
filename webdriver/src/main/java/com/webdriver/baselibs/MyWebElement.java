package com.webdriver.baselibs;

import static com.webdriver.baseapp.BasePage.driver;
import static com.webdriver.baselibs.PrintTestCases.charSequence2String;
import static com.webdriver.baselibs.PrintTestCases.print;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Anthony
 *
 */
public class MyWebElement implements WebElement {
	private String locator;
	private String[] subLocator;
	private int index = -1;
	private int[] sub_index;
	private final int WEB_ELEMENT_WAIT_TIME = 60;
	private final int IMPLICITLY_WAIT_TIME = 30;
	private WebDriverWait wait;
	private String elementName = "defaultElement";
	private WebElement webElement;
	private static Logger qaLog = Logger.getLogger(MyWebElement.class);

	public MyWebElement(String locator) {
		driver.manage().timeouts().implicitlyWait(IMPLICITLY_WAIT_TIME, TimeUnit.SECONDS);
		this.locator = locator;
	}

	public MyWebElement(String locator, String elementName, String... subLocator) {
		this(locator);
		this.subLocator = subLocator;
		this.elementName = elementName;
	}

	// index is zero based
	public MyWebElement(String locator, String elementName, int index) {
		this(locator);
		this.index = index;
		this.elementName = elementName;
	}

	public MyWebElement(WebElement webElement) {
		this.webElement = webElement;
		locator = "WebElement";
		elementName = webElement.getText();
	}

	// locate sub-element of MyWebelement object
	public MyWebElement(String locator, String elementName, int index, String... subLocator) {
		this(locator);
		this.subLocator = subLocator;
		this.index = index;
		this.elementName = elementName;
	}

	// locate sub-element of MyWebelement object by sub-index, useful in table
	// item list
	public MyWebElement(String locator, String elementName, int index, String[] subLocator, int[] sub_index) {
		this(locator);
		this.index = index;
		this.elementName = elementName;
		this.subLocator = subLocator;
		this.sub_index = sub_index;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	public void setElement(WebElement element) {
		this.webElement = element;
	}

	public String getLocator() {
		return locator;
	}

	// get element from jQuery Selector, default returns the first one
	private WebElement getWebElementByJquery(int index) {
		WebElement we_Jquery;
		String[] lArr = locator.split(":");
		String by = lArr[0];
		String using = locator.substring(by.length() + 1);
		index = (index >= 0) ? index : 0;
		we_Jquery = (WebElement) ((JavascriptExecutor) driver)
				.executeScript("return jQuery(\"" + using + "\").get(" + index + ")");
		return we_Jquery;
	}

	// get element, default returns the first one
	public WebElement getWebElement() {
		driver.manage().timeouts().implicitlyWait(IMPLICITLY_WAIT_TIME, TimeUnit.SECONDS);
		WebElement we;
		if (locator.contains("WebElement")) {
			we = webElement;
		} else {
			if (locator.contains("jQuerySelector")) {
				we = getWebElementByJquery(index);
			} else {
				if (index >= 0) {
					// the index is zero based
					we = driver.findElements(MyBy.locator(locator)).get(index);
				} else {
					we = driver.findElement(MyBy.locator(locator));
				}
				if (subLocator != null) {
					if (sub_index == null) {
						for (String sub : subLocator) {
							we = we.findElement(MyBy.locator(sub));
						}
					} else {
						for (int k = 0; k < subLocator.length; k++) {
							we = we.findElements(MyBy.locator(subLocator[k])).get(sub_index[k]);
						}
					}
				}
			}
		}
		return we;
	}

	public String getElementName() {
		if (elementName.equalsIgnoreCase("defaultElement")) {
			try {
				if (!(getAttribute("id").isEmpty()))
					elementName = getAttribute("id");
				else if (!(getAttribute("name").isEmpty()))
					elementName = getAttribute("name");
				return elementName;
			} catch (Exception e) {
				return elementName;
			}
		} else {
			return elementName;
		}
	}

	/*
	 * this method is used to count the total numbers of an elements group by
	 * jQuery
	 */
	public int jqueryCountElementNumbers() {
		String[] lArr = locator.split(":");
		String by = lArr[0];
		String using = locator.substring(by.length() + 1);
		Integer n = (Integer) ((JavascriptExecutor) driver).executeScript("jQuery(\"" + using + "\").size()");
		return n;
	}

	public void click() {
		print(" Click " + getElementName());
		getWebElement().click();
	}

	public void check() {
		print(" Click " + getElementName());
		if (!getWebElement().isSelected()) {
			getWebElement().click();
		}
	}

	public void uncheck() {
		print(" Uncheck " + getElementName());
		if (webElement.isSelected()) {
			webElement.click();
		}

	}

	public boolean isSelected() {
		return getWebElement().isSelected();
	}

	public void clearSet(String s) {
		webElement = getWebElement();
		print(" Type " + s + " in " + getElementName());
		webElement.clear();
		webElement.sendKeys(s);
	}

	public String getText() {
		return getAttribute("innerHTML").replaceAll("\\<.*?>", "").replaceAll("&amp;", "&");
	}

	public String getTextNoNewLine() {
		return getText().replaceAll("\\n", "");
	}

	public void submit() {
		print(" Submit " + getElementName());
		getWebElement().submit();
	}

	public WebElement findElement(By by) {
		return getWebElement().findElement(by);
	}

	public WebElement findElement(By by, String regEx) {
		List<WebElement> webElements = findElements(by);
		for (WebElement webElement : webElements) {

			if (webElement.getText().matches(regEx)) {
				return webElement;
			}
		}

		throw new NotFoundException("Element is not selected");
	}

	public List<WebElement> findElements(By by) {
		return getWebElement().findElements(by);
	}

	public WebElement findElement(String regEx) {
		List<WebElement> webElements = findElements(MyBy.locator(locator));
		for (WebElement webElement : webElements) {

			if (webElement.getText().matches(regEx)) {
				return webElement;
			}
		}

		throw new NotFoundException("Element is not selected");
	}

	public Dimension getSize() {
		return getWebElement().getSize();
	}

	public void clear() {
		getWebElement().clear();
	}

	// this elapsetim is to prevent stale webelement,
	// happen when you find a WebElement, and then the DOM changes, and then you
	// try to do something to that WebElement,
	public String getAttribute(String name) {
		String attribute = "";
		long startTime = System.currentTimeMillis();
		long elapsedTime = 0L;
		while (elapsedTime < 30 * 1000) {
			try {
				attribute = getWebElement().getAttribute(name);
				break;
			} catch (StaleElementReferenceException e) {
				print("stale attribute found. Name is:" + name);
			}
			elapsedTime = System.currentTimeMillis() - startTime;
		}
		return attribute;
	}

	public String getCssValue(String propertyName) {
		return getWebElement().getCssValue(propertyName);
	}

	public Point getLocation() {
		return getWebElement().getLocation();
	}

	public String getTagName() {
		return getWebElement().getTagName();
	}

	public boolean isDisplayed() {
		return getWebElement().isDisplayed();
	}

	public boolean isEnabled() {
		return getWebElement().isEnabled();
	}

	public void sendKeys(CharSequence... keysToSend) {
		print(" Type " + charSequence2String(keysToSend) + " in " + getElementName());
		getWebElement().sendKeys(keysToSend);
	}

	public void input(CharSequence... keysToSend) {
		webElement = getWebElement();
		print(" Type " + charSequence2String(keysToSend) + " in " + getElementName());
		webElement.sendKeys(keysToSend);
	}

	/*
	 * this method is similar to JQuery's val, which is used to insert data into
	 * a read-only textbox which appended by data picker
	 */
	public void val(CharSequence... keysToSend) {
		webElement = getWebElement();
		print(" Type " + charSequence2String(keysToSend) + " in " + getElementName());
		((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1]", webElement, keysToSend);
	}

	/*
	 * this method is similar to JQuery's val, which is used to insert data into
	 * a read-only textbox which appended by data picker
	 */
	public void val() {
		webElement = getWebElement();

		((JavascriptExecutor) driver).executeScript("$(webElement).val()", webElement);
	}

	public void verifyCssValue(String propertyName, String expectedValue) {
		String elementName = getElementName();
		Object actual;
		Object expected;
		if (propertyName.contains("color")) {
			actual = (Color) Color.fromString(getCssValue(propertyName));
			expected = (Color) Color.fromString(expectedValue);
		} else {
			actual = (String) getCssValue(propertyName);
			expected = (String) expectedValue;
		}
		MyVerifyAssert
				.verifyEquals(
						" Verify " + propertyName + " of " + elementName + " equals: "
								+ "<br/>&nbsp; -----expected---: " + expected + "<br/>&nbsp; -----actual---: " + actual,
						actual, expected);
	}

	public void verifyAttributeContains(String attribute, String... expectedArr) {
		String elementName = getElementName();
		String actual = getAttribute(attribute);
		String actualReplace = actual.replaceAll("\r|\n", "");
		String expected = "";
		for (String s : expectedArr) {
			expected = expected + s;
			MyVerifyAssert.verifyContains(" Verify " + elementName + " contains:" + "<br/>&nbsp; -----expected---: "
					+ expected + "<br/>&nbsp; -----actual---: " + actual, actualReplace, expected);
			expected = "";
		}
	}

	public void verifyAttributeContainsIgnoreSpaces(String attribute, String... expectedArr) {
		String elementName = getElementName();
		String actual = getAttribute(attribute);
		String actualReplace = actual.replaceAll("\r|\n|\\s+", "");
		String expected = "";
		for (String s : expectedArr) {
			expected = expected + s;
			String expectedReplace = expected.replaceAll("\\s+", "");
			MyVerifyAssert
					.verifyContains(
							" Verify " + elementName + " contains(ignore space):" + "<br/>&nbsp; -----expected---: "
									+ expected + "<br/>&nbsp; -----actual---: " + actualReplace,
							actualReplace, expectedReplace);
			expected = "";
		}
	}

	public void verifyMatches(String expected) {
		String elementName = getElementName();
		String actual = getTextNoNewLine();
		MyVerifyAssert.verifyMatches(driver,
				"\nVerify " + elementName + " matches:" + "\nexpected:" + expected + "\n" + "actual:" + actual + "\n",
				actual, expected);
	}

	public void verifyMatchesByLowerCase(String expected) {
		String elementName = getElementName();
		String actual = getTextNoNewLine().toLowerCase();
		MyVerifyAssert.verifyMatches(driver,
				"\nVerify " + elementName + " matches:" + "\nexpected:" + expected + "\n" + "actual:" + actual + "\n",
				actual, expected);
	}

	public void verifyContains(String... expectedArr) {
		String elementName = getElementName();
		String actual = getText();
		if (actual.length() == 0 && getAttribute("value") != null) {
			actual = getAttribute("value");
		}
		String actualReplace = actual.replaceAll("\r|\n", " ");
		String expected = "";
		for (String s : expectedArr) {
			expected = expected + s;
			MyVerifyAssert.verifyContains(" Verify " + elementName + " contains: " + "<br/>&nbsp; -----expected---: "
					+ expected + "<br/>&nbsp; -----actual---: " + actualReplace, actualReplace, expected);
			expected = "";
		}
	}

	// this method is used to compare expected value with element's attribute
	// value, if no attribute specified, then compare the getText();
	public void verifyContainsAttribute(String expected, String... attributeNames) {
		String elementName = getElementName();
		String actual = "";
		if (attributeNames.length == 0) {
			actual = getText().replaceAll("\r|\n", " ");
		} else {
			for (String s : attributeNames) {
				actual = actual + getAttribute(s);
			}
		}
		MyVerifyAssert.verifyContains(" Verify " + elementName + " equals: " + "<br/>&nbsp; -----expected---: "
				+ expected + "<br/>&nbsp; -----actual---: " + actual, actual, expected);
	}

	public void verifyNotContains(String... expectedArr) {
		String elementName = getElementName();
		String actual = getText();
		if (actual.length() == 0 && getAttribute("value") != null) {
			actual = getAttribute("value");
		}
		String actualReplace = actual.replaceAll("\r|\n", " ");
		String expected = "";
		for (String s : expectedArr) {
			expected = expected + s;
			MyVerifyAssert.verifyNotContains(" Verify " + elementName + " NOT contains: "
					+ "<br/>&nbsp; -----expected---: " + expected + "<br/>&nbsp; -----actual---: " + actualReplace,
					actualReplace, expected);
			expected = "";
		}
	}

	public void verifyContainsIgnoreSpaces(String... expectedArr) {
		String elementName = getElementName();
		String actual = getText();
		if (actual.length() == 0 && getAttribute("value") != null) {
			actual = getAttribute("value");
		}
		String actualReplace = actual.replaceAll("\r|\n|\\s+", "");
		String expected = "";
		for (String s : expectedArr) {
			expected = expected + s;
			String expectedReplace = expected.replaceAll("\\s+", "");
			MyVerifyAssert.verifyContains(
					" Verify " + elementName + " contains(ignore spaces): " + "<br/>&nbsp; -----expected---: "
							+ expected + "<br/>&nbsp; -----actual---: " + actual.replaceAll("\r|\n|\\s+", " "),
					actualReplace, expectedReplace);
			expected = "";
		}
	}

	public void verifyNotContainsIgnoreSpaces(String... expectedArr) {
		String elementName = getElementName();
		String actual = getText();
		if (actual.length() == 0 && getAttribute("value") != null) {
			actual = getAttribute("value");
		}
		String actualReplace = actual.replaceAll("\r|\n|\\s+", "");
		String expected = "";
		for (String s : expectedArr) {
			expected = expected + s;
			String expectedReplace = expected.replaceAll("\\s+", "");
			MyVerifyAssert.verifyNotContains(
					" Verify " + elementName + " NOT contains(ignore spaces): " + "<br/>&nbsp; -----expected---: "
							+ expected + "<br/>&nbsp; -----actual---: " + actual.replaceAll("\r|\n|\\s+", " "),
					actualReplace, expectedReplace);
			expected = "";
		}
	}

	public void verifySelected(boolean expected) {
		String elementName = getElementName();
		boolean actual = isSelected();
		MyVerifyAssert.verifyEquals(" Verify " + elementName + " isSelected:" + "<br/>&nbsp; -----expected---: "
				+ expected + "<br/>&nbsp; -----actual---: " + actual, actual, expected);
	}

	public void verifyEnabled(boolean expected) {
		String elementName = getElementName();
		boolean actual = isEnabled();
		MyVerifyAssert.verifyEquals(driver,
				"\nVerify " + elementName + " isEnabled:" + "\nexpected:" + expected + "\n" + "actual:" + actual + "\n",
				actual, expected);
	}

	public void verifyDisplayed(boolean expected) {
		webElement = driver.findElement(MyBy.locator(locator));
		String elementName = getElementName();
		boolean actual = webElement.isDisplayed();
		MyVerifyAssert.verifyEquals(driver, "\nVerify " + elementName + " isDisplayed:" + "\nexpected:" + expected
				+ "\n" + "actual:" + actual + "\n", actual, expected);
	}

	public void verifyEquals(String expected, String... attributeNames) {
		String elementName = getElementName();
		String actual = "";
		if (attributeNames.length == 0) {
			actual = getText().replaceAll("\r|\n", " ");
		} else {
			for (String s : attributeNames) {
				actual = actual + getAttribute(s);
			}
		}

		MyVerifyAssert.verifyEquals(" Verify " + elementName + " equals: " + "<br/>&nbsp; -----expected---: " + expected
				+ "<br/>&nbsp; -----actual---: " + actual, actual, expected);
	}

	public void selectByVisibleText(String text) {
		webElement = getWebElement();
		print(" Select " + text + " in dropdown " + getElementName());
		new Select(webElement).selectByVisibleText(text);
	}

	public void deselectByVisibleText(String text) {
		webElement = getWebElement();
		print(" Deselect " + text + " in dropdown " + getElementName());
		new Select(webElement).deselectByVisibleText(text);
	}

	public void selectByIndex(int index) {
		webElement = getWebElement();
		new Select(webElement).selectByIndex(index);
		print(" Select index" + index + " in dropdown " + getElementName());
	}

	public void deselectByIndex(int index) {
		webElement = getWebElement();
		print(" Deselect index" + index + " in dropdown " + getElementName());
		new Select(webElement).deselectByIndex(index);
	}

	public void deselectAll() {
		webElement = getWebElement();
		print(" Deselect all" + " in dropdown " + getElementName());
		new Select(webElement).deselectAll();
	}

	public WebElement getFirstSelectedOption() {
		webElement = getWebElement();
		print(" Get first selected option" + " in dropdown " + getElementName());
		return new Select(webElement).getFirstSelectedOption();
	}

	public List<WebElement> getAllSelectedOptions() {
		webElement = getWebElement();
		print(" Get all selected option" + " in dropdown " + getElementName());
		return new Select(webElement).getAllSelectedOptions();
	}

	public List<WebElement> getOptions() {
		webElement = getWebElement();
		return new Select(webElement).getOptions();
	}

	public void verifySelectDropDownListSize(int expected) {
		String elementName = getElementName();
		int actual = this.getOptions().size();
		MyVerifyAssert.verifyEquals(
				" Verify the total number in the select dropdown list: " + elementName + " equals to:"
						+ "<br/>&nbsp; -----expected---: " + expected + "<br/>&nbsp; -----actual---: " + actual,
				actual, expected);
	}

	public void verifyFirstSelectedOption(String expected) {
		String elementName = getElementName();
		String actual = this.getFirstSelectedOption().getText();
		MyVerifyAssert.verifyEquals(
				" Verify First selected element options in Select box: " + elementName + " equals:"
						+ "<br/>&nbsp; -----expected---: " + expected + "<br/>&nbsp; -----actual---: " + actual,
				actual, expected);
	}

	public void verifyAllSelectedOptions(String... expected) {
		webElement = getWebElement();
		String elementName = getElementName();
		List<String> exp_sel_options = Arrays.asList(expected);
		List<String> act_sel_options = new ArrayList<String>();
		for (WebElement option : this.getAllSelectedOptions())
			act_sel_options.add(option.getText());
		// Verify expected array for selected options match with actual
		// options selected
		MyVerifyAssert
				.verifyArrayEquals(
						" Verify All selected element options in Select box: " + elementName + " equals: "
								+ "<br/>&nbsp; -----expected---: " + exp_sel_options.toArray()
								+ "<br/>&nbsp; -----actual---: " + act_sel_options.toArray(),
						act_sel_options.toArray(), exp_sel_options.toArray());
	}

	public void verifyAllOptions(String[] expected) {
		webElement = getWebElement();
		String elementName = getElementName();
		List<String> exp_sel_options = Arrays.asList(expected);
		List<String> act_sel_options = new ArrayList<String>();
		for (WebElement option : this.getOptions())
			act_sel_options.add(option.getText());
		// Verify expected array for selected options match with actual options
		// selected
		MyVerifyAssert
				.verifyArrayEquals(
						" Verify All visible element options in Select box: " + elementName + " equals: "
								+ "<br/>&nbsp; -----expected---: " + exp_sel_options.toString()
								+ "<br/>&nbsp; -----actual---: " + act_sel_options.toString(),
						act_sel_options.toArray(), exp_sel_options.toArray());
	}

	public void verifyOptionsByIndex(String expected, int index) {
		webElement = getWebElement();
		String elementName = getElementName();
		String actual = this.getOptions().get(index).getText();
		// Verify expected array for selected options match with actual options
		// selected
		MyVerifyAssert.verifyArrayEquals(
				" Verify NO." + index + " element options in Select box: " + elementName + " equals: "
						+ "<br/>&nbsp; -----expected---: " + expected + "<br/>&nbsp; -----actual---: " + actual,
				actual, expected);
	}

	public MyWebElement waitUntilElementFound() {
		this.wait = new WebDriverWait(driver, WEB_ELEMENT_WAIT_TIME);
		wait.until(new ExpectedCondition<WebElement>() {
			public WebElement apply(WebDriver driver) {
				try {
					return getWebElement();
				} catch (NoSuchElementException e) {
					throw new NoSuchElementException("Element is not found");
				}
			}
		});
		this.setElementName(getElementName());
		return this;
	}

	public MyWebElement waitUntilElementPresent() {

		this.wait = new WebDriverWait(driver, WEB_ELEMENT_WAIT_TIME);
		wait.until(new ExpectedCondition<WebElement>() {
			public WebElement apply(WebDriver driver) {
				WebElement element = getWebElement();
				if (element == null || !element.isDisplayed()) {
					/**
					 * add element == null into judgement, because once login
					 * submit, signOut Link will be 'null' at first so that the
					 * element.isDisplayed in waitUntilElementPresent method
					 * will always throw java.null.lang.exception which cause
					 * cases wrong another workaround is just to use
					 * waitUntilElementFound method, which works well every
					 * time..
					 */
					throw new NotFoundException("Element is not displayed");
				}
				return element;
			}
		});
		this.setElementName(getElementName());
		return this;
	}

	public MyWebElement waitUntilElementPresentByTime(int t) {
		this.wait = new WebDriverWait(driver, t);
		wait.until(new ExpectedCondition<WebElement>() {
			public WebElement apply(WebDriver driver) {
				WebElement element = getWebElement();
				if (element == null || !element.isDisplayed()) {
					/**
					 * add element == null into judgement, because once login
					 * submit, signOut Link will be 'null' at first so that the
					 * element.isDisplayed in waitUntilElementPresent method
					 * will always throw java.null.lang.exception which cause
					 * cases wrong another workaround is just to use
					 * waitUntilElementFound method, which works well every
					 * time..
					 */
					throw new NotFoundException("Element is not displayed");
				}
				return element;
			}
		});
		this.setElementName(getElementName());
		return this;
	}

	public MyWebElement waitUntilElementPresentInWindow(String windowName) {
		this.wait = new WebDriverWait(driver, WEB_ELEMENT_WAIT_TIME);
		final String myWindowName = windowName;
		wait.until(new ExpectedCondition<WebElement>() {
			public WebElement apply(WebDriver driver) {
				try {
					Set<String> handlers = driver.getWindowHandles();
					for (String handler : handlers) {
						driver.switchTo().window(handler);
						if (driver.getTitle().contains(myWindowName)) {
							qaLog.info(myWindowName);
							break;
						}
					}
					WebElement element = getWebElement();
					if (!element.isDisplayed()) {
						throw new NotFoundException("Element is not displayed");
					}
					return element;

				} catch (Exception e) {
					throw new NotFoundException("Element is not displayed");
				}
			}
		});
		this.setElementName(getElementName());
		return this;
	}

	public MyWebElement waitUntilElementEnabled() {
		this.wait = new WebDriverWait(driver, WEB_ELEMENT_WAIT_TIME);
		wait.until(new ExpectedCondition<WebElement>() {
			public WebElement apply(WebDriver driver) {
				WebElement element = getWebElement();
				if (!element.isDisplayed()) {
					throw new NotFoundException("Element is not displayed");
				}
				if (!element.isEnabled()) {
					throw new NotFoundException("Element is not enabled");
				}
				return element;
			}
		});
		this.setElementName(getElementName());
		return this;
	}

	public void waitUntilAjaxCallsComplete() {
		this.wait = new WebDriverWait(driver, WEB_ELEMENT_WAIT_TIME);
		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {

				boolean result = (Boolean) ((JavascriptExecutor) driver).executeScript("return jQuery.active == 0");
				if (!result) {
					throw new NotFoundException("Ajax calls have not completed");
				}
				return result;
			}
		});
	}

	public MyWebElement waitUntilElementChecked() {
		this.wait = new WebDriverWait(driver, WEB_ELEMENT_WAIT_TIME);
		wait.until(new ExpectedCondition<WebElement>() {
			public WebElement apply(WebDriver driver) {
				WebElement element = getWebElement();
				if (!element.isDisplayed()) {
					throw new NotFoundException("Element is not displayed");
				}
				if (!element.isSelected()) {
					throw new NotFoundException("Element is not selected");
				}
				return element;
			}
		});
		this.setElementName(getElementName());
		return this;
	}

	public MyWebElement waitUntilElementEnabledInFrame(String frameName) {
		this.wait = new WebDriverWait(driver, WEB_ELEMENT_WAIT_TIME);
		final String myFrameName = frameName;
		wait.until(new ExpectedCondition<WebElement>() {
			public WebElement apply(WebDriver driver) {
				try {
					driver.switchTo().defaultContent().switchTo().frame(myFrameName);
					WebElement element = getWebElement();
					if (!element.isDisplayed()) {
						throw new NotFoundException("Element is not displayed");
					}
					if (!element.isEnabled()) {
						throw new NotFoundException("Element is not enabled");
					}
					return element;
				} catch (Exception e) {
					throw new NotFoundException("Frame is not found");
				}
			}
		});
		this.setElementName(getElementName());
		return this;
	}

	public MyWebElement waitUntilElementPresentInFrame(String frameName) {
		this.wait = new WebDriverWait(driver, WEB_ELEMENT_WAIT_TIME);
		final String myFrameName = frameName;
		wait.until(new ExpectedCondition<WebElement>() {
			public WebElement apply(WebDriver driver) {
				try {
					driver.switchTo().defaultContent().switchTo().frame(myFrameName);
					WebElement element = getWebElement();
					if (!element.isDisplayed()) {
						throw new NotFoundException("Element is not displayed");
					}
					return element;
				} catch (Exception e) {
					throw new NotFoundException("Frame is not found");
				}
			}
		});
		this.setElementName(getElementName());
		return this;
	}

	public void verifyElementFound() {
		try {
			print(" Verify whether element " + getElementName() + " found");
			waitUntilElementFound();
		} catch (Exception e) {
			throw new NoSuchElementException("Element " + getElementName() + " Not Found!");
		}
	}

	public void verifyElementPresent() {
		try {
			print(" Verify whether element " + getElementName() + " displayed");
			waitUntilElementPresent();
		} catch (Exception e) {
			print(" -" + getElementName() + " NOT displayed");
			throw new NotFoundException("Element " + getElementName() + " Not Displayed!");
		}
	}

	public void verifyElementNotPresent() {
		print(" Verify whether element " + getElementName() + " NOT displayed");
		Boolean flag = false;
		try {
			waitUntilElementPresentByTime(1); // wait for 1 second to see
												// whether element disappeared
		} catch (Exception e) {
			flag = true;
		}
		if (!flag) {
			print(" -Element " + getElementName() + " still displayed!");
			throw new IllegalStateException("Element " + getElementName() + " still displayed!");
		}
	}

	public MyWebElement waitUntilElementDisappearByTime(int t) throws InterruptedException {
		for (int i = 0; i < t; i++) {
			try {
				verifyElementNotPresent();
				return this;
			} catch (IllegalStateException e) {
				Thread.sleep(1000);
				continue;
			}
		}
		print(" Element " + getElementName() + " still displayed after " + t + "seconds!");
		throw new IllegalStateException("Element " + getElementName() + " still displayed!");
	}

	public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
		// TODO Auto-generated method stub
		return null;
	}

	public Rectangle getRect() {
		// TODO Auto-generated method stub
		return null;
	}
}
