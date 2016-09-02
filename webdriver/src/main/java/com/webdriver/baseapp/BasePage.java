package com.webdriver.baseapp;

import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.webdriver.baselibs.MyException;
import com.webdriver.baselibs.MyWebElement;
import com.webdriver.baselibs.MyWebElements;

/**
 * @author Anthony
 * 
 *         测试对象库，所有页面类都需要继承的基类
 *
 */
public class BasePage {
	public static WebDriver driver;
	protected static WebDriverWait wait;
	private static final int PAGE_WAIT_TIME = 60;
	protected static String pageTitle = "defaultPage";
	protected static String pageURL = "defaultURL";

	/**
	 * 获取页面标题
	 * 
	 * @return
	 */
	public static String getPageTitle() {
		pageTitle = driver.getTitle();
		return pageTitle;
	}

	/**
	 * 获取页面的url
	 */
	public static String getPageURL() {

		pageURL = driver.getCurrentUrl();
		return pageURL;
	}

	/**
	 * 封装找元素的方法
	 */
	public static MyWebElement myWebelement(WebElement webElement) {
		return new MyWebElement(webElement);
	}

	public static MyWebElement myWebelement(String locator) {
		return new MyWebElement(locator);
	}

	public static MyWebElement myWebelement(String locator, String name, String... subLocator) {
		return new MyWebElement(locator, name, subLocator);
	}

	public static MyWebElement myWebelement(String locator, String name, int index) {
		return new MyWebElement(locator, name, index);
	}

	public static MyWebElement myWebelement(String locator, String name, int index, String... subLocator) {
		return new MyWebElement(locator, name, index, subLocator);
	}

	public static MyWebElement myWebelement(String locator, String name, int index, String[] subLocator,
			int[] sub_index) {
		return new MyWebElement(locator, name, index, subLocator, sub_index);
	}

	/**
	 * 封装找元素组合的方法
	 */
	public static MyWebElements myWebelements(String locator) {
		return new MyWebElements(locator);
	}

	/**
	 * 刷新页面
	 */
	public static void refreshPage() {
		driver.navigate().refresh();
	}

	/**
	 * 前进页面
	 */
	public static void Forward() {
		driver.navigate().forward();
	}

	/**
	 * 回退页面
	 */
	public static void back() {
		driver.navigate().back();
	}

	/**
	 * frame之间的切换，通过frame的名称来进行switch
	 * 
	 * @param frame
	 */

	public static void switchToFrame(String frame) {
		driver.switchTo().defaultContent().switchTo().frame(frame);
	}
	
	/**
	 * frame之间的切换,通过frame的序号来switch，依次顺序为从上到下，从左到右
	 * 
	 * @param frame
	 */

	public static void switchToFrame(int i) {
		driver.switchTo().parentFrame();
		driver.switchTo().frame(i);
	}

	/**
	 * 页面窗口之间的切换
	 */
	public static void switchToWindow() {

		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
		}
	}

	/**
	 * 通过传参数来操作弹出的alter窗口，true代表继续，false代表取消
	 */
	public static void AlertHandling(boolean acceptNextAlert, String alertText) {
		try {
			Alert alert = driver.switchTo().alert();
			String textOnAlert = alert.getText();

			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			Assert.assertTrue(textOnAlert.contains(alertText));
		} catch (NoAlertPresentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 验证页面标题
	 * 
	 * @param pageTitle
	 */
	public static void verifyPageTitle(String pageTitle) {
		if (!pageTitle.equals(getPageTitle())) {
			throw new IllegalStateException("This is not " + pageTitle);
		}
	}

	/**
	 * 验证页面url
	 * 
	 * @param pageURL
	 */
	public static void verifyPageURL(String pageURL) {
		if (!getPageURL().contains(pageURL)) {
			throw new IllegalStateException("This is not " + pageURL);
		}
	}

	/**
	 * 打开页面
	 * 
	 * @param url
	 */
	public static void navigateTo(String url) {
		driver.get(url);
	}

	public static void closeBrowser() {
		driver.quit();
		// clean the test output message
		BaseTestSuite.testInfo = "";
	}

	/**
	 * 开启浏览器
	 * 
	 * @param browser
	 */
	public static void openBrowser(String browser) {
		try {
			if (browser.equalsIgnoreCase("firefox")) {
				// 启动本地已经设置好的firefox，比如有一些插件已经安装好了这样
				ProfilesIni pi = new ProfilesIni();
				FirefoxProfile profile = pi.getProfile("default");
				driver = new FirefoxDriver(profile);
			} else if (browser.equalsIgnoreCase("winchrome")) {
				System.setProperty("webdriver.chrome.driver", "webdriverServer/chromedriver.exe");
				driver = new ChromeDriver();
			} else if (browser.equalsIgnoreCase("macchrome")) {
				System.setProperty("webdriver.chrome.driver", "webdriverServer/chromedriver");
				driver = new ChromeDriver();
			} else if (browser.equalsIgnoreCase("safari")) {
				driver = new SafariDriver();
			} else {
				System.setProperty("webdriver.ie.driver", "webdriverServer/IEDriverServer.exe");
				driver = new InternetExplorerDriver();
			}
			wait = new WebDriverWait(driver, PAGE_WAIT_TIME);
			driver.manage().window().maximize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 等待页面出现
	 * 
	 * @param windowName
	 */
	public static void waitUntilWindowPresent(String windowName) {
		final String myWindowName = windowName;
		wait.until(new ExpectedCondition<String>() {
			public String apply(WebDriver driver) {
				try {
					String s = null;
					Set<String> handlers = driver.getWindowHandles();
					for (String handler : handlers) {
						driver.switchTo().window(handler);
						if (driver.getTitle().equals(myWindowName)) {
							s = driver.getTitle();
							break;
						}
					}
					return s;
				} catch (Exception e) {
					throw new NoSuchWindowException("Window is not displayed");
				}
			}
		});
	}

	/**
	 * 通过js状态判断页面是否加载完成
	 */
	public static void waitUntilPageReady() {
		wait.until(new ExpectedCondition<String>() {
			public String apply(WebDriver driver) {
				try {
					String s = null;
					if (((String) ((JavascriptExecutor) driver).executeScript("return document.readyState"))
							.equalsIgnoreCase("complete")) {
						s = "complete";
					}
					return s;
				} catch (Exception e) {
					throw new MyException("waitUntilPageIsReady failed");
				}
			}
		});
	}

	/**
	 * 等待页面标题出现
	 * 
	 * @param pageTitle
	 */
	public static void waitUntilPageTitlePresent(final String pageTitle) {
		wait.until(new ExpectedCondition<String>() {
			public String apply(WebDriver driver) {
				try {
					String s = null;
					if (driver.getTitle().contains(pageTitle)) {
						s = pageTitle;
					}
					return s;
				} catch (Exception e) {
					throw new MyException("waitUntilPageTitlePresent failed");
				}
			}
		});
	}

	/**
	 * 获取当前页面的sessionID
	 */
	public static String getSessionId() {
		Set<Cookie> cookies = driver.manage().getCookies();
		String sessionId = "";
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("sid")) {
				sessionId = cookie.getValue();
				return sessionId;
			}
		}
		return sessionId;
	}

}
