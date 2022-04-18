
package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * @author 91809
 *
 */
public class Cookies extends BasePage {

	public Cookies(WebDriver driver) {
		super(driver);
		waitTillPageLoads();
	}

	By cookiesDialog = By.className("ot-sdk-container");

	By agreeButton = By.id("onetrust-accept-btn-handler");

	By onlyNecessaryButton = By.id("onetrust-reject-all-handler");

	By customButton = By.id("onetrust-pc-btn-handler");

	public Cart cookiesAcceptDialogue() {
		if (driver.findElement(cookiesDialog).isDisplayed()) {
			click(driver.findElement(agreeButton));
		}
		return new Cart(driver);
	}

}
