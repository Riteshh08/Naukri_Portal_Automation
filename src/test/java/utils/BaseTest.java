package utils;

import pages.NaukriPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public abstract class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected NaukriPage naukriPage;

    @BeforeMethod
    public void setUp() {

        driver = new ChromeDriver();
        driver.manage().window().maximize();

        wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(15)
        );

        naukriPage = new NaukriPage(driver, wait);
    }

    protected String getRequiredEnvironmentVariable(String variableName) {
        String value = System.getenv(variableName);

        Assert.assertNotNull(
                value,
                "Environment variable " + variableName + " is not set."
        );

        Assert.assertFalse(
                value.isBlank(),
                "Environment variable " + variableName + " is empty."
        );

        return value;
    }

    protected void launchWebsiteAndLogin() {
        naukriPage.launchWebsite();

        String username = getRequiredEnvironmentVariable("NAUKRI_USERNAME");
        String password = getRequiredEnvironmentVariable("NAUKRI_PASSWORD");

        naukriPage.login(username, password);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}