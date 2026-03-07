package com.orangehrm.listners;

import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyzer;
import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class TestListener implements ITestListener, IAnnotationTransformer {

    //Triggers when test starts
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ExtentManager.startTest(testName);
        ExtentManager.logStep("Test Started: "+testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ExtentManager.logPass("Test Passed Successfully");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String failureMessage = result.getThrowable().getMessage();
        ExtentManager.logStep(failureMessage);
        if(result.getTestClass().getName().toLowerCase().contains("api")){
            ExtentManager.logFailureApi("Test Failed");
        }
        else ExtentManager.logFail("Test Failed", "Test End: " + testName + " Test Failed");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentManager.logSkip("Test Skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        ITestListener.super.onTestFailedButWithinSuccessPercentage(result);
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        ITestListener.super.onTestFailedWithTimeout(result);
    }

    //Triggers when suit starts
    @Override
    public void onStart(ITestContext context) {
        //Initialise extent reports
        ExtentManager.getReporter();
    }

    @Override
    public void onFinish(ITestContext context) {
        //Flush the extent report
        ExtentManager.endTest();
    }

    @Override
    public boolean isEnabled() {
        return ITestListener.super.isEnabled();
    }

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
}
