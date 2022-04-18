
package pages;

import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import utils.ConfigReader;

/**
 * @author 91809
 *
 */
public class Cart extends BasePage {
	Cookies cookie;

	public Cart(WebDriver driver) {
		super(driver);
		driver.get(ConfigReader.getBaseUrl());
	}

	By cartSection = By.xpath("//div[contains(@class,'cart__table__row two__column')]");

	By subtotal = By.xpath("//span[@data-zta='overviewSubTotalValue']");

	By total = By.xpath("//span[@data-zta='total__price__value']");

	By shippingCountryName = By.xpath("//*[@class='js-shipping-cost shipping__country__label']//*[name()='svg']");

	By postcode = By.id("zipCode");

	By shippingCost = By.xpath("//span[@data-zta='shippingCostValueOverview']");

	By checkout = By.xpath("//a[@data-zta='gotoPreviewBottom']");

	By updateButton = By.xpath("//button[@data-zta='shippingCostPopoverAction']");

	By nextButton = By.className("lSNext");

	By previousButton = By.className("lSPrev");

	By recommendationSection = By.xpath("//div[contains(@id,'js-zootopiaRecos')]//ul//li//button");

	public Cart handleCookies() {
		cookie = new Cookies(driver);
		return cookie.cookiesAcceptDialogue();
	}

	public void addAnyItemsToCart(int count) {
		List<WebElement> recommendedItems;
		By rotatingItems = By.xpath(
				"//div[contains(@id,'js-zootopiaRecos-0')]//div[@class='lSAction']//a[contains(@class,'lSNext')]//*[local-name()='svg' and contains(@class,'disabled')]");
		wait(rotatingItems);
		recommendedItems = driver.findElements(recommendationSection);
		for (int i = 0; i < count; i++) {
			try {
				click(recommendedItems.get(i));
				wait(rotatingItems);
			} catch (StaleElementReferenceException ex) {
				System.out.println("Inside catch block");
				recommendedItems = driver.findElements(recommendationSection);
				click(recommendedItems.get(i));
				wait(rotatingItems);
			}
		}

	}

	public void addItemsToCart() {
		addAnyItemsToCart(1);
		Assert.assertTrue(driver.findElements(cartSection).size() == 1);
	}

	public void addItemsToCartFromOverview(int count) {
		addAnyItemsToCart(count);
		Assert.assertTrue(driver.findElements(cartSection).size() > count);
	}

	public TreeMap<String, Double> getPriceOfTheCartProducts() {
		List<WebElement> elements = driver.findElements(cartSection);
		TreeMap<String, Double> productPrice = new TreeMap<String, Double>();
		for (int i = 0; i < elements.size(); i++) {
			WebElement product = elements.get(i).findElement(By.xpath("child::div[@class='product']"));
			String priceWithSymbol = elements.get(i).findElement(By.xpath("child::div[@class='value two__column']"))
					.getText();
			Double price = Double.valueOf(priceWithSymbol.split(Currency.getInstance(Locale.GERMANY).getSymbol())[1]);
			String productName = product.findElement(By.xpath(
					"./child::div[@class='product__info__container']//child::div[@class='product__info two__column']//a//child::span[@data-zta='productName']/span"))
					.getText();
			productPrice.put(productName, price);
		}
		return productPrice;
	}

	public void incrementQuantity(String product, int number) {
		for (int i = 0; i < number; i++) {
			click(driver.findElement(By.xpath("//span[contains(text(),'" + product
					+ "')]/ancestor::div[contains(@class,'cart__table__row two__column')]//following-sibling::button[@data-zta='increaseQuantityBtn']")));
			wait(recommendationSection);
		}
	}

	public void decrementQuantity(String product, int number) {
		for (int i = 0; i < number; i++) {
			click(driver.findElement(By.xpath("//span[contains(text(),'" + product
					+ "')]/ancestor::div[contains(@class,'cart__table__row two__column')]//following-sibling::button[@data-zta='reduceQuantityBtn']")));
		}
	}

	public void deleteItemFromCart(String product) {
		click(driver.findElement(By.xpath("//span[contains(text(),'" + product
				+ "')]/ancestor::div[contains(@class,'cart__table__row two__column')]//following-sibling::button[@data-zta='removeArticleBtn']")));
		wait(recommendationSection);
	}

	public void sortInDescendingOrder(List c) {
		sortItems(c);
	}

	public void clickOnCheckout() {
		click(driver.findElement(checkout));
	}

	public void validateSubTotalAndTotal() {
		Double subTotal = Double.valueOf(
				driver.findElement(subtotal).getText().split(Currency.getInstance(Locale.GERMANY).getSymbol())[1]);
		Double totalValue = Double.valueOf(
				driver.findElement(total).getText().split(Currency.getInstance(Locale.GERMANY).getSymbol())[1]);
		String shippingString = driver.findElement(shippingCost).getText();
		Double shippingValue = Double.valueOf(shippingString.equals("Free") ? "0"
				: shippingString.split(Currency.getInstance(Locale.GERMANY).getSymbol())[1]);
		Double calculatedTotalValue = subTotal + shippingValue;
		Assert.assertTrue(totalValue.equals(calculatedTotalValue));
	}

	public void changeDeliveryCountryChange(String country) {
		click(driver.findElement(shippingCountryName));
		driver.findElement(By.xpath("//div[contains(@class,'countryDropdown ')]//button")).click();
		driver.findElement(
				By.xpath("//li[contains(@class,'dropdown__list__item') and contains(@data-label,'" + country + "')]"))
				.click();
	}

	public void changeDeliveryPostalChange(String postalCode) {
		driver.findElement(postcode).sendKeys(postalCode);
		click(driver.findElement(updateButton));
	}

}
