package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import org.testng.annotations.Test;

import static com.orangehrm.utilities.ExtentManager.*;

public class DummyClass extends BaseClass {
    @Test
    public void dummyTest(){
        String title = getDriver().getTitle();
        System.out.println(title);
        logStep("title");
        assert 1==1: "Pass";
        logPassWithScreenshot(title,"homescreen");
    }
}
