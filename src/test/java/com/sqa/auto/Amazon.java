package com.sqa.auto;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Properties;
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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class Amazon
{

	private final static String BASE_URL = "http://amazon.com";

	private WebDriver driver;

	private WebDriverWait wait;

	private Properties amazonProps;

	private String searchField;

	private String addToCart;

	private String saveForLater;

	private String cartCount;

	@AfterClass
	public void afterClass()
	{
		this.driver.quit();
	}

	@BeforeClass
	public void beforeClass() throws IOException
	{
		// System properties = new
		this.driver = new FirefoxDriver();
		this.wait = new WebDriverWait(this.driver, 10);
		Properties props = new Properties();
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("amazon.properties");
		props.load(in);

		this.searchField = props.getProperty("idSearchField");
		this.addToCart = props.getProperty("cssAddToCart");
		this.saveForLater = props.getProperty("cssSaveForLater");
		this.cartCount = props.getProperty("idCartCount");
	}

	// Test 2
	@Test(dataProvider = "quantityData", priority = 2)
	public void cartQuantity(String label, String item, String quantity, String total, String location)
	{
		System.out.println(label);
		this.driver.findElement(By.id(this.searchField)).sendKeys(item);
		this.driver.findElement(By.cssSelector(".nav-input")).click();
		this.driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		this.driver.findElement(By.xpath(location)).click();
		this.driver.findElement(By.cssSelector("#quantity")).sendKeys(quantity);
		this.driver.findElement(By.cssSelector(this.addToCart)).click();

		if (item.equals("Macbook Pro"))
		{
			try
			{
				this.driver.findElement(By.cssSelector(".a-popover-wrapper"));
				this.driver.findElement(By.cssSelector("#siNoCoverage-announce")).click();
			}
			catch (NoSuchElementException e)
			{
				return;
			}
		}
		WebElement cartNum = this.driver.findElement(By.id(this.cartCount));
		System.out.println(cartNum.getText());
		Assert.assertEquals(cartNum.getText(), total);
	}

	@BeforeMethod
	public void launchBrowser()
	{
		this.driver.get(BASE_URL);
		this.wait.until(ExpectedConditions.titleContains("Amazon.com"));
	}

	@DataProvider
	public Object[][] quantityData()
	{
		return new Object[][] {
				new Object[] { "Test 1 - Add 3 Apple Watch", "Apple Watch", "3", "6",
						".//*[@id='result_3']/div/div/div/div[2]/div[2]/a/h2" },
				{ "Test 2 - Add 2 Macbook", "Macbook Pro", "2", "8", ".//*[@id='result_0']/div/div[3]/div[1]/a/h2" },
				{ "Test 3 - add 4 ping pong ball", "Ball", "4", "12",
						".//*[@id='result_0']/div/div/div/div[2]/div[2]/a/h2" } };
	}

	@DataProvider
	public Object[][] saveForLaterItem()
	{
		return new Object[][] { new Object[] { "Shampoo", "Garnier Shampoo, Sleek and Shine, 13 Fluid Ounce" } };
	}

	// Test 3
	@Test(dataProvider = "saveForLaterItem", priority = 3)
	public void saveForLaterTest(String searchItem, String linkName) throws IOException
	{
		this.driver.findElement(By.id(this.searchField)).clear();
		this.driver.findElement(By.id(this.searchField)).sendKeys(searchItem);
		this.driver.findElement(By.id(this.searchField)).sendKeys(Keys.RETURN);
		this.driver.findElement(By.linkText(linkName)).click();
		this.driver.findElement(By.cssSelector(this.addToCart)).click();
		String cartNum = this.driver.findElement(By.id(this.cartCount)).getText();
		this.wait.until(ExpectedConditions.titleContains("Amazon.com Shopping Cart"));
		this.wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#hlb-view-cart-announce"))).click();
		this.wait.until(ExpectedConditions.elementToBeClickable(
				By.cssSelector(".a-declarative>input").name("submit.save-for-later.C256UWSTFUX1RV"))).click();
		this.wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(this.saveForLater)));
		String saveForLaterBeforeDeletion = this.driver.findElement(By.cssSelector(this.saveForLater)).getText();
		this.driver.findElement(By.name("submit.delete.S256UWSTFUX1RV")).click();
		this.wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(this.saveForLater)));
		String saveForLaterAfterDeletion = this.driver.findElement(By.cssSelector(this.saveForLater)).getText();
		Assert.assertNotSame(saveForLaterBeforeDeletion, saveForLaterAfterDeletion);
		String cartNumDecrease = this.driver.findElement(By.id(this.cartCount)).getText();
		Assert.assertNotEquals(cartNum, cartNumDecrease);
	}

	// Test 1
	@Test(dataProvider = "totalCost", priority = 1)
	public void threeItemsCostTest(String searchItem, String linkName, String quantity, String total)
	{
		this.driver.findElement(By.id(this.searchField)).clear();
		this.driver.findElement(By.id(this.searchField)).sendKeys(searchItem);
		this.driver.findElement(By.id(this.searchField)).sendKeys(Keys.RETURN);
		this.driver.findElement(By.linkText(linkName)).click();
		this.driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		this.driver.findElement(By.cssSelector("#quantity")).sendKeys(quantity);
		this.driver.findElement(By.cssSelector(this.addToCart)).click();
		this.wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#nav-cart"))).click();
		this.wait.until(ExpectedConditions.titleContains("Amazon.com Shopping Cart"));
		WebElement cartTotal = this.driver.findElement(
				By.cssSelector(".a-size-medium.a-color-price.sc-price.sc-white-space-nowrap.sc-price-sign"));
		Assert.assertEquals(total, cartTotal.getText());
	}

	@DataProvider
	public Object[][] totalCost()
	{
		return new Object[][] {
				new Object[] { "speakers", "AmazonBasics USB Powered Computer Speakers (A100)", "3", "$41.97" } };
	}
}