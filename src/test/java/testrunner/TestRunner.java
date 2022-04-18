
package testrunner;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

/**
 * @author 91809
 *
 */
@RunWith(Cucumber.class)
@CucumberOptions(features = { "src/test/resources/features" }, glue = { "stepDefinition" }, plugin = { "pretty",
		"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:", "timeline:test-output-thread/" }

)
public class TestRunner {
}
