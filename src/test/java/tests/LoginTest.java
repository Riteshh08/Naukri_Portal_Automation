package tests;

import utils.BaseTest;
import utils.TestData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    // TODO: Replace with the actual invalid-login error locator.
	private static final By LOGIN_ERROR =
	        By.cssSelector(".commonErrorMsg");

    @Test
    public void validLogin() {
    	naukriPage.launchWebsite();

        String username = getRequiredEnvironmentVariable("NAUKRI_USERNAME");
        String password = getRequiredEnvironmentVariable("NAUKRI_PASSWORD");

        naukriPage.login(username, password);

        // TODO: Replace with a stable post-login locator or URL condition.
        By loggedInElement = By.xpath(
                "//*[contains(translate(normalize-space(.),"
                        + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'profile')"
                        + " or contains(translate(normalize-space(.),"
                        + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'my naukri')]"
        );

        boolean loggedIn = wait.until(
                ExpectedConditions.or(
                        ExpectedConditions.visibilityOfElementLocated(loggedInElement),
                        ExpectedConditions.urlContains("homepage")
                )
        );

        Assert.assertTrue(
                loggedIn,
                "Expected the user to be logged in."
        );
    }

    @Test
    public void invalidLoginShowsError() {
        String username = getRequiredEnvironmentVariable("NAUKRI_USERNAME");

        naukriPage.launchWebsite();
        naukriPage.login(username, TestData.INVALID_PASSWORD);

        WebElement errorMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(LOGIN_ERROR)
        );

        Assert.assertFalse(
                errorMessage.getText().trim().isEmpty(),
                "Expected an invalid-login error message."
        );
    }
}