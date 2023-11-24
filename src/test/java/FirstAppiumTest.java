import com.beust.ah.A;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.AndroidTouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class FirstAppiumTest {

    AppiumDriver driver;
    public AndroidTouchAction action;

    @BeforeTest
    public void setUp() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();// used to link with the appium client and appium server
        // contains a set of caps that enable us to start the session with appium server
        caps.setCapability("platformName", "Android");
        caps.setCapability("automationName", "UiAutomator2");
        caps.setCapability("platformVersion", "11.0");
        caps.setCapability("deviceName", "Android Emulator");
        //caps.setCapability("app", System.getProperty("user.dir") + "/App/ApiDemos-debug.apk");
        //for web-based app
        caps.setCapability("browserName","Chrome");
        // appium will start the session by calling the desired cap then connect to the server
        driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub/"), caps);
    }

    private void scrollDown() {
        Dimension dimension = driver.manage().window().getSize();
        System.out.println(dimension);
        int scrollStart = (int) (dimension.getHeight() * 0.8);
        int scrollEnd = (int) (dimension.getHeight() * 0.1);

        action = new AndroidTouchAction(driver)
                .press(PointOption.point(0, scrollStart))
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(3)))
                .moveTo(PointOption.point(0, scrollEnd))
                .release()
                .perform();
    }

    private void dragAndDrop() {
        AndroidElement dragAndDrop = (AndroidElement) driver.findElementByAccessibilityId("Drag and Drop");
        action = new AndroidTouchAction(driver);
        action.tap(ElementOption.element(dragAndDrop)).perform();

        AndroidElement drag = (AndroidElement) driver.findElement(By.id("drag_dot_1"));
        AndroidElement drop = (AndroidElement) driver.findElement(By.id("drag_dot_2"));

        action.longPress(ElementOption.element(drag))
                .waitAction().moveTo(ElementOption.element(drop))
                .release()
                .perform();
    }

    private void clickMethod(String identifier) {
        AndroidElement element = (AndroidElement) driver.findElementByAccessibilityId(identifier);
        action = new AndroidTouchAction(driver);
        action.tap(ElementOption.element(element)).perform();
    }

    @Test
    public void testScrollDown() {
        clickMethod("Views");
        scrollDown();
        AndroidElement Lists = (AndroidElement) driver.findElementByAccessibilityId("Lists");
        action.tap(ElementOption.element(Lists)).perform();
    }

    @Test
    public void testDragAndDrop() {
        clickMethod("Views");
        dragAndDrop();
    }

    @Test
    public void swipe() {
        clickMethod("Views");
        clickMethod("Gallery");
        clickMethod("1. Photos");

        ArrayList<AndroidElement> pics;
        pics=(ArrayList<AndroidElement>) driver.findElementsByClassName("android.widget.ImageView");
        System.out.println(pics.size());
        // AndroidElement lastPic=(AndroidElement) driver.findElementsByClassName("android.widget.ImageView").get(pics.size);


        AndroidElement pic1=(AndroidElement) driver.findElementsByClassName("android.widget.ImageView").get(0);
        action=new AndroidTouchAction(driver)
                .press(ElementOption.element(pic1))
                .waitAction()
                .moveTo(PointOption.point(-100,200))
                .release().perform();
    }
    @Test
    public void UserLogin() {
        driver.get("https://the-internet.herokuapp.com/login");
        driver.manage().timeouts().implicitlyWait(3000, TimeUnit.MILLISECONDS);
        driver.get("https://the-internet.herokuapp.com/login");
        driver.manage().timeouts().implicitlyWait(3000, TimeUnit.MILLISECONDS);
        WebElement username = driver.findElementByCssSelector("input#username");
        username.sendKeys("tomsmith");
        WebElement password = driver.findElementByCssSelector("input#password");
        password.sendKeys("SuperSecretPassword!");
        WebElement loginBtn = driver.findElementByCssSelector("button.radius");
        loginBtn.click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.urlContains("secure"));
        System.out.println(driver.getCurrentUrl());
    }
    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
