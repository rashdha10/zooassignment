package stepDefinition;

import java.util.Properties;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utils.ConfigReader;
import webdriver.DriverFactory;

/**
 * @author 91809
 *
 */
public class ApplicationHooks {

	DriverFactory driverFactory;
	WebDriver driver;
	Properties prop;

	@Before()
	public void before() {
		prop = ConfigReader.loadProperties();
		driverFactory = new DriverFactory();
		driver = DriverFactory.init_driver(prop.getProperty("browser"));
	}

	@After()
	public void tearDown(Scenario scenario) {
		if (scenario.isFailed()) {
			try {
				String screenshotName = scenario.getName().replaceAll(" ", "_");
				byte[] sourcePath = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
				scenario.attach(sourcePath, "image/png", screenshotName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		driver.quit();
	}
}
