package com.orangehrm.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromBase64String;

public class ExtentManager {
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test= new ThreadLocal<>();
    private static Map<Long, WebDriver> driverMap = new HashMap();

    //Initialize Extent Report
    public synchronized static ExtentReports getReporter(){
        if(extent == null){
          String reportPath = System.getProperty("user.dir")+"/src/test/resources/ExtentReports/ExtentReport.html";
            ExtentSparkReporter spark= new ExtentSparkReporter(reportPath);
            spark.config().setReportName("Automation Test Report");
            spark.config().setDocumentTitle("Orange HRM Report");
            extent= new ExtentReports();

            //Adding System Information
            extent.setSystemInfo("Operating System", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("User Name", System.getProperty("user.name"));

            extent.attachReporter(spark);
        }
        return extent;
    }

    //Start the Test
    public synchronized static ExtentTest startTest(String testName){
        ExtentTest extentTest =extent.createTest(testName);
        test.set(extentTest);
        return extentTest;
    }

    //End a Test
    public static void endTest(){
        getReporter().flush();
    }

    //Get Current Threads Test Name
    public synchronized static ExtentTest getTest(){
       return test.get();
    }

    //To get the current Test
    public static String getTestName(){
        ExtentTest currentTest = getTest();
        if(currentTest !=null){
            return currentTest.getModel().getName();
        }
        else return "Not test is Currently Active for this Thread";
    }

    //Log Steps
    public static void logStep(String message){
        getTest().info(message);
    }

    //Log Step with screenshot
    public static void logStepWithScreenshot(String message, String screenshotMessage){
        getTest().info(message);
        //Screenshot Method call
        attachScreenshot(screenshotMessage);
    }

    //Log Pass
    public static void logPass(String message){
        getTest().pass(message);
    }

    //Log Pass with screenshot
    public static void logPassWithScreenshot(String message, String screenshotMessage){
        getTest().pass(message);
        //Screenshot Method call
        attachScreenshot(screenshotMessage);
    }

    //log Failure
    public static void logFail(String message, String screenshotMessage){
        String colorMessage = String.format("<span style='color:red'>%s</span>", message);
        getTest().fail(colorMessage);
        //Screenshot Method call
        attachScreenshot(screenshotMessage);
    }

    //log Failure for API
    public static void logFailureApi(String message){
        String colorMessage = String.format("<span style='color:red'>%s</span>", message);
        getTest().fail(colorMessage);
    }

    //log skip
    public static void logSkip(String message){
        getTest().skip(message);
    }

    //Take screenshot with date and time in the file
    public synchronized static String takeScreenshot(String screenshotName){
        WebDriver driver = driverMap.get(Thread.currentThread().getId());
        TakesScreenshot ts = (TakesScreenshot)driver;
        File src = ts.getScreenshotAs(OutputType.FILE);
        //Format date and time for filename
        String timeStamp = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format( new Date());

        //Saving the screenshot to a file
        String destinationPath =System.getProperty("user.dir")+"/src/test/resources/screenshots/"+screenshotName+"_"+timeStamp+".png";
        File screenshot = new File(destinationPath);
        try {
            FileUtils.copyFile(src, screenshot);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Convert Screenshot to base64 to embed in the Extent Report
        String base64Format =convertToBase64(src);
        return base64Format;
    }


    //Convert Screenshot to Base64 Format
    public static String convertToBase64(File screenshot) {
        String base64Format ="";
        //Read the File content into a byte Array
        byte[] fileContent = null;
        try {
            fileContent = FileUtils.readFileToByteArray(screenshot);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //convert the byte array to base64 String
        base64Format = Base64.getEncoder().encodeToString(fileContent);

        return base64Format;
    }

    //attach screenshot to report using Base64
    public synchronized static void attachScreenshot(String message){
        try {
            String screenshotBase64 = takeScreenshot(getTestName());
            getTest().info(message, createScreenCaptureFromBase64String(screenshotBase64).build());
        } catch (Exception e) {
            getTest().fail("Failed to attach screenshot: "+message);
            throw new RuntimeException(e);
        }

    }

    //Register WebDriver for current thread
    public static void registerDriver(WebDriver driver){
        driverMap.put(Thread.currentThread().getId(), driver);
    }
}
