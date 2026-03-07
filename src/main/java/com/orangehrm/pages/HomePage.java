package com.orangehrm.pages;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {

    private ActionDriver actionDriver;

    public HomePage(WebDriver driver){
        this.actionDriver =  BaseClass.getActionDriver();
    }

    private By admin_tab = By.xpath("//span[text()='Admin']");
    private By profile = By.xpath("//span[@class='oxd-userdropdown-tab']");
    private By logout_button = By.xpath("//a[text()='Logout']");
    private By logo = By.xpath("//div[contains(@class,'oxd-brand-banner')]");
    private By hamburger_icon = By.xpath("//i[contains(@class,'hamburger')]");
    private By close_menu_icon = By.xpath("//i[contains(@class,'close')]");


    public boolean isHamburgerVisible(){
        return actionDriver.isDisplayed(hamburger_icon);
    }

    public void clickHamburgerIcon(){
        actionDriver.click(hamburger_icon);
    }

    public boolean isAdminTabvisible(){
      return  actionDriver.isDisplayed(admin_tab);
    }

    public boolean verifyOrangeHRMlogo(){
        return actionDriver.isDisplayed(logo);
    }

    public boolean isCloseMenuVisible(){
        return actionDriver.isDisplayed(close_menu_icon);
    }

    public void clickClose(){
        actionDriver.click(close_menu_icon);
    }

    public void logout(){
       actionDriver.click(profile);
       actionDriver.click(logout_button, "logout");
    }
}
