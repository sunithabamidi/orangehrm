package com.orangehrm.pages;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private ActionDriver actionDriver;

    public LoginPage(WebDriver driver){
        this.actionDriver =  BaseClass.getActionDriver();
    }

    //Locators
    private By userName_input = By.name("username");
    private By password_input = By.name("password");
    private By login_button = By.xpath("//button[@type='submit']");
    private By error_msg = By.xpath("//p[text()='Invalid credentials']");


    //Method to perform login
    public void login(String username, String password){
        actionDriver.enterText(userName_input, username);
        actionDriver.enterText(password_input, password);
        actionDriver.clickButton(login_button, "Login");
    }

    //Method to perform default login
    public void login(){
        actionDriver.enterText(userName_input, BaseClass.getProp().getProperty("username"));
        actionDriver.enterText(password_input, BaseClass.getProp().getProperty("password"));
        actionDriver.clickButton(login_button, "Login");
    }

    //Method to verify error message
    public boolean errorMessageIsDisplayed(){
        return actionDriver.isDisplayed(error_msg);
    }

    //Method to get text from error message
    public String getErrorMessage(){
        return actionDriver.getText(error_msg);
    }

    //verify the error message
    public boolean verifyErrorMessage(String expected){
       return actionDriver.compareText(error_msg, expected);
    }
}
