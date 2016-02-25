package com.sqa.auto;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class Ebay {

	private final String BASE_URL = "www.ebay.com/";

	private WebDriver driver;

	private Actions action;

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
