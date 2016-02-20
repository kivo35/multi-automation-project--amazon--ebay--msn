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

public class Amazon {

	private WebDriver driver;

	private WebDriverWait wait;

	private String baseUrl;

	@AfterClass
	public void afterClass() {
		driver.quit();
	}

	@BeforeClass
	public void beforeClass() {
		driver = new FirefoxDriver();
		baseUrl = "http://wwww.amazon.com/";
	}

	@Test(dataProvider = "quantityData")
	public void cartQuantity(String label, String item, String quantity) {
		driver.get(baseUrl);
		System.out.println(label);
		driver.findElement(By.cssSelector("#twotabsearchtextbox")).sendKeys(item);
		driver.findElement(By.cssSelector(".nav-input")).click();
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.findElement(By.xpath(".//*[@id='result_1']/div/div/div/div[2]/div[1]/a/h2")).click();
		driver.findElement(By.cssSelector("#quantity")).sendKeys(quantity);
		driver.findElement(By.cssSelector("#add-to-cart-button")).click();
		WebElement cartNum = driver.findElement(By.id("nav-cart-count"));
		System.out.println(cartNum.getText());
		Assert.assertEquals(cartNum.getText(), quantity);
	}

	@DataProvider
	public Object[][] quantityData() {
		return new Object[][] { new Object[] { "Test 1", "Apple Watch", "3" } };
	}

	@Test(dataProvider = "totalCost")
	public void threeItemsCostTest(String searchItem, String linkName, String total) {
		wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.titleContains("Amazon.com"));
		driver.findElement(By.id("twotabsearchtextbox")).clear();
		driver.findElement(By.id("twotabsearchtextbox")).sendKeys(searchItem);
		driver.findElement(By.id("twotabsearchtextbox")).sendKeys(Keys.RETURN);
		driver.findElement(By.linkText(linkName)).click();
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.findElement(By.cssSelector("#add-to-cart-button")).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#nav-cart"))).click();
		wait.until(ExpectedConditions.titleContains("Amazon.com Shopping Cart"));
		WebElement cartTotal = driver.findElement(
				By.cssSelector(".a-size-medium.a-color-price.sc-price.sc-white-space-nowrap.sc-price-sign"));
		Assert.assertEquals(total, cartTotal.getText());
	}

	@DataProvider
	public Object[][] totalCost() {
		return new Object[][] {
				new Object[] { "toy", "Manhattan Toy Winkel Rattle and Sensory Teether Activity Toy", "$8.59" },
				{ "map", "World Map Vintage Style Poster Print", "$12.17" },
				{ "vacuum cleaner", "Soniclean Galaxy 1150 Canister Vacuum Cleaner", "$362.16" } };
	}
}
