package testCases;

import org.testng.annotations.Test;
import setup.BaseDriver;

public class googleMapsAppTest extends BaseDriver {

    @Test
    public void testSearchLocation() throws InterruptedException {
        googlemapspage.inputDestination("Jakarta");
        googlemapspage.selectResult(2);
        googlemapspage.ZoomOutAndZoomIn();
    }

}
