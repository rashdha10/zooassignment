
package pages;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;

import junit.framework.Assert;

/**
 * @author 91809
 *
 */
public class BasePage {

	public static final Set<Class<? extends RuntimeException>> IGNORE_EXCEPTIONS_WHILE_WAITING_SET = ImmutableSet.of(
			NotFoundException.class, ElementNotVisibleException.class, IndexOutOfBoundsException.class,
			NullPointerException.class, StaleElementReferenceException.class, IllegalStateException.class,
			NoSuchFrameException.class, WebDriverException.class, NoSuchElementException.class, TimeoutException.class);

	protected static WebDriver driver;

	public int defaultTimeOut = 15;

	public BasePage(WebDriver driver) {
		this.driver = driver;
	}

	public void click(WebElement e) {
		try {
			e.click();
			waitTillPageLoads();
		} catch (Exception exception) {
			Object[] args = new Object[1];
			args[0] = e;
			executeJs("arguments[0].click()", args);
			waitTillPageLoads();
		}
	}

	public void waitForElement(boolean isVisible, WebElement element) {
		boolean actual = fluentWait(driver -> {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(defaultTimeOut));
			wait.until(ExpectedConditions.visibilityOf(element));
			return true;
		}, 5);
		Assert.assertEquals(actual, isVisible);
	}

	public void wait(By locator) {
		boolean actual = fluentWait(driver -> {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(defaultTimeOut));
			wait.until(ExpectedConditions.visibilityOf(driver.findElement(locator)));
			return true;
		}, 5);
	}

	private boolean fluentWait(Function<WebDriver, Boolean> predicate, int waitForSecs) {
		try {
			return new FluentWait<>(driver).withTimeout(Duration.ofSeconds(waitForSecs))
					.pollingEvery(Duration.ofSeconds(15)).ignoreAll(IGNORE_EXCEPTIONS_WHILE_WAITING_SET)
					.until(predicate);
		} catch (TimeoutException | NoSuchElementException e) {
			return false;
		}
	}

	public void clickOn(By e) {
		new WebDriverWait(driver, Duration.ofSeconds(defaultTimeOut))
				.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(e)));
		driver.findElement(e).click();
	}

	public boolean waitTillPageLoads() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		ExpectedCondition<Boolean> successCondition = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				Object[] args = new Object[0];
				return BasePage.this.executeJs("return document.readyState", args).equals("complete");
			}
		};
		boolean success;
		try {
			success = wait.until(successCondition);
		} catch (Exception e) {
			success = false;
		}
		return success;
	}

	private Object executeJs(String cmd, Object[] args) {
		JavascriptExecutor javaScriptExecutor = (JavascriptExecutor) this.driver;
		return javaScriptExecutor.executeScript(cmd, args);
	}

	protected void sortItems(List c) {
		System.out.println(c);
		Collections.sort(c, Collections.reverseOrder());
		System.out.println(c);
	}

}
