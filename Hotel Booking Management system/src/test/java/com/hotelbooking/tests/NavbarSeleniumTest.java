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

public class NavbarSeleniumTest {

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
    public void testNavbarLinks() {
        // Open the React application URL (make sure the React app is running)
        driver.get("http://localhost:3000"); 

        // Use WebDriverWait to wait for elements to be visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Test for "Search Hotels" link
        WebElement searchLink = wait.until(ExpectedConditions.elementToBeClickable(By.id("search-link")));
        searchLink.click();  // Simulate a click on the Search Hotels link
        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/search"));  // Validate the URL

        // Test for "Home" link
        WebElement homeLink = wait.until(ExpectedConditions.elementToBeClickable(By.id("home-link")));
        homeLink.click();  // Simulate a click on the Home link
        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/"));  // Validate the URL

        // Test for "Booking History" link
        WebElement bookingLink = wait.until(ExpectedConditions.elementToBeClickable(By.id("booking-link")));
        bookingLink.click();  // Simulate a click on the Booking History link
        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/History"));  // Validate the URL
    }

    @AfterClass
    public void tearDown() {
        // Close the driver after tests are finished
        if (driver != null) {
            driver.quit();
        }
    }
}
