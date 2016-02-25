package com.sqa.auto;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.sqa.kv.util.helper.SelUtil;
import com.sqa.kv.util.helper.SelUtil.STRATEGY;

public class MSN
{
	private final static String BASE_URL = "http://www.msn.com/";
	private WebDriver driver;
	private WebDriverWait wait;
	private Actions action;
	private JavascriptExecutor js;

	@AfterClass
	public void afterClass()
	{
		this.driver.quit();
	}

	@BeforeClass
	public void beforeClass()
	{
		this.driver = new FirefoxDriver();
		this.action = new Actions(this.driver);
	}

	// Test 2
	@Test(dataProvider = "topic")
	public void categoryTopics(String category, String topic)
	{
		this.driver.get(this.BASE_URL);
		this.driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		this.wait = new WebDriverWait(this.driver, 10);
		SelUtil.gotoAndClick(this.driver, category, STRATEGY.CLASSNAME);
		SelUtil.gotoAndClick(this.driver, topic, STRATEGY.TEXT);
		this.wait.until(ExpectedConditions.visibilityOfElementLocated(By
				.cssSelector(".skyline.headline-template.loaded.layout-small")));

		WebElement newsPanel = this.driver.findElement(By.cssSelector(".newlist.list-primary"));
		List<WebElement> articles = newsPanel.findElements(By.tagName("a"));
		double range = articles.size() % 6;

		Assert.assertEquals(articles.size(), 6, range);
		System.out.println("The article names are listed below:\n");
		for (WebElement article : articles)
		{
			System.out.println(article.getText());
		}
	}

	// Test 3 (needs to be fixed)
	@Test
	public void changeWeatherPanel()
	{
		this.driver.get(BASE_URL);
		this.driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		WebElement elem = this.driver.findElement(By.xpath(".//*[@id='main']/div[5]/ul/li[2]/div/div/a"));
		String previousLocation = this.driver.findElement(By.cssSelector(".weacity>span")).getText();
		this.js = (JavascriptExecutor) this.driver;
		this.js.executeScript("arguments[0].click();", elem);
		this.driver.findElement(By.cssSelector(".add-loc-as-container>input")).sendKeys("Berlin");
		SelUtil.gotoAndClick(this.driver, ".searchbtn", STRATEGY.CSS);
		SelUtil.gotoAndClick(this.driver, "#celsius", STRATEGY.CSS);
		SelUtil.gotoAndClick(this.driver, ".donebutton", STRATEGY.CSS);

		String currentLocation = this.driver.findElement(By.cssSelector(".weacity>span")).getText();

		Assert.assertNotEquals(currentLocation, previousLocation);
	}

	// Test 1
	@Test(dataProvider = "epComments")
	public void editorsPickComments(String id, double comCount)
	{
		this.driver.get(this.BASE_URL);
		this.driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		this.wait = new WebDriverWait(this.driver, 10);
		WebElement first = this.driver.findElement(By.xpath(id));
		this.driver.get(first.getAttribute("href"));
		this.driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		String conv = this.wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span#comment-count"))).getText();

		// leave only numeric values in String
		conv = conv.replaceAll("\\D+", "");
		comCount = Double.parseDouble(conv);

		// see if the number of comments within 10-50 range
		Assert.assertEquals(comCount, 50, valRange(comCount, comCount));
	}

	@DataProvider
	public Object[][] epComments()
	{
		return new Object[][] { new Object[] { ".//*[@id='main']/div[5]/ul/li[13]/div/ul/li[1]/a", 0 } };
	}

	@DataProvider
	public Object[][] topic()
	{
		String[] data = { "entertainment", "Movies" };
		return new Object[][] { data };
	}

	public double valRange(double range, double act)
	{
		if (range >= 10 && range <= 50)
		{
			range = 50 - range;
		}
		else
		{
			range = 0;
			System.out.println("Test failed. The number of comments should be within 10-50 range, currently returns "
					+ act);
		}
		return range;
	}
}