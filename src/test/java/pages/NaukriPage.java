package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NaukriPage {

	private final WebDriver driver;
    private final WebDriverWait wait;

    private static final String BASE_URL =
            "https://www.naukri.com/nlogin/login?URL=https://www.naukri.com/mnjuser/homepage";

    private static final By EMAIL_INPUT =
            By.id("usernameField");

    private static final By PASSWORD_INPUT =
            By.id("passwordField");

    private static final By LOGIN_BUTTON =
            By.xpath("//*[@id=\"loginForm\"]/div[2]/div[3]/div/button[1]");

    public NaukriPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void launchWebsite() {
        driver.get(BASE_URL);
    }

    public void login(String username, String password) {

        WebElement emailField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(EMAIL_INPUT)
        );

        emailField.clear();
        emailField.sendKeys(username);

        WebElement passwordField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(PASSWORD_INPUT)
        );

        passwordField.clear();
        passwordField.sendKeys(password);

        wait.until(
                ExpectedConditions.elementToBeClickable(LOGIN_BUTTON)
        ).click();
    }
}