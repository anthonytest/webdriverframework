package com.webdriver.baselibs;

import static com.webdriver.baselibs.PrintTestCases.print;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.FindsById;
import org.openqa.selenium.internal.FindsByXPath;

/**
 * @author Anthony
 *
 */
public abstract class MyBy extends By {

	public static By locator(String locator) {
		if (!locator.contains(":")) {
			return By.id(locator);
		} else {
			String[] lArr = locator.split(":");
			String by = lArr[0];
			String using = locator.substring(by.length() + 1);
			if (by.equalsIgnoreCase("id")) {
				return By.id(using);
			} else if (by.equalsIgnoreCase("name")) {
				return By.name(using);
			} else if (by.equalsIgnoreCase("xpath")) {
				return By.xpath(using);
			} else if (by.equalsIgnoreCase("cssSelector")) {
				return By.cssSelector(using);
			} else if (by.equalsIgnoreCase("linkText")) {
				return By.linkText(using);
			} else if (by.equalsIgnoreCase("partialLinkText")) {
				return By.partialLinkText(using);
			} else if (by.equalsIgnoreCase("tagName")) {
				return By.tagName(using);
			} else if (by.equalsIgnoreCase("className")) {
				return By.className(using);
			} else {
				print(" Element " + locator + "cannot be found.. ");
				throw new IllegalArgumentException("Can't find element");
			}
		}
	}

	public List<WebElement> findElements(SearchContext context, String id) {
		if (context instanceof FindsById)
			return ((FindsById) context).findElementsById(id);
		return ((FindsByXPath) context).findElementsByXPath(".//*[@id = '" + id + "']");
	}
}
