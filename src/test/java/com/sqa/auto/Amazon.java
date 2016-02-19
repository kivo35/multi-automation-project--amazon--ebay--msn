package com.sqa.auto;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class Amazon {

	private WebDriver driver;

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
	// @Test
	// public void f() {
	// }

	@DataProvider
	public Object[][] quantityData() {
		return new Object[][] { new Object[] { "Test 1", "Apple Watch", "3" } };
	}
}
