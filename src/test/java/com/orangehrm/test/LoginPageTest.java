package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.orangehrm.utilities.ExtentManager.logPassWithScreenshot;

public class LoginPageTest extends BaseClass {
    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setupPages(){
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
    }

    @Test (dataProvider = "validLogin" , dataProviderClass = DataProviders.class)
    public void validLoginTest(String username, String password){
        loginPage.login(username, password);
        if(homePage.isHamburgerVisible()) homePage.clickHamburgerIcon();
        Assert.assertTrue( homePage.isAdminTabvisible(),"Admin tab should be visible");
        logPassWithScreenshot("Admin tab is visible","Admin Tab");
        if(homePage.isCloseMenuVisible()) homePage.clickClose();
        homePage.logout();
    }


    @Test (dataProvider = "invalidLogin" , dataProviderClass = DataProviders.class)
    public void invalidLoginTest(String username, String password){
        loginPage.login(username, password);
        Assert.assertTrue(loginPage.verifyErrorMessage("Invalid Credentials"),"Error message should be Invalid Credentials");
        logPassWithScreenshot("Invalid Credentials error message","login screen");
    }
}
