package com.orangehrm.base;

import com.aventstack.extentreports.ExtentTest;
import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

public class BaseClass {
    protected static Properties prop;
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
    public static final Logger log = LoggerManager.getLogger(BaseClass.class);

    protected ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);

    public SoftAssert getSoftAssert(){
        return softAssert.get();
    }

    @BeforeSuite
    public void loadConfig() throws Exception {
        //Load the configuration file
        prop = new Properties();
        FileInputStream fis =  new FileInputStream(System.getProperty("user.dir")+"/src/main/resources/config.properties");
        prop.load(fis);
        log.info("Properties file loaded");
    }

    @BeforeMethod
    @Parameters("browser")
    public synchronized void setup(String browser){
        log.info("Setting up WebDriver for: "+this.getClass().getSimpleName());

        //Initialise the webdriver based on browser defined in config.properties file.
        //browser as TestNG parameter
        configureBrowser(browser);
        launchBrowser();

        //initialize action driver for current thread
        actionDriver.set(new ActionDriver(getDriver()));
        log.info("Action Driver instance is created: "+ Thread.currentThread().getId());
    }


    private synchronized void configureBrowser(String browser) {
        //Initialise the webdriver based on browser defined in config.properties file.
//        String browser = prop.getProperty("browser");
        Boolean seleniumGrid = Boolean.parseBoolean(prop.getProperty("seleniumGrid"));
        String gridURL = prop.getProperty("gridURL");

        if (seleniumGrid) {
            try {
                if (browser.equalsIgnoreCase("chrome")) {
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments( "--headless", "--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage", "--window-size=1920,1080");
                    driver.set(new RemoteWebDriver(new URL(gridURL), options));
                    ExtentManager.registerDriver(getDriver());
                } else if (browser.equalsIgnoreCase("firefox")) {
                    FirefoxOptions options = new FirefoxOptions();
                    options.addArguments("--headless");
                    driver.set(new RemoteWebDriver(new URL(gridURL), options));
                    ExtentManager.registerDriver(getDriver());
                } else if (browser.equalsIgnoreCase("edge")) {
                    EdgeOptions options = new EdgeOptions();
                    options.addArguments( "--headless", "--window-size=1920,1080", "--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage");
                    driver.set(new RemoteWebDriver(new URL(gridURL), options));
                    ExtentManager.registerDriver(getDriver());
                } else {
                    throw new IllegalArgumentException("Browser Not Supported: " + browser);
                }
                log.info("RemoteWebDriver instance created for Grid in headless mode: "+ browser);
            } catch (MalformedURLException e) {
                throw new RuntimeException("Invalid Grid URL", e);
            }
        } else {
            if (browser.equalsIgnoreCase("chrome")) {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--headless"); // Run Chrome in headless mode
                options.addArguments("--disable-gpu"); // Disable GPU for headless mode
                options.addArguments("--window-size=1920,1080"); // Set window size
                options.addArguments("--disable-notifications"); // Disable browser notifications
                options.addArguments("--no-sandbox"); // Required for some CI environments like Jenkins
                options.addArguments("--disable-dev-shm-usage"); // Resolve issues in resource-limited environments

                driver.set(new ChromeDriver(options));
                log.info("Chrome Driver instance is created: " + Thread.currentThread().getId());
                ExtentManager.registerDriver(getDriver());
            } else if (browser.equalsIgnoreCase("firefox")) {
                FirefoxOptions options = new FirefoxOptions();
                options.addArguments("--headless"); // Run firefox in headless mode
                options.addArguments("--disable-gpu"); // Disable GPU for headless mode
                options.addArguments("--window-size=1920,1080"); // Set window size
                options.addArguments("--disable-notifications"); // Disable browser notifications
                options.addArguments("--no-sandbox"); // Required for some CI environments like Jenkins
                options.addArguments("--disable-dev-shm-usage"); // Resolve issues in resource-limited environments

                driver.set(new FirefoxDriver(options));
                log.info("firefox Driver instance is created: " + Thread.currentThread().getId());
                ExtentManager.registerDriver(getDriver());
            } else if (browser.equalsIgnoreCase("edge")) {
                EdgeOptions options = new EdgeOptions();
                options.addArguments("--headless=new"); // Run edge in headless mode
                options.addArguments("--disable-gpu"); // Disable GPU for headless mode
                options.addArguments("--window-size=1920,1080"); // Set window size
                options.addArguments("--disable-notifications"); // Disable browser notifications
                options.addArguments("--no-sandbox"); // Required for some CI environments like Jenkins
                options.addArguments("--disable-dev-shm-usage"); // Resolve issues in resource-limited environments

                driver.set(new EdgeDriver(options));
                log.info("Edge Driver instance is created: " + Thread.currentThread().getId());
                ExtentManager.registerDriver(getDriver());
            } else {
                throw new IllegalArgumentException("Browser not supported:" + browser);
            }
        }
    }

    private void launchBrowser(){
        //Implicit wait
        int implicitWait = Integer.parseInt(prop.getProperty("implicitwait"));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

        //maximize the browser
        getDriver().manage().window().maximize();

        //Navigate to url
        try{
            getDriver().get(prop.getProperty("url"));
        }
        catch(Exception e){
            log.info("Unable to navigate to url: "+e.getMessage());
        }
    }


    //getter method for prop
    public static Properties getProp(){
        return prop;
    }

    //Driver setter method
    public void setDriver(ThreadLocal<WebDriver> driver) {
        this.driver = driver;
    }

    //Driver getter method for WebDriver
    public static WebDriver getDriver(){
        if(driver.get() == null){
            log.info("WebDriver is not initialized");
            throw new IllegalStateException("WebDriver is not initialized");
        }
        return driver.get();
    }

    //Driver getter method for action driver
    public static ActionDriver getActionDriver(){
        if(actionDriver.get() == null){
            log.info("ActionDriver is not initialized");
            throw new IllegalStateException("ActionDriver is not initialized");
        }
        return actionDriver.get();
    }

    @AfterMethod
    public synchronized void teardown(){
        //close all the open tabs
        if(getDriver() != null){
            try{
                getDriver().quit();
            } catch(Exception e){
                log.info("Unable to quit the driver: "+e.getMessage());
            }
        }
        log.info("WebDriver instance is closed");
        driver.remove();
        actionDriver.remove();
    }

}
