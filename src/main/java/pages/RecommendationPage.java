package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author 91809
 *
 */
public class RecommendationPage extends BasePage {

	By nextButton = By.className("lSNext");

	By previousButton = By.className("lSPrev");

	By recommendationSection = By.xpath("//div[contains(@id,'js-zootopiaRecos')]//ul//li//button");

	public RecommendationPage(WebDriver driver) {
		super(driver);
		waitForElement(true, driver.findElement(recommendationSection));
	}

	public void addAnyItemsToCart(int count) {
		waitTillPageLoads();
		List<WebElement> recommendedItems;
		recommendedItems = driver.findElements(recommendationSection);
		for (int i = 0; i < count; i++) {
			try {
				click(recommendedItems.get(i));
				waitForElement(true, driver.findElement(recommendationSection));
			} catch (Exception e) {
				click(recommendedItems.get(i));
				waitForElement(true, driver.findElement(recommendationSection));
			}
		}
	}
}
