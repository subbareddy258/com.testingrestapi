package stepDefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.response.Response;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import stepDefinationsImpl.ReqresImpl;
import utlis.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class usersStepdefinition extends AbstractStepDefinition
{
    ReqresImpl ReqresImpl =new ReqresImpl();
    @When("client retrieve the get users with {string}")
    public void test(String scenario)
    {
        Map<String,String> queryParams = new HashMap<>();
        switch (scenario.toLowerCase())
        {
            case "valid":
                queryParams.put("page","2");
                queryParams.put("per_page","2");
              break;
        }
        ReqresImpl.getUsers(queryParams);
    }

    @Then("the status code of {string} Response is {int} and Response Body matches with {string} swagger schema")
    public void swaggerSchema(String action, int statusCode ,String schema) throws IOException {
        Response response = null;
        String component =null;
        switch (action.toLowerCase())
        {
            case "getusers":
                response =getUsersResp;
                component=COMPONENT_USER;
        }
        Assert.assertEquals(response.statusCode(),statusCode,"\nx-request-id->" + response.getHeader("x-request-id") +"\nResponse Body is\n"+response.prettyPrint());
        if(component !=null && Utils.swaggerValidationRequired())
        {
           getswaggerValidationResp= Utils.getswaggerValidation(component,response,schema);
           Assert.assertEquals(getswaggerValidationResp.then().extract().jsonPath().get("validationStatus"),"Success","\n[[CODE ISSUE]] Response Body :-\n" +
                   response.then().extract().response().asString()
                   + "\nIs not matching with swagger doc ." + "Error Returned is\n" +
                   getswaggerValidationResp.then().extract().jsonPath().get("reports").toString()) ;
           System.out.print("[[INFO]] Swagger validation success for" +component + "\n");
        }
    }



    @Given("User is on Home page")
    public void userOnHomePage() {
        ChromeOptions chromeOptions = new ChromeOptions();
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.get("https://www.demoqa.com");
    }
}
