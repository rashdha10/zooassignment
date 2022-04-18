
package stepDefinition;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.Cart;
import webdriver.DriverFactory;

/**
 * @author 91809
 *
 */
public class stepdef {

	WebDriver driver = DriverFactory.getWebDriver();
	Cart cart;
	Map<String, Double> priceProductMap;

	@Given("that user is on cartpage")
	public void that_user_is_on_cartpage() {
		cart = new Cart(driver);
		cart.handleCookies();
	}

	@When("user add the products from the recommended section")
	public void user_add_the_products_from_the_recommended_section() {
		/**
		 * Just a method to add only item in the cart, can be changed as single method
		 **/
		cart.addItemsToCart();
	}

	@Then("user should be on overview page")
	public void user_should_be_on_overview_page() {
		Assert.assertTrue(driver.getCurrentUrl().contains("overview"));
	}

	@Then("user adds few more products from the recommended section {string}")
	public void user_adds_few_more_products_from_the_recommended_section(String count) {
		cart.addItemsToCartFromOverview(Integer.valueOf(count));
	}

	@Then("display the prices of the products added in an order {string}")
	public void display_the_prices_of_the_products_added_in_an_order(String order) {
		priceProductMap = cart.getPriceOfTheCartProducts();
		if (order.equals("Descending")) {
			cart.sortInDescendingOrder(priceProductMap.values().stream().collect(Collectors.toList()));
		}
	}

	@When("user increments the products from lowest price")
	public void user_increments_the_products_from_lowest_price() {
		int i = 0;
		priceProductMap = priceProductMap.entrySet().stream().sorted(Entry.comparingByValue())
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		for (Map.Entry<String, Double> entry : priceProductMap.entrySet()) {
			if (i == priceProductMap.size() - 1) {
				break;
			}
			cart.incrementQuantity(entry.getKey(), 1);
			i++;
		}
	}

	@When("delete the product with highest price")
	public void delete_the_product_with_highest_price() {
		cart.deleteItemFromCart(
				priceProductMap.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).get().getKey());
	}

	@Then("the total and subtotal should be correct with shipping charge")
	public void the_total_and_subtotal_should_be_correct_with_shipping_charge() {
		cart.validateSubTotalAndTotal();
	}

	@When("user changes the shipping country {string}")
	public void user_changes_the_shipping_country(String country) {
		cart.changeDeliveryCountryChange(country);
	}

	@When("add the postal code {string}")
	public void add_the_postal_code(String postalCode) {
		cart.changeDeliveryPostalChange(postalCode);
	}

}
