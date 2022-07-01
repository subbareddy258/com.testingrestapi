package testrunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;


@CucumberOptions(
        features = "src/test/java/Feature",
        glue = {"stepDefinitions"},monochrome = true,plugin = { "pretty", "json:target/cucumber.json" })
public class TestRunner extends AbstractTestNGCucumberTests {
}
