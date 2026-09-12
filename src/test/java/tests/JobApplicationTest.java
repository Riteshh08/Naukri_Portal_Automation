package tests;

import utils.BaseTest;
import utils.TestData;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class JobApplicationTest extends BaseTest {

    // =====================================================
    // MAXIMUM APPLICATIONS PER RUN
    // =====================================================

    private static final int MAX_APPLICATIONS = 5;


    // =====================================================
    // SEARCH LOCATORS
    // =====================================================

    private static final By SEARCH_EXPAND_BUTTON =
        By.cssSelector(
            "button.nI-gNb-sb__expand"
        );

    private static final By DESIGNATION_INPUT =
        By.cssSelector(
            "input[placeholder='Enter keyword / designation / companies']"
        );

    private static final By LOCATION_INPUT =
        By.cssSelector(
            "input[placeholder='Enter location']"
        );

    private static final By SEARCH_BUTTON =
        By.cssSelector(
            "button.nI-gNb-sb__icon-wrapper[aria-label='Search']"
        );


    // =====================================================
    // JOB LOCATOR
    // =====================================================

    private static final By QA_AUTOMATION_JOBS =
        By.xpath(
            "//div[contains(@class,'srp-jobtuple-wrapper')]"
          + "//h2/a[contains(@class,'title')"
          + " and contains("
          + "translate(normalize-space(.),"
          + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ',"
          + "'abcdefghijklmnopqrstuvwxyz'),"
          + "'qa automation engineer')]"
        );


    // =====================================================
    // APPLY BUTTONS
    // =====================================================

    private static final By APPLY_BUTTON =
        By.id("apply-button");

    private static final By COMPANY_SITE_BUTTON =
        By.id("company-site-button");


    // =====================================================
    // CHATBOT
    // =====================================================

    private static final By CHATBOT_DRAWER =
        By.cssSelector(
            "div.chatbot_DrawerContentWrapper"
        );

    private static final By CHATBOT_MESSAGES =
        By.cssSelector(
            "div.chatbot_MessageContainer"
        );

    private static final By CHATBOT_QUESTIONS =
        By.cssSelector(
            "div.chatbot_MessageContainer "
          + "li.botItem .botMsg"
        );

    private static final By CHATBOT_TEXT_AREA =
        By.cssSelector(
            "div.textArea[contenteditable='true']"
        );

    private static final By SAVE_BUTTON =
        By.xpath(
            "//div[contains(@class,'sendMsg')"
          + " and normalize-space()='Save']"
        );


    // =====================================================
    // MAIN TEST
    // =====================================================

    @Test
    public void applyForFiveJobs() {

        int successfulApplications = 0;

        int checkedJobs = 0;


        // =================================================
        // LOGIN
        // =================================================

        launchWebsiteAndLogin();

        System.out.println(
            "Login successful."
        );


        // =================================================
        // OPEN SEARCH
        // =================================================

        openJobSearch();

        System.out.println(
            "Job search opened."
        );


        // =================================================
        // ENTER JOB
        // =================================================

        WebElement designation = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                DESIGNATION_INPUT
            )
        );

        designation.clear();

        designation.sendKeys(
            TestData.SEARCH_TERM
        );

        System.out.println(
            "Entered job: "
            + TestData.SEARCH_TERM
        );


        // =================================================
        // ENTER LOCATION
        // =================================================

        WebElement location = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                LOCATION_INPUT
            )
        );

        location.clear();

        location.sendKeys(
            TestData.LOCATION
        );

        System.out.println(
            "Entered location: "
            + TestData.LOCATION
        );


        // =================================================
        // SEARCH
        // =================================================

        clickWhenReady(
            SEARCH_BUTTON
        );

        System.out.println(
            "Search button clicked."
        );


        // =================================================
        // WAIT FOR RESULTS
        // =================================================

        wait.until(
            ExpectedConditions.urlContains(
                "qa-automation-engineer-jobs"
            )
        );

        System.out.println(
            "Search results URL: "
            + driver.getCurrentUrl()
        );


        // =================================================
        // FIND JOBS
        // =================================================

        List<WebElement> jobs = wait.until(
            ExpectedConditions.visibilityOfAllElementsLocatedBy(
                QA_AUTOMATION_JOBS
            )
        );

        System.out.println(
            "Total matching jobs found: "
            + jobs.size()
        );


        Assert.assertTrue(
            jobs.size() > 0,
            "No matching jobs found."
        );


        // =================================================
        // SAVE SEARCH RESULTS TAB
        // =================================================

        String searchResultsWindow =
            driver.getWindowHandle();


        // =================================================
        // PROCESS JOBS
        // =================================================

        for (
            int i = 0;
            i < jobs.size() && successfulApplications < MAX_APPLICATIONS;
            i++
        ) {

            checkedJobs++;


            System.out.println();
            System.out.println(
                "========================================"
            );

            System.out.println(
                "Checking job "
                + (i + 1)
                + " | Applications: "
                + successfulApplications
                + "/"
                + MAX_APPLICATIONS
            );

            System.out.println(
                "========================================"
            );


            // -------------------------------------------------
            // RE-FIND JOBS
            // -------------------------------------------------

            List<WebElement> currentJobs = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(
                    QA_AUTOMATION_JOBS
                )
            );


            if (i >= currentJobs.size()) {

                System.out.println(
                    "Job list changed."
                );

                break;
            }


            WebElement job =
                currentJobs.get(i);


            String jobTitle =
                job.getText().trim();

            String jobUrl =
                job.getAttribute("href");


            System.out.println(
                "Job: "
                + jobTitle
            );

            System.out.println(
                "URL: "
                + jobUrl
            );


            if (
                jobUrl == null
                || jobUrl.isBlank()
            ) {

                System.out.println(
                    "Invalid job URL. Skipping."
                );

                continue;
            }


            // =================================================
            // OPEN JOB IN NEW TAB
            // =================================================

            driver.switchTo().newWindow(
                WindowType.TAB
            );

            driver.get(
                jobUrl
            );


            // =================================================
            // WAIT FOR JOB PAGE
            // =================================================

            wait.until(
                ExpectedConditions.urlContains(
                    "job-listings"
                )
            );


            System.out.println(
                "Job details page opened."
            );


            waitForSeconds(2);


            // =================================================
            // SCROLL
            // =================================================

            JavascriptExecutor js =
                (JavascriptExecutor) driver;

            js.executeScript(
                "window.scrollTo(0, document.body.scrollHeight);"
            );

            waitForSeconds(2);


            // =================================================
            // CHECK APPLY BUTTONS
            // =================================================

            boolean applyExists =
                driver.findElements(
                    APPLY_BUTTON
                ).size() > 0;


            boolean companySiteExists =
                driver.findElements(
                    COMPANY_SITE_BUTTON
                ).size() > 0;


            System.out.println(
                "Apply button present: "
                + applyExists
            );

            System.out.println(
                "Company site button present: "
                + companySiteExists
            );


            // =================================================
            // COMPANY SITE
            // =================================================

            if (
                !applyExists
                && companySiteExists
            ) {

                System.out.println(
                    "Apply on company site."
                );

                closeJobTab(
                    searchResultsWindow
                );

                continue;
            }


            // =================================================
            // NO APPLY
            // =================================================

            if (!applyExists) {

                System.out.println(
                    "No Naukri Apply button."
                );

                closeJobTab(
                    searchResultsWindow
                );

                continue;
            }


            // =================================================
            // NAUKRI APPLY
            // =================================================

            System.out.println(
                "Naukri Apply button found."
            );


            WebElement applyButton =
                wait.until(
                    ExpectedConditions.elementToBeClickable(
                        APPLY_BUTTON
                    )
                );


            js.executeScript(
                "arguments[0].scrollIntoView({block:'center'});",
                applyButton
            );


            waitForSeconds(1);


            js.executeScript(
                "arguments[0].click();",
                applyButton
            );


            System.out.println(
                "Apply button clicked."
            );


            // =================================================
            // CHATBOT
            // =================================================

            boolean chatbotOpened =
                waitForChatbot();


            if (!chatbotOpened) {

                System.out.println(
                    "Chatbot did not open."
                );

                closeJobTab(
                    searchResultsWindow
                );

                continue;
            }


            System.out.println(
                "Application chatbot opened."
            );


            // =================================================
            // ANSWER QUESTIONS
            // =================================================

            boolean completed =
                answerAllKnownQuestions();


            if (completed) {

                successfulApplications++;


                System.out.println();
                System.out.println(
                    "****************************************"
                );

                System.out.println(
                    "APPLICATION COUNT: "
                    + successfulApplications
                    + "/"
                    + MAX_APPLICATIONS
                );

                System.out.println(
                    "Job application flow completed."
                );

                System.out.println(
                    "****************************************"
                );


                // Close job tab after application
                closeJobTab(
                    searchResultsWindow
                );

            } else {

                System.out.println(
                    "Application could not be completed "
                  + "because an unknown question was found."
                );


                closeJobTab(
                    searchResultsWindow
                );
            }
        }


        // =================================================
        // FINAL RESULT
        // =================================================

        System.out.println();
        System.out.println(
            "========================================"
        );

        System.out.println(
            "FINAL TEST SUMMARY"
        );

        System.out.println(
            "========================================"
        );

        System.out.println(
            "Jobs checked: "
            + checkedJobs
        );

        System.out.println(
            "Successful applications: "
            + successfulApplications
            + "/"
            + MAX_APPLICATIONS
        );


        if (
            successfulApplications == MAX_APPLICATIONS
        ) {

            System.out.println(
                "SUCCESS: 5 applications completed."
            );

        } else {

            System.out.println(
                "Only "
                + successfulApplications
                + " applications were completed."
            );

            System.out.println(
                "This can happen if fewer than 5 eligible "
              + "Naukri-Apply jobs were found or an unknown "
              + "question was encountered."
            );
        }
    }


    // =====================================================
    // ANSWER ALL KNOWN QUESTIONS
    // =====================================================

    private boolean answerAllKnownQuestions() {

        int safetyCounter = 0;

        String previousQuestion = "";


        while (safetyCounter < 10) {

            safetyCounter++;


            // ---------------------------------------------
            // Find latest recruiter question
            // ---------------------------------------------

            String question =
                getLatestQuestion();


            if (
                question == null
                || question.isBlank()
            ) {

                System.out.println(
                    "No recruiter question found."
                );

                return true;
            }


            question =
                question.trim();


            System.out.println();
            System.out.println(
                "Recruiter question:"
            );

            System.out.println(
                question
            );


            // ---------------------------------------------
            // Avoid processing same question repeatedly
            // ---------------------------------------------

            if (
                question.equalsIgnoreCase(
                    previousQuestion
                )
            ) {

                waitForSeconds(1);

                continue;
            }


            previousQuestion =
                question;


            // ---------------------------------------------
            // Find answer
            // ---------------------------------------------

            String answer =
                getAnswerForQuestion(
                    question
                );


            // ---------------------------------------------
            // Unknown question
            // ---------------------------------------------

            if (
                answer == null
                || answer.isBlank()
            ) {

                System.out.println();
                System.out.println(
                    "UNKNOWN RECRUITER QUESTION"
                );

                System.out.println(
                    "Question: "
                    + question
                );

                System.out.println(
                    "Application will NOT guess the answer."
                );

                return false;
            }


            // ---------------------------------------------
            // Enter answer
            // ---------------------------------------------

            WebElement input =
                wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                        CHATBOT_TEXT_AREA
                    )
                );


            input.click();

            input.clear();

            input.sendKeys(
                answer
            );


            System.out.println(
                "Answer entered: "
                + answer
            );


            // ---------------------------------------------
            // Save
            // ---------------------------------------------

            WebElement save =
                wait.until(
                    ExpectedConditions.elementToBeClickable(
                        SAVE_BUTTON
                    )
                );


            save.click();


            System.out.println(
                "Answer saved."
            );


            // ---------------------------------------------
            // Wait for next question
            // ---------------------------------------------

            waitForSeconds(2);


            // ---------------------------------------------
            // Check if application is complete
            // ---------------------------------------------

            if (isApplicationCompleted()) {

                System.out.println(
                    "Application completion detected."
                );

                return true;
            }
        }


        System.out.println(
            "Question loop safety limit reached."
        );

        return false;
    }


    // =====================================================
    // GET LATEST QUESTION
    // =====================================================

    private String getLatestQuestion() {

        List<WebElement> questions =
            driver.findElements(
                CHATBOT_QUESTIONS
            );


        String latestQuestion = null;


        for (
            WebElement question :
            questions
        ) {

            String text =
                question.getText().trim();


            if (
                !text.isEmpty()
                && !text.toLowerCase().contains(
                    "thank you for showing interest"
                )
            ) {

                latestQuestion =
                    text;
            }
        }


        return latestQuestion;
    }


    // =====================================================
    // GET ANSWER FOR QUESTION
    // =====================================================

    private String getAnswerForQuestion(
        String question
    ) {

        String q =
            question.toLowerCase();


        // ---------------------------------------------
        // APPIUM
        // ---------------------------------------------

        if (
            q.contains("appium")
            && (
                q.contains("year")
                || q.contains("experience")
            )
        ) {

            return TestData.APPIUM_EXPERIENCE;
        }


        // ---------------------------------------------
        // SELENIUM
        // ---------------------------------------------

        if (
            q.contains("selenium")
            && (
                q.contains("year")
                || q.contains("experience")
            )
        ) {

            return TestData.SELENIUM_EXPERIENCE;
        }


        // ---------------------------------------------
        // JAVA
        // ---------------------------------------------

        if (
            q.contains("java")
            && (
                q.contains("year")
                || q.contains("experience")
            )
        ) {

            return TestData.JAVA_EXPERIENCE;
        }


        // ---------------------------------------------
        // PYTHON
        // ---------------------------------------------

        if (
            q.contains("python")
            && (
                q.contains("year")
                || q.contains("experience")
            )
        ) {

            return TestData.PYTHON_EXPERIENCE;
        }


        // ---------------------------------------------
        // API TESTING
        // ---------------------------------------------

        if (
            (
                q.contains("api testing")
                || q.contains("api")
            )
            && (
                q.contains("year")
                || q.contains("experience")
            )
        ) {

            return TestData.API_TESTING_EXPERIENCE;
        }


        // ---------------------------------------------
        // NOTICE PERIOD
        // ---------------------------------------------

        if (
            q.contains("notice period")
            || q.contains("notice")
        ) {

            return TestData.NOTICE_PERIOD;
        }


        // ---------------------------------------------
        // CURRENT CTC
        // ---------------------------------------------

        if (
            q.contains("current ctc")
            || q.contains("current salary")
            || q.contains("current compensation")
        ) {

            return TestData.CURRENT_CTC;
        }


        // ---------------------------------------------
        // EXPECTED CTC
        // ---------------------------------------------

        if (
            q.contains("expected ctc")
            || q.contains("expected salary")
            || q.contains("salary expectation")
            || q.contains("expected compensation")
        ) {

            return TestData.EXPECTED_CTC;
        }


        // ---------------------------------------------
        // CURRENT LOCATION
        // ---------------------------------------------

        if (
            q.contains("current location")
            || q.contains("where are you located")
            || q.contains("location are you")
        ) {

            return TestData.CURRENT_LOCATION;
        }


        // ---------------------------------------------
        // RELOCATION
        // ---------------------------------------------

        if (
            q.contains("relocate")
            || q.contains("relocation")
        ) {

            return TestData.WILLING_TO_RELOCATE;
        }


        // ---------------------------------------------
        // NIGHT SHIFT
        // ---------------------------------------------

        if (
            q.contains("night shift")
            || q.contains("night shifts")
            || q.contains("shift")
        ) {

            return TestData.NIGHT_SHIFT;
        }


        // ---------------------------------------------
        // EDUCATION
        // ---------------------------------------------

        if (
            q.contains("highest qualification")
            || q.contains("highest education")
            || q.contains("educational qualification")
        ) {

            return TestData.HIGHEST_QUALIFICATION;
        }


        // ---------------------------------------------
        // UNKNOWN
        // ---------------------------------------------

        return null;
    }


    // =====================================================
    // CHECK APPLICATION COMPLETION
    // =====================================================

    private boolean isApplicationCompleted() {

        String pageText =
            driver.findElement(
                By.tagName("body")
            )
            .getText()
            .toLowerCase();


        // Possible completion messages
        // We intentionally keep this conservative.

        if (
            pageText.contains(
                "application submitted"
            )
        ) {

            return true;
        }


        if (
            pageText.contains(
                "successfully applied"
            )
        ) {

            return true;
        }


        if (
            pageText.contains(
                "application completed"
            )
        ) {

            return true;
        }


        // If chatbot disappeared, the application
        // may have completed.

        List<WebElement> chatbot =
            driver.findElements(
                CHATBOT_DRAWER
            );


        if (
            chatbot.isEmpty()
        ) {

            return true;
        }


        return false;
    }


    // =====================================================
    // WAIT FOR CHATBOT
    // =====================================================

    private boolean waitForChatbot() {

        try {

            wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    CHATBOT_DRAWER
                )
            );


            wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    CHATBOT_MESSAGES
                )
            );


            wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    CHATBOT_TEXT_AREA
                )
            );


            return true;

        } catch (Exception e) {

            System.out.println(
                "Chatbot was not detected."
            );

            return false;
        }
    }


    // =====================================================
    // CLOSE JOB TAB
    // =====================================================

    private void closeJobTab(
        String searchResultsWindow
    ) {

        try {

            driver.close();

        } catch (Exception e) {

            System.out.println(
                "Could not close job tab: "
                + e.getMessage()
            );
        }


        driver.switchTo().window(
            searchResultsWindow
        );


        System.out.println(
            "Returned to search results."
        );
    }


    // =====================================================
    // OPEN SEARCH
    // =====================================================

    private void openJobSearch() {

        for (
            int attempt = 1;
            attempt <= 3;
            attempt++
        ) {

            try {

                WebElement searchExpandButton =
                    wait.until(
                        ExpectedConditions.elementToBeClickable(
                            SEARCH_EXPAND_BUTTON
                        )
                    );


                searchExpandButton.click();


                wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                        DESIGNATION_INPUT
                    )
                );


                return;

            } catch (
                StaleElementReferenceException e
            ) {

                System.out.println(
                    "Search element became stale. "
                  + "Retrying "
                  + attempt
                  + "..."
                );


                if (
                    attempt == 3
                ) {

                    throw e;
                }
            }
        }
    }


    // =====================================================
    // CLICK WITH RETRY
    // =====================================================

    private void clickWhenReady(
        By locator
    ) {

        for (
            int attempt = 1;
            attempt <= 3;
            attempt++
        ) {

            try {

                WebElement element =
                    wait.until(
                        ExpectedConditions.elementToBeClickable(
                            locator
                        )
                    );


                element.click();


                return;

            } catch (
                StaleElementReferenceException e
            ) {

                System.out.println(
                    "Element became stale. "
                  + "Retrying "
                  + attempt
                  + "..."
                );


                if (
                    attempt == 3
                ) {

                    throw e;
                }
            }
        }
    }


    // =====================================================
    // SIMPLE WAIT
    // =====================================================

    private void waitForSeconds(
        int seconds
    ) {

        try {

            Thread.sleep(
                seconds * 1000L
            );

        } catch (
            InterruptedException e
        ) {

            Thread.currentThread().interrupt();

            throw new RuntimeException(
                "Thread interrupted.",
                e
            );
        }
    }
}