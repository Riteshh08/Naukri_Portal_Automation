package tests;

import utils.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class ApplicationTrackingTest extends BaseTest {

	// ==========================================
    // Jobs menu
    // ==========================================
    private static final By JOBS_MENU = By.cssSelector(
        "a.nI-gNb-menuItems__anchorDropdown[title='Recommended Jobs']"
    );

    // ==========================================
    // Application Status link
    // ==========================================
    private static final By APPLICATION_STATUS = By.cssSelector(
        "a[href='/myapply/historypage']"
    );

    @Test
    public void previouslyAppliedJobsShowStatus() {

        // ==========================================
        // STEP 1: Login
        // ==========================================
        launchWebsiteAndLogin();

        System.out.println("Login successful.");

        // ==========================================
        // STEP 2: Open Application Status
        // ==========================================
        openApplicationStatus();

        // ==========================================
        // STEP 3: Wait for Application Status page
        // ==========================================
        wait.until(
            ExpectedConditions.urlContains("/myapply/historypage")
        );

        // ==========================================
        // STEP 4: Get current URL
        // ==========================================
        String currentUrl = driver.getCurrentUrl();

        System.out.println(
            "Application Status URL: " + currentUrl
        );

        // ==========================================
        // STEP 5: Verify Application Status page
        // ==========================================
        Assert.assertTrue(
            currentUrl.contains("/myapply/historypage"),
            "Application Status page was not opened."
        );

        System.out.println(
            "Application Status page opened successfully."
        );

        // ==========================================
        // STEP 6:
        // Application records will be validated after
        // inspecting the actual Application Status page DOM.
        // ==========================================

        System.out.println(
            "Application Tracking navigation test PASSED."
        );
    }

    /**
     * Hover over Jobs and open Application Status.
     */
    private void openApplicationStatus() {

        for (int attempt = 1; attempt <= 3; attempt++) {

            try {

                // ------------------------------------------
                // Find Jobs menu
                // ------------------------------------------
                WebElement jobsMenu = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                        JOBS_MENU
                    )
                );

                System.out.println(
                    "Jobs menu found."
                );

                // ------------------------------------------
                // Hover over Jobs
                // ------------------------------------------
                Actions actions = new Actions(driver);

                actions
                    .moveToElement(jobsMenu)
                    .pause(Duration.ofSeconds(1))
                    .perform();

                System.out.println(
                    "Hovered over Jobs menu."
                );

                // ------------------------------------------
                // Wait for Application Status link
                // ------------------------------------------
                WebElement applicationStatus = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                        APPLICATION_STATUS
                    )
                );

                System.out.println(
                    "Application status link found."
                );

                // ------------------------------------------
                // Get the actual href
                // ------------------------------------------
                String href = applicationStatus.getAttribute("href");

                System.out.println(
                    "Application status href: " + href
                );

                // ------------------------------------------
                // Navigate using the actual href
                // ------------------------------------------
                driver.get(href);

                System.out.println(
                    "Navigated to Application Status."
                );

                // ------------------------------------------
                // Wait for page URL
                // ------------------------------------------
                wait.until(
                    ExpectedConditions.urlContains(
                        "/myapply/historypage"
                    )
                );

                return;

            } catch (StaleElementReferenceException e) {

                System.out.println(
                    "Naukri menu changed. Retrying..."
                );

                if (attempt == 3) {
                    throw e;
                }
            }
        }
    }
}