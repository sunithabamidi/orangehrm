package com.orangehrm.actiondriver;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.orangehrm.utilities.ExtentManager.logPass;

public class ActionDriver {
    private WebDriver driver;
    private WebDriverWait wait;
    public static final Logger log = BaseClass.log;

    public ActionDriver(WebDriver driver){
        this.driver = driver;
        int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicitwait"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
    }

    //Method to click
    public void click(By by){
        try {
            waitForElementToBeClickable(by);
            driver.findElement(by).click();
            log.info("Clicked on element: "+ by);
        } catch (Exception e) {
            ExtentManager.logFail("Unable to click the element", "Unable to click the element"+getElementDescription(by));
            log.error("Unable to click the element: "+ e.getMessage());
        }
    }

    public void click(By by, String message){
       try {
            waitForElementToBeClickable(by);
            driver.findElement(by).click();
           ExtentManager.logStep("Clicked "+message);
           log.info("Clicked on element: "+ by);
        } catch (Exception e) {
            log.error("Unable to click the element: "+ e.getMessage());
        }
    }

    public void clickButton(By by, String message){
        try {
            waitForElementToBeClickable(by);
            driver.findElement(by).click();
            log.info("Clicked "+message+" button");
        } catch (Exception e) {
            log.error("Unable to click the button: "+ e.getMessage());
        }
    }

    //method to enter text in an input filed
    public void enterText(By by, String text){
        try {
            waitForElementToBeVisible(by);
            driver.findElement(by).clear();
            driver.findElement(by).sendKeys(text);
            log.info("Entered "+text);
        } catch (Exception e) {
            log.error("Unable to enter the text: "+ e.getMessage());
        }
    }

    //method to get text from an input field
    public String getText(By by){
        try {
            waitForElementToBeVisible(by);
            return driver.findElement(by).getText();
        } catch (Exception e) {
            log.error("unable to get text "+e.getMessage());
            return "";
        }
    }

    //compare text and return boolean
    public boolean compareText(By by, String expectedText){
        try {
            waitForElementToBeVisible(by);
            String actualText = driver.findElement(by).getText();
            if(expectedText.equalsIgnoreCase(actualText)){
                log.info("Texts are matching:"+actualText+" equals "+expectedText);
                return true;
            }
            else log.info("Texts are not matching: "+actualText+" not equals "+expectedText);
        } catch (Exception e) {
            log.error("unable to compare text: "+e.getMessage());
        }
        return false;
    }

    //Method to check if an element is displayed
    public boolean isDisplayed(By by){
        try {
            waitForElementToBeVisible(by);
            return driver.findElement(by).isDisplayed();
        } catch (Exception e) {
            log.error("Element is not displayed: "+e.getMessage());
            return false;
        }
    }

    //scroll to an element
    public void scrollToElement(By by){
        try {
            JavascriptExecutor jse = (JavascriptExecutor) driver;
            jse.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(by));
        } catch (Exception e) {
            log.error("Unable to scroll to the element: "+e.getMessage());
        }
    }

    //wait for element to be clickable
    private void waitForElementToBeClickable(By by){
        waitForElementToBeVisible(by);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(by));
        } catch (Exception e) {
            log.error("Element is not clickable: "+ e.getMessage());
        }
    }

    //wait for element to be visible
    private void waitForElementToBeVisible(By by){
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            log.error("Element is not visible: "+ e.getMessage());
        }
    }

    //Utility method to check String is not NULL or Empty
    private boolean isNotEmpty(String string){
        return string != null && string.isEmpty();
    }

    //Utility method to truncate long string
    private String truncate(String string, int maxLen){
        if(string == null || string.length()<= maxLen) return string;
        else return string.substring(0,maxLen)+"...";
    }

    //Utility method to apply border
    public void applyBorder(By by, String color){
        try {
            //Location Element
            WebElement element = driver.findElement(by);
            //Apply the border
            String script="arguments[0].apply.border='3px solid "+color+"'";
            JavascriptExecutor jse =  (JavascriptExecutor) driver;
            jse.executeScript(script, element);
            log.info("Applied "+color+" color border to element:"+ getElementDescription(by));
        } catch (Exception e) {
            log.warn("failed to apply the border to element "+getElementDescription(by));
        }
    }

    //Method to get description of an element using BY Locator
    public String getElementDescription(By by){
        if(driver == null) return "driver is null";
        if(by == null) return "locator is null";

        try {
            WebElement element = driver.findElement(by);
            String name = element.getDomAttribute("name");
            String id = element.getDomAttribute("id");
            String text = element.getText();
            String className = element.getDomAttribute("class");
            String placeholder = element.getDomAttribute("placeholder");

            if(isNotEmpty(name)) return "Element with name: "+ name;
            else if(isNotEmpty(id)) return "Element with id: "+ id;
            else if(isNotEmpty(text)) return "Element with text: "+ truncate(text, 50);
            else if(isNotEmpty(className)) return "Element with className: "+ className;
            else if(isNotEmpty(placeholder)) return "Element with placeholder: "+ placeholder;
        } catch (Exception e) {
            log.error("unable to describe element: "+ e.getMessage());
        }
        return "unable to describe element";
    }
}
