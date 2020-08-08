package pageObject;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidBy;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import setup.BaseDriver;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class googleMapsPage extends BaseDriver {

    public googleMapsPage(AppiumDriver<WebElement> driver){
        PageFactory.initElements(driver, this);
    }

    /*PAGE OBJECT*/

    private String omniSearchBoxTf = "com.google.android.apps.maps:id/search_omnibox_text_box";
    private String searchKeyTf = "com.google.android.apps.maps:id/search_omnibox_edit_text";
    private String searchResultCard = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.FrameLayout[1]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.ScrollView/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.RelativeLayout";
    private String destinationNameTf ="/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.FrameLayout[1]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.ScrollView/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.RelativeLayout[%d]/android.widget.LinearLayout/android.widget.TextView[1]";
    private String destinationAddressTf = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.FrameLayout[1]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.ScrollView/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.RelativeLayout[%d]/android.widget.LinearLayout/android.widget.TextView[2]";
    private String destinationDistanceTf = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.FrameLayout[1]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.ScrollView/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.RelativeLayout[%d]/android.widget.TextView";

    /*ACTION*/

    public void inputDestination(String destination) throws InterruptedException {
        Thread.sleep(2000);
        driver.findElement(By.id(omniSearchBoxTf)).click();
        Thread.sleep(2000);
        driver.findElement(By.id(searchKeyTf)).clear();
        driver.findElement(By.id(searchKeyTf)).sendKeys(destination);
        System.out.println("Input search key '" + destination +"'");
    }

    public void selectResult(int result) throws InterruptedException {
        Thread.sleep(2000);
        List <WebElement> resultCard = driver.findElements(By.xpath(searchResultCard));
        System.out.println("Select result number " + result);
        Thread.sleep(2000);
        System.out.println("Result name " + driver.findElement(By.xpath(String.format(destinationNameTf,result))).getText());
        System.out.println("Result address " + driver.findElement(By.xpath(String.format(destinationAddressTf,result))).getText());
        System.out.println("Result distance " + driver.findElement(By.xpath(String.format(destinationDistanceTf,result))).getText());
        resultCard.get(result).click();
    }


    /*Zoom method*/

    private Collection<Sequence> zoom(Point locus, int startRadius, int endRadius, int pinchAngle, Duration duration) {
        // convert degree angle into radians. 0/360 is top (12 O'clock).
        double angle = Math.PI / 2 - (2 * Math.PI / 360 * pinchAngle);

        // create the gesture for one finger
        Sequence fingerAPath = zoomSinglefinger("fingerA", locus, startRadius, endRadius, angle, duration);

        // flip the angle around to the other side of the locus and get the gesture for the second finger
        angle = angle + Math.PI;
        Sequence fingerBPath = zoomSinglefinger("fingerB", locus, startRadius, endRadius, angle, duration);

        return Arrays.asList(fingerAPath, fingerBPath);
    }

    private Sequence zoomSinglefinger(String fingerName, Point locus, int startRadius, int endRadius, double angle, Duration duration) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, fingerName);
        Sequence fingerPath = new Sequence(finger, 0);

        double midpointRadius = startRadius + (endRadius > startRadius ? 1 : -1) * 20;

        // find coordinates for starting point of action (converting from polar coordinates to cartesian)
        int fingerStartx = (int)Math.floor(locus.x + startRadius * Math.cos(angle));
        int fingerStarty = (int)Math.floor(locus.y - startRadius * Math.sin(angle));

        // find coordinates for first point that pingers move quickly to
        int fingerMidx = (int)Math.floor(locus.x + (midpointRadius * Math.cos(angle)));
        int fingerMidy = (int)Math.floor(locus.y - (midpointRadius * Math.sin(angle)));

        // find coordinates for ending point of action (converting from polar coordinates to cartesian)
        int fingerEndx = (int)Math.floor(locus.x + endRadius * Math.cos(angle));
        int fingerEndy = (int)Math.floor(locus.y - endRadius * Math.sin(angle));

        // move finger into start position
        fingerPath.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), fingerStartx, fingerStarty));
        // finger comes down into contact with screen
        fingerPath.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        // finger moves a small amount very quickly
        fingerPath.addAction(finger.createPointerMove(Duration.ofMillis(1), PointerInput.Origin.viewport(), fingerMidx, fingerMidy));
        // pause for a little bit
        fingerPath.addAction(new Pause(finger, Duration.ofMillis(100)));
        // finger moves to end position
        fingerPath.addAction(finger.createPointerMove(duration, PointerInput.Origin.viewport(), fingerEndx, fingerEndy));
        // finger lets up, off the screen
        fingerPath.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        return fingerPath;
    }

    private Collection<Sequence> zoomIn(Point locus, int distance) {
        return zoom(locus, 100, 200 + distance, 45, Duration.ofMillis(25));
    }

    private Collection<Sequence> zoomOut(Point locus, int distance) {
        return zoom(locus, 200 + distance, 200, 45, Duration.ofMillis(25));
    }

    public void ZoomOutAndZoomIn() throws InterruptedException {
        Thread.sleep(2000);
        // tap center to dismiss toolbars
        WebElement map = driver.findElementById("com.google.android.apps.maps:id/mainmap_container");
        map.click();
        Rectangle mapCoordinates = map.getRect();
        Point center = getCenter(mapCoordinates);
        System.out.println("Pin central coordinates");

        driver.perform(zoomOut(center, 150));
        Thread.sleep(2000);
        System.out.println("Zoom Out");

        driver.perform(zoomIn(center, 350));
        driver.perform(zoomIn(center, 350));
        driver.perform(zoomIn(center, 350));
        Thread.sleep(2000);
        System.out.println("Zoom In");
    }

    private Point getCenter(Rectangle rect) {
        return new Point(rect.x + rect.getWidth() / 2, rect.y + rect.getHeight() / 2);
    }

}
