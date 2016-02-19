package com.sqa.auto;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class Amazon
{
	private WebDriver driver;
	private WebDriverWait wait;
	private String BASE_URL = "http://www.amazon.com/";

	@AfterClass
	public void afterClass()
	{
		this.driver.quit();
	}

	@BeforeClass
	public void beforeClass()
	{
		this.driver = new FirefoxDriver();
		this.driver.get(this.BASE_URL);
	}

	@Test(dataProvider = "totalCost")
	public void threeItemsCostTest(String searchItem, String linkName, String total)
	{
		this.wait = new WebDriverWait(this.driver, 10);
		this.wait.until(ExpectedConditions.titleContains("Amazon.com"));
		this.driver.findElement(By.id("twotabsearchtextbox")).clear();
		this.driver.findElement(By.id("twotabsearchtextbox")).sendKeys(searchItem);
		this.driver.findElement(By.id("twotabsearchtextbox")).sendKeys(Keys.RETURN);
		this.driver.findElement(By.linkText(linkName)).click();
		this.driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		this.driver.findElement(By.cssSelector("#add-to-cart-button")).click();
		this.wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#nav-cart")))
				.click();
		this.wait.until(ExpectedConditions.titleContains("Amazon.com Shopping Cart"));
		WebElement cartTotal = this.driver
				.findElement(By
						.cssSelector(".a-size-medium.a-color-price.sc-price.sc-white-space-nowrap.sc-price-sign"));
		Assert.assertEquals(total, cartTotal.getText());
	}

	@DataProvider
	public Object[][] totalCost()
	{
		return new Object[][] {
				new Object[] { "toy",
						"Manhattan Toy Winkel Rattle and Sensory Teether Activity Toy", "$8.59" },
				{ "map", "World Map Vintage Style Poster Print", "$12.17" },
				{ "vacuum cleaner", "Soniclean Galaxy 1150 Canister Vacuum Cleaner", "$362.16" } };
	}
}
