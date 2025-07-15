package com.hotelbooking.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

public class homepagetest {

    private WebDriver driver;

    @BeforeClass
    public void setUp() {
        // Setup WebDriverManager and ChromeDriver
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Optional: Run in headless mode
        driver = new ChromeDriver(options);

        // Add an implicit wait to the WebDriver
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }

    @Test
    public void testhomepage() {
        // Open the React application URL (make sure the React app is running)
        driver.get("http://localhost:3000"); 

        // Use WebDriverWait to wait for elements to be visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Test for the "Book Now" button
        WebElement bookNowButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("booknow-button")));
        bookNowButton.click();  // Simulate a click on the "Book Now" button

        // Validate that the URL changes to the correct page ("/search")
        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/search"));
    }

    @AfterClass
    public void tearDown() {
        // Close the driver after tests are finished
        if (driver != null) {
            driver.quit();
        }
    }
}
