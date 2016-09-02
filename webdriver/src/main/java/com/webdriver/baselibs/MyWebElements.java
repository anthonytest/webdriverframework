package com.webdriver.baselibs;

import static com.webdriver.baseapp.BasePage.driver;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

/**
 * @author Anthony
 *
 */
public class MyWebElements implements WebElement {

	private String locator;

	public MyWebElements(String locator) {
		//
		this.locator = locator;
	}

	public List<WebElement> getWebElements() {

		List<WebElement> elements;
		elements = driver.findElements(MyBy.locator(locator));
		return elements;
	}

	public int size() {
		int size = driver.findElements(MyBy.locator(locator)).size();
		return size;
	}

	public boolean isChecked() {
		boolean flag = false;
		for (WebElement element : driver.findElements(MyBy.locator(locator))) {
			flag = element.isSelected();
		}
		return flag;
	}

	public void click() {
		for (WebElement element : driver.findElements(MyBy.locator(locator))) {
			element.click();
		}
	}

	public String getText() {
		String texts = "";
		for (WebElement element : driver.findElements(MyBy.locator(locator))) {
			String text = element.getText();
			texts = texts + "\n" + text;
		}

		return texts;
	}

	public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
		// TODO Auto-generated method stub
		return null;
	}

	public void submit() {
		// TODO Auto-generated method stub
		
	}

	public void sendKeys(CharSequence... keysToSend) {
		// TODO Auto-generated method stub
		
	}

	public void clear() {
		// TODO Auto-generated method stub
		
	}

	public String getTagName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAttribute(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isSelected() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	public List<WebElement> findElements(By by) {
		// TODO Auto-generated method stub
		return null;
	}

	public WebElement findElement(By by) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public Point getLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	public Dimension getSize() {
		// TODO Auto-generated method stub
		return null;
	}

	public Rectangle getRect() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCssValue(String propertyName) {
		// TODO Auto-generated method stub
		return null;
	}

}
