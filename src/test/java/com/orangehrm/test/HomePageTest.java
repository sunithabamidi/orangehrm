package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import static com.orangehrm.utilities.ExtentManager.*;

import com.orangehrm.utilities.DataProviders;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HomePageTest extends BaseClass {
    private HomePage homePage;
    private LoginPage loginPage;

    @BeforeMethod
    public void setupPages(){
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
    }

    @Test (dataProvider = "validLogin" , dataProviderClass = DataProviders.class)
    public void verifyOrangeHRMlogo(String username, String password){
        loginPage.login(username,password);
        if(homePage.isHamburgerVisible()) homePage.clickHamburgerIcon();
        Assert.assertTrue(homePage.verifyOrangeHRMlogo());
        logPassWithScreenshot(getDriver().getTitle(),"logo");
    }

}
