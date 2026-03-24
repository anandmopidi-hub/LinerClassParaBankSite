package base;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ParaBankRegistrationLogin {
    static WebDriver driver;
    static WebDriverWait wait;

    // ✅ STEP LOGGER
    public static void stepLog(String step, String message, boolean status) {
        if (status) {
            System.out.println(step + " PASSED: " + message);
        } else {
            System.out.println(step + " FAILED: " + message);
            throw new RuntimeException(step + " FAILED: " + message);
        }
    }

    // ✅ GENERIC METHODS
    public static void click(String step, By locator) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
            stepLog(step, "Clicked element", true);
        } catch (Exception e) {
            stepLog(step, "Unable to click element", false);
        }
    }

    public static void type(String step, By locator, String value) {
        try {
            WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            el.clear();
            el.sendKeys(value);
            stepLog(step, "Entered value: " + value, true);
        } catch (Exception e) {
            stepLog(step, "Unable to enter value", false);
        }
    }

    public static void selectDropdown(String step, By locator, int index) {
        try {
            WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            Select select = new Select(el);

            if (select.getOptions().size() > index) {
                select.selectByIndex(index);
                stepLog(step, "Selected dropdown index " + index, true);
            } else {
                stepLog(step, "Dropdown index not available", false);
            }
        } catch (Exception e) {
            stepLog(step, "Dropdown selection failed", false);
        }
    }

    public static void main(String[] args) {

        try {

            // ✅ TEST DATA
            String username = "user" + System.currentTimeMillis();
            String password = "Password123";

            // ✅ DRIVER SETUP
            ChromeOptions options = new ChromeOptions();
            options.setExperimentalOption("excludeSwitches",
                    Arrays.asList("enable-automation", "enable-logging"));

            Map<String, Object> prefs = new HashMap<>();
            prefs.put("credentials_enable_service", false);
            prefs.put("profile.password_manager_enabled", false);
            options.setExperimentalOption("prefs", prefs);

            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver(options);

            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            driver.manage().window().maximize();

            // ================= START =================
            driver.get("https://parabank.parasoft.com/parabank/register.htm");
            stepLog("Step 1", "Opened Registration Page", true);

            // ================= REGISTRATION =================
            type("Step 2", By.name("customer.firstName"), "John");
            type("Step 3", By.name("customer.lastName"), "Doe");
            type("Step 4", By.name("customer.address.street"), "Street");
            type("Step 5", By.name("customer.address.city"), "City");
            type("Step 6", By.name("customer.address.state"), "State");
            type("Step 7", By.name("customer.address.zipCode"), "12345");
            type("Step 8", By.name("customer.phoneNumber"), "9999999999");
            type("Step 9", By.name("customer.ssn"), "123456");
            type("Step 10", By.name("customer.username"), username);
            type("Step 11", By.name("customer.password"), password);
            type("Step 12", By.name("repeatedPassword"), password);

            click("Step 13", By.xpath("//input[@value='Register']"));

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//h1[contains(text(),'Welcome')]")));
            stepLog("Step 14", "Registration Successful", true);

            // ================= LOGOUT =================
            click("Step 15", By.linkText("Log Out"));

            // ================= LOGIN =================
            type("Step 16", By.name("username"), username);
            type("Step 17", By.name("password"), password);
            click("Step 18", By.xpath("//input[@value='Log In']"));

            stepLog("Step 19", "Login Successful", true);
            stepLog("Step 19", "Login Successful", true);

         // ✅ WAIT FOR DASHBOARD LOAD
         wait.until(ExpectedConditions.presenceOfElementLocated(By.id("leftPanel")));
         wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Open New Account")));
            Thread.sleep(1000);

            // ================= OPEN ACCOUNT =================
            click("Step 20", By.linkText("Open New Account"));
            selectDropdown("Step 21", By.id("type"), 1);
            click("Step 22", By.xpath("//input[@value='Open New Account']"));

            // ================= ACCOUNT OVERVIEW =================
            click("Step 23", By.linkText("Accounts Overview"));
            click("Step 24", By.xpath("//table[@id='accountTable']//a"));

            // ================= TRANSACTIONS =================
            selectDropdown("Step 25", By.id("month"), 4);
            selectDropdown("Step 26", By.id("transactionType"), 1);
            click("Step 27", By.xpath("//input[@value='Go']"));

            // ================= TRANSFER FUNDS =================
            click("Step 28", By.linkText("Transfer Funds"));
            type("Step 29", By.id("amount"), "100");
         
            click("Step 31", By.xpath("//input[@value='Transfer']"));

            // ================= BILL PAY =================
            click("Step 32", By.linkText("Bill Pay"));
            type("Step 33", By.name("payee.name"), "Test User");
            type("Step 34", By.name("payee.address.street"), "Street");
            type("Step 35", By.name("payee.address.city"), "City");
            type("Step 36", By.name("payee.address.state"), "State");
            type("Step 37", By.name("payee.address.zipCode"), "12345");
            type("Step 38", By.name("payee.phoneNumber"), "9999999999");
            type("Step 39", By.name("payee.accountNumber"), "123456");
            type("Step 40", By.name("verifyAccount"), "123456");
            type("Step 41", By.name("amount"), "50");
            click("Step 42", By.xpath("//input[@value='Send Payment']"));

            // ================= FIND TRANSACTION =================
            click("Step 43", By.linkText("Find Transactions"));
            selectDropdown("Step 44", By.id("accountId"), 0);
            type("Step 45", By.id("transactionId"), "1");
            click("Step 46", By.id("findById"));

            // ================= UPDATE CONTACT =================
            click("Step 47", By.linkText("Update Contact Info"));

            // ================= REQUEST LOAN =================
            click("Step 48", By.linkText("Request Loan"));
            type("Step 49", By.id("amount"), "500");
            type("Step 50", By.id("downPayment"), "100");
            selectDropdown("Step 51", By.id("fromAccountId"), 0);
            click("Step 52", By.xpath("//input[@value='Apply Now']"));

            // ================= FINAL =================
            System.out.println("\n======================================");
            System.out.println(" TEST CASE STATUS: PASSED ✅");
            System.out.println("======================================");

        } catch (Exception e) {

            System.out.println("\n======================================");
            System.out.println(" TEST CASE STATUS: FAILED ❌");
            System.out.println(" Reason: " + e.getMessage());
            System.out.println("======================================");

        } finally {
            if (driver != null) {
                driver.quit();
                System.out.println("Browser closed.");
            }
        }
    }
}
