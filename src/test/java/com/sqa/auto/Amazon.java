package com.sqa.auto;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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

public class Amazon {

	private final static String BASE_URL = "http://amazon.com";

	private Actions action;

	private WebDriver driver;

	private WebDriverWait wait;

	@AfterClass
	public void afterClass() {
		driver.quit();
	}

	@BeforeClass
	public void beforeClass() {
		driver = new FirefoxDriver();
		action = new Actions(driver);
	}

	/**
	 * Test that navigates to amazons department pages using the 2 tier
	 * navigation menu on the home page and verifies the page title is correct
	 *
	 * @param tier1Selection
	 *            xpath of tier 1 department selection
	 * @param tier2Selection
	 *            xpath of tier 2 department selection
	 * @param title
	 *            expected title of page after selecting tier 2 element
	 */
	@Test(dataProvider = "browseDepData")
	public void browseDepartments(String tier1Selection, String tier2Selection, String title) {
		driver.get(BASE_URL);
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		WebElement byDepartment = driver.findElement(By.cssSelector("#nav-link-shopall"));
		action.moveToElement(byDepartment);
		action.perform();
		WebElement tier1 = driver.findElement(By.xpath(tier1Selection));
		action.moveToElement(tier1);
		action.perform();
		driver.findElement(By.xpath(tier2Selection)).click();
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		Assert.assertEquals(driver.getTitle(), title);
	}

	@DataProvider
	public Object[][] browseDepData() {
		return new Object[][] {
				new Object[] { ".//*[@id='nav-flyout-shopAll']/div[2]/span[9]",
						".//*[@id='nav-flyout-shopAll']/div[3]/div[9]/div[1]/a[1]", "Amazon.com: Books" },
				{ ".//*[@id='nav-flyout-shopAll']/div[2]/span[10]",
						".//*[@id='nav-flyout-shopAll']/div[3]/div[10]/div/a[1]", "Amazon.com: Movies & TV" } };
	}

	/**
	 * Test that adds a specified quantity of a particular item to the users
	 * cart and verifies the sum was added
	 *
	 * @param item
	 *            search for
	 * @param quantity
	 *            multiples of the item
	 */
	@Test(dataProvider = "quantityData", enabled = false)
	public void cartQuantity(String item, String quantity) {
		driver.get(BASE_URL);
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
		return new Object[][] { new Object[] { "Apple Watch", "3" }, { "Macbook Pro", "2" }, { "Red Ball", "4" } };
	}

	@DataProvider
	public Object[][] saveForLaterItem() {
		return new Object[][] { new Object[] { "Shampoo", "Garnier Shampoo, Sleek and Shine, 13 Fluid Ounce" } };
	}

	// Test 3
	@Test(dataProvider = "saveForLaterItem", enabled = false)
	public void saveForLaterTest(String searchItem, String linkName) {
		driver.get(BASE_URL);
		wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.titleContains("Amazon.com"));
		driver.findElement(By.id("twotabsearchtextbox")).clear();
		driver.findElement(By.id("twotabsearchtextbox")).sendKeys(searchItem);
		driver.findElement(By.id("twotabsearchtextbox")).sendKeys(Keys.RETURN);
		driver.findElement(By.linkText(linkName)).click();
		driver.findElement(By.cssSelector("#add-to-cart-button")).click();
		String cartNum = driver.findElement(By.id("nav-cart-count")).getText();
		wait.until(ExpectedConditions.titleContains("Amazon.com Shopping Cart"));
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#hlb-view-cart-announce"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(
				By.cssSelector(".a-declarative>input").name("submit.save-for-later.C256UWSTFUX1RV"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".a-row.sc-list-caption")));
		String saveForLaterBeforeDeletion = driver.findElement(By.cssSelector(".a-row.sc-list-caption")).getText();
		driver.findElement(By.name("submit.delete.S256UWSTFUX1RV")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".a-row.sc-list-caption")));
		String saveForLaterAfterDeletion = driver.findElement(By.cssSelector(".a-row.sc-list-caption")).getText();
		Assert.assertNotSame(saveForLaterBeforeDeletion, saveForLaterAfterDeletion);
		String cartNumDecrease = driver.findElement(By.id("nav-cart-count")).getText();
		Assert.assertNotEquals(cartNum, cartNumDecrease);
	}

	// Test 2
	@Test(dataProvider = "totalCost", enabled = false)
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
}