package com.sqa.auto;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class Ebay
{

	private final String BASE_URL = "www.ebay.com/";

	private WebDriver driver;

	private Actions action;

	private WebDriverWait wait;

	@Test
	public void addToWatchList()
	{
		this.wait = new WebDriverWait(this.driver, 10);
		boolean result = false;
		this.driver.get(this.BASE_URL);
		this.driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		this.driver.findElement(By.cssSelector("#gh-ug>a")).click();
		this.driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		this.driver.findElement(By.cssSelector("#userid")).sendKeys("chance+testing@rev.com");
		this.driver.findElement(By.cssSelector("#pass")).sendKeys("Test@123");
		this.driver.findElement(By.cssSelector("#sgnBt")).click();
		this.driver.findElement(By.cssSelector("#gh-ac")).sendKeys("comics");
		this.driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		this.driver.findElement(By.cssSelector("#gh-btn")).click();
		String selected = this.driver.findElement(By.cssSelector(".vip")).getText();
		this.driver.findElement(By.cssSelector(".vip")).click();
		this.driver.findElement(By.linkText("Add to watch list")).click();
		this.wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#w1-5-_lmsg>a"))).click();
		List<WebElement> watchItems = this.driver.findElements(By.cssSelector(".vip.item-title"));
		for (WebElement item : watchItems)
		{
			if (item.getText().equals(selected))
			{
				result = true;
			}
		}
		Assert.assertEquals(result, true);
	}

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

	@Test
	public void dropDownAddToCart()
	{
		this.driver.get(this.BASE_URL);
		this.driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		WebElement tier1 = this.driver.findElement(By
				.xpath(".//*[@id='navigationFragment']/div/table/tbody/tr/td[9]/a"));
		this.action.moveToElement(tier1);
		this.action.perform();
		this.driver.findElement(
				By.xpath(".//*[@id='navigationFragment']/div/table/tbody/tr/td[9]/div[2]/div[1]/ul[2]/li[3]/a"))
				.click();
		this.driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		this.driver.findElement(By.xpath(".//*[@id='mainContent']/div[3]/ul/li/ul/li[3]/a")).click();
		this.driver.findElement(By.xpath(".//*[@id='item1c57c19788']/h3/a")).click();
		this.driver.findElement(By.xpath(".//*[@id='isCartBtn_btn']")).click();
		WebElement cartTotal = this.driver.findElement(By.xpath(".//*[@id='gh-cart-n']"));
		Assert.assertEquals(cartTotal.getText(), "1");
	}
}
