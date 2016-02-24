/**
 *
 */
package com.sqa.kv.util.helper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * @author kvoitau
 *
 */
public class Login
{
	@FindBy(id = "username")
	private WebElement userField;

	@FindBy(id = "password")
	private WebElement passwordField;

	@FindBy(id = "Login")
	private WebElement loginButton;

	public Login(WebDriver driver)
	{
		PageFactory.initElements(driver, this);
	}

	public void login(String email, String password)
	{
		this.userField.sendKeys(email);
		this.passwordField.sendKeys(password);
		this.loginButton.click();
	}
}
