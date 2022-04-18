
package webdriver;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * @author 91809
 *
 */
public class DriverFactory {

	public static ThreadLocal<WebDriver> threadDriver = new ThreadLocal<WebDriver>();

	/**
	 * Method to initialise the driver based on the browser of one's choice
	 * 
	 * @param browser
	 * @return
	 */
	public static WebDriver init_driver(String browser) {

		System.out.println("browser value is: " + browser);

		if (browser.equals("chrome")) {
			WebDriverManager.chromedriver().setup();
			threadDriver.set(new ChromeDriver());
		}
		/**
		 * Mention any driver if wants to use other than chrome browser in the else part
		 */
		else {
			System.out.println("Unsupported browser " + browser);
		}

		getWebDriver().manage().deleteAllCookies();
		getWebDriver().manage().window().maximize();
		getWebDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		return getWebDriver();

	}

	/**
	 * Method to get thread local web driver
	 * 
	 * @return
	 */
	public static synchronized WebDriver getWebDriver() {
		return threadDriver.get();
	}
}
