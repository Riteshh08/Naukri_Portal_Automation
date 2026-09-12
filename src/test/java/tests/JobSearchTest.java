package tests;

import utils.BaseTest;
import utils.TestData;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Locale;

public class JobSearchTest extends BaseTest {

	// ---------------------------------------------------------
    // Naukri Search Locators
    // ---------------------------------------------------------

    private static final By SEARCH_EXPAND_BUTTON =
            By.cssSelector("button.nI-gNb-sb__expand");

    private static final By DESIGNATION_INPUT =
            By.cssSelector(
                    "input[placeholder='Enter keyword / designation / companies']"
            );

    private static final By EXPERIENCE_INPUT =
            By.id("experienceDD");

    private static final By LOCATION_INPUT =
            By.cssSelector(
                    "input[placeholder='Enter location']"
            );

    private static final By SEARCH_BUTTON =
            By.cssSelector(
                    "button.nI-gNb-sb__icon-wrapper[aria-label='Search']"
            );

    // ---------------------------------------------------------
    // Job Result Locator
    // ---------------------------------------------------------

    /*
     * Naukri job cards can change their HTML structure.
     * We use the visible job titles as the result indicator.
     */
    private static final By JOB_RESULT_TITLES =
            By.xpath(
                    "//*[self::a or self::span or self::div]"
                    + "[contains(translate(normalize-space(.),"
                    + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ',"
                    + "'abcdefghijklmnopqrstuvwxyz'),'qa automation')"
                    + " or contains(translate(normalize-space(.),"
                    + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ',"
                    + "'abcdefghijklmnopqrstuvwxyz'),'automation qa')]"
            );

    // ---------------------------------------------------------
    // Test
    // ---------------------------------------------------------

    @Test
    public void searchAndApplyLocationFilter() {

        // 1. Launch Naukri and login
        launchWebsiteAndLogin();

        // 2. Click "Search jobs here"
        clickWhenReady(SEARCH_EXPAND_BUTTON);

        // 3. Enter designation
        WebElement designationField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        DESIGNATION_INPUT
                )
        );

        designationField.clear();
        designationField.sendKeys(TestData.SEARCH_TERM);

        // 4. Enter location
        WebElement locationField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        LOCATION_INPUT
                )
        );

        locationField.clear();
        locationField.sendKeys(TestData.LOCATION);

        // 5. Click Search
        clickWhenReady(SEARCH_BUTTON);

        // -----------------------------------------------------
        // Wait for search results
        // -----------------------------------------------------

        wait.until(
                ExpectedConditions.urlContains(
                        "qa-automation-engineer-jobs-in-pune"
                )
        );

        // -----------------------------------------------------
        // Verify URL
        // -----------------------------------------------------

        String currentUrl = driver.getCurrentUrl();

        Assert.assertTrue(
                currentUrl.contains("qa-automation-engineer-jobs-in-pune"),
                "URL should contain the searched designation and location."
        );

        // -----------------------------------------------------
        // Verify page title
        // -----------------------------------------------------

        wait.until(
                ExpectedConditions.titleContains(
                        "Qa Automation Engineer Jobs In Pune"
                )
        );

        String pageTitle = driver.getTitle();

        Assert.assertTrue(
                pageTitle.toLowerCase().contains("qa automation engineer"),
                "Page title should contain the searched designation."
        );

        Assert.assertTrue(
                pageTitle.toLowerCase().contains("pune"),
                "Page title should contain the selected location."
        );

        // -----------------------------------------------------
        // Verify search keyword and location on page
        // -----------------------------------------------------

        wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath(
                                "//*[contains("
                                        + "translate(normalize-space(.),"
                                        + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ',"
                                        + "'abcdefghijklmnopqrstuvwxyz'),"
                                        + "'qa automation engineer'"
                                        + ")]"
                        )
                )
        );

        String pageText = driver
                .findElement(By.tagName("body"))
                .getText()
                .toLowerCase();

        Assert.assertTrue(
                pageText.contains("qa automation engineer"),
                "Search page should contain the searched designation."
        );

        Assert.assertTrue(
                pageText.contains("pune"),
                "Search page should contain the selected location."
        );

        // -----------------------------------------------------
        // Verify job results
        // -----------------------------------------------------

        wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(
                        JOB_RESULT_TITLES
                )
        );

        int resultCount =
                driver.findElements(JOB_RESULT_TITLES).size();

        Assert.assertTrue(
                resultCount > 0,
                "Expected at least one QA/Automation job result."
        );

        System.out.println(
                "Number of QA/Automation result elements found: "
                        + resultCount
        );

        System.out.println(
                "Search URL: "
                        + currentUrl
        );

        System.out.println(
                "Search Title: "
                        + pageTitle
        );
    }

    // ---------------------------------------------------------
    // Helper method
    // Handles Naukri's dynamic DOM / stale elements
    // ---------------------------------------------------------

    private void clickWhenReady(By locator) {

        for (int attempt = 1; attempt <= 3; attempt++) {

            try {

                WebElement element = wait.until(
                        ExpectedConditions.elementToBeClickable(locator)
                );

                element.click();

                return;

            } catch (StaleElementReferenceException e) {

                if (attempt == 3) {
                    throw e;
                }
            }
        }
    }
}