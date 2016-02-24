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

public class Ebay {

	private final String BASE_URL = "www.ebay.com/";

	private WebDriver driver;

	private Actions action;

	private WebDriverWait wait;

	@Test
	public void addToWatchList() {
		wait = new WebDriverWait(driver, 10);
		boolean result = false;
		driver.get(BASE_URL);
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.findElement(By.cssSelector("#gh-ug>a")).click();
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.findElement(By.cssSelector("#userid")).sendKeys("chance+testing@rev.com");
		driver.findElement(By.cssSelector("#pass")).sendKeys("Test@123");
		driver.findElement(By.cssSelector("#sgnBt")).click();
		driver.findElement(By.cssSelector("#gh-ac")).sendKeys("comics");
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.findElement(By.cssSelector("#gh-btn")).click();
		String selected = driver.findElement(By.cssSelector(".vip")).getText();
		driver.findElement(By.cssSelector(".vip")).click();
		driver.findElement(By.linkText("Add to watch list")).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#w1-5-_lmsg>a"))).click();
		List<WebElement> watchItems = driver.findElements(By.cssSelector(".vip.item-title"));
		for (WebElement item : watchItems) {
			if (item.getText().equals(selected)) {
				result = true;
			}
		}
		Assert.assertEquals(result, true);
	}

	@AfterClass
	public void afterClass() {
		driver.quit();
	}

	@BeforeClass
	public void beforeClass() {
		driver = new FirefoxDriver();
		action = new Actions(driver);
	}

	@Test
	public void dropDownAddToCart() {
		driver.get(BASE_URL);
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		WebElement tier1 = driver.findElement(By.xpath(".//*[@id='navigationFragment']/div/table/tbody/tr/td[9]/a"));
		action.moveToElement(tier1);
		action.perform();
		driver.findElement(
				By.xpath(".//*[@id='navigationFragment']/div/table/tbody/tr/td[9]/div[2]/div[1]/ul[2]/li[3]/a"))
				.click();
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.findElement(By.xpath(".//*[@id='mainContent']/div[3]/ul/li/ul/li[3]/a")).click();
		driver.findElement(By.xpath(".//*[@id='item1c57c19788']/h3/a")).click();
		driver.findElement(By.xpath(".//*[@id='isCartBtn_btn']")).click();
		WebElement cartTotal = driver.findElement(By.xpath(".//*[@id='gh-cart-n']"));
		Assert.assertEquals(cartTotal.getText(), "1");
	}
}
