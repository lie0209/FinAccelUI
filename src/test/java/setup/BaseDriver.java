package setup;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pageObject.googleMapsPage;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.NoSuchElementException;

public class BaseDriver<driver> {

//    public AppiumDriver<MobileElement> driver;
    protected static AppiumDriver driver;
    protected static googleMapsPage googlemapspage = new googleMapsPage(driver);

    @BeforeTest
    public void setup(){

        try {
        DesiredCapabilities capability = new DesiredCapabilities();
        capability.setCapability(MobileCapabilityType.PLATFORM_NAME,"Android");
        capability.setCapability(MobileCapabilityType.DEVICE_NAME,"emulator-5554");
        capability.setCapability(MobileCapabilityType.PLATFORM_VERSION,"8.1");
        capability.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT,60);
        capability.setCapability(AndroidMobileCapabilityType.APP_PACKAGE,"com.google.android.apps.maps");
        capability.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY,"com.google.android.maps.MapsActivity");

        URL serverURL = new URL("http://localhost:4723/wd/hub");
        driver = new AndroidDriver<WebElement>(serverURL,capability);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRun(){
        System.out.println("Test to run emmulator");
    }

    @AfterSuite
    public void tearDown(){
    }

}
