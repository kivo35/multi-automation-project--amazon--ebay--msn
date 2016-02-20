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

	private final static String BASE_URL = "http://amazon.com";

	private WebDriver driver;

	private WebDriverWait wait;

	@AfterClass
	public void afterClass()
	{
		this.driver.quit();
	}

	@BeforeClass
	public void beforeClass()
	{
		this.driver = new FirefoxDriver();
	}

	// Test 1
	@Test(dataProvider = "quantityData", enabled = false)
	public void cartQuantity(String label, String item, String quantity)
	{
		this.driver.get(BASE_URL);
		System.out.println(label);
		this.driver.findElement(By.cssSelector("#twotabsearchtextbox")).sendKeys(item);
		this.driver.findElement(By.cssSelector(".nav-input")).click();
		this.driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		this.driver.findElement(By.xpath(".//*[@id='result_1']/div/div/div/div[2]/div[1]/a/h2"))
				.click();
		this.driver.findElement(By.cssSelector("#quantity")).sendKeys(quantity);
		this.driver.findElement(By.cssSelector("#add-to-cart-button")).click();
		WebElement cartNum = this.driver.findElement(By.id("nav-cart-count"));
		System.out.println(cartNum.getText());
		Assert.assertEquals(cartNum.getText(), quantity);
	}

	@DataProvider
	public Object[][] quantityData()
	{
		return new Object[][] { new Object[] { "Test 1", "Apple Watch", "3" },
				{ "Test 2", "Macbook Pro", "2" }, { "Test 3", "Red Ball", "4" } };
	}

	@DataProvider
	public Object[][] saveForLaterItem()
	{
		return new Object[][] { new Object[] { "Shampoo",
				"Garnier Shampoo, Sleek and Shine, 13 Fluid Ounce" } };
	}

	// Test 3
	@Test(dataProvider = "saveForLaterItem")
	public void saveForLaterTest(String searchItem, String linkName)
	{
		this.driver.get(BASE_URL);
		this.wait = new WebDriverWait(this.driver, 10);
		this.wait.until(ExpectedConditions.titleContains("Amazon.com"));
		this.driver.findElement(By.id("twotabsearchtextbox")).clear();
		this.driver.findElement(By.id("twotabsearchtextbox")).sendKeys(searchItem);
		this.driver.findElement(By.id("twotabsearchtextbox")).sendKeys(Keys.RETURN);
		this.driver.findElement(By.linkText(linkName)).click();
		this.driver.findElement(By.cssSelector("#add-to-cart-button")).click();
		String cartNum = this.driver.findElement(By.id("nav-cart-count")).getText();
		this.wait.until(ExpectedConditions.titleContains("Amazon.com Shopping Cart"));
		this.wait.until(
				ExpectedConditions.elementToBeClickable(By.cssSelector("#hlb-view-cart-announce")))
				.click();
		this.wait.until(
				ExpectedConditions.elementToBeClickable(By.cssSelector(".a-declarative>input")
						.name("submit.save-for-later.C256UWSTFUX1RV"))).click();
		this.wait.until(ExpectedConditions.visibilityOfElementLocated(By
				.cssSelector(".a-row.sc-list-caption")));
		String saveForLaterBeforeDeletion = this.driver.findElement(
				By.cssSelector(".a-row.sc-list-caption")).getText();
		this.driver.findElement(By.name("submit.delete.S256UWSTFUX1RV")).click();
		this.wait.until(ExpectedConditions.visibilityOfElementLocated(By
				.cssSelector(".a-row.sc-list-caption")));
		String saveForLaterAfterDeletion = this.driver.findElement(
				By.cssSelector(".a-row.sc-list-caption")).getText();
		Assert.assertNotSame(saveForLaterBeforeDeletion, saveForLaterAfterDeletion);
		String cartNumDecrease = this.driver.findElement(By.id("nav-cart-count")).getText();
		Assert.assertNotEquals(cartNum, cartNumDecrease);
		;
	}

	// Test 2
	@Test(dataProvider = "totalCost", enabled = false)
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
}
