package stepDefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import stepDefinationsImpl.ReqresImpl;
import utlis.PayloadGenerator;
import utlis.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;
import static org.testng.AssertJUnit.fail;
import static utlis.Utils.*;

public class usersStepdefinition extends AbstractStepDefinition {
    ReqresImpl ReqresImpl = new ReqresImpl();
    SoftAssert softAssert = new SoftAssert();

    @When("client retrieve the get users with {string}")
    public void test(String scenario) {
        Map<String, String> queryParams = new HashMap<String, String>();
        switch (scenario) {
            case "valid":
                queryParams.put("page", "2");
                queryParams.put("per_page", "2");
                break;
        }
        ReqresImpl.getUsers(queryParams);
    }

    @Then("the status code of {string} Response is {int} and Response Body matches with {string} swagger schema")
    public void swaggerSchema(String action, int statusCode, String schema) throws IOException {
        Response response = null;
        String component = null;
        switch (action) {
            case "getusers":
                response = getUsersResp;
                component = COMPONENT_USER;
                break;
            default:
                fail("not founr");
        }
        Assert.assertEquals(response.statusCode(), statusCode, "\nx-request-id->" + response.getHeader("x-request-id") + "\nResponse Body is\n" + response.prettyPrint());
        if (component != null && Utils.swaggerValidationRequired()) {
            getswaggerValidationResp = Utils.getswaggerValidation(component, response, schema);
            Assert.assertEquals(getswaggerValidationResp.then().extract().jsonPath().get("validationStatus"), "Success", "\n[[CODE ISSUE]] Response Body :-\n" +
                    response.then().extract().response().asString()
                    + "\nIs not matching with swagger doc ." + "Error Returned is\n" +
                    getswaggerValidationResp.then().extract().jsonPath().get("reports").toString());
            System.out.print("[[INFO]] Swagger validation success for" + component + "\n");
        }
    }


    @Given("User is on Home page")
    public void userOnHomePage() {
        ChromeOptions chromeOptions = new ChromeOptions();
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.get("https://www.demoqa.com");
    }

    @And("The Error code and Error Response while user {string} Displayed Correctly")
    public void theErrorCodeAndErrorResponseWhileUserGetusersDisplayedCorrectly(String useraction, DataTable errorArrayAsDataTable) {
        Response response = null;
        int i = 0;
        switch (useraction.toLowerCase()) {
            case "getusers":
                response = getUsersResp;
                break;
            default:
                fail("not founrd");

        }
        String expectedmessage, actualmessage;
        if (!Objects.isNull(errorArrayAsDataTable.asMaps(String.class, String.class).get(0).get("ErrorMessage"))
                && "Invalid Authz token".equalsIgnoreCase(errorArrayAsDataTable.asMaps(String.class, String.class).get(0).get("ErrorMessage"))) {
            Assert.assertEquals(response.then().extract().jsonPath().getString("error"), "Invalid Auth token");

        } else {
            Assert.assertEquals(response.then().extract().jsonPath().getString("errors.size()"), errorArrayAsDataTable.asMaps(String.class, String.class).size());
            if (!Objects.isNull(errorArrayAsDataTable.asMaps(String.class, String.class).get(0).get("ErrorMessageArray"))) {
                JSONAssert.assertEquals(Utils.getExpectedErrorArray(errorArrayAsDataTable, response), Utils.getActualErrorArray(response), JSONCompareMode.LENIENT);

            } else {
                for (Map<String, String> data : errorArrayAsDataTable.asMaps(String.class, String.class)) {
                    expectedmessage = data.get("ErrorMessage").toString();
                    if (expectedmessage.contains("%s")) {
                        expectedmessage = getFormattedErrorMsg(data.get("ErrorMessage").toString(), response);
                    }
                    actualmessage = response.then().extract().jsonPath().getString("errors[" + i + "].message");
                    Assert.assertEquals(response.then().extract().jsonPath().getString("errors[" + i + "].code"), data.get("ErrorCode"));
                    Assert.assertTrue(actualmessage.contains(expectedmessage), "Actual Message:-" + actualmessage + " doesn't contain expected message:-" + expectedmessage);
                    i++;
                }

            }

        }
    }

    private String getFormattedErrorMsg(String errormessage, Response response) {
        if (errormessage.contains("id")) {
            errormessage = String.format(errormessage, userId, getXrequestId(response));
        } else {
            errormessage = String.format(errormessage, getXrequestId(response));
        }
        return errormessage;
    }


    @And("Verifies get users displayed properly")
    public void verifiesGetUsersDisplayedProperly() {
        getUsersRespJson = new JsonPath(getUsersResp.asString());
        softAssert.assertEquals(getUsersRespJson.getString("total"), "12");
        for (int i = 0; i < getUsersRespJson.getInt("data.size"); i++) {
            softAssert.assertNotNull(getUsersRespJson.getString("data[" + i + "].email"), "email not found" + i);
            softAssert.assertNotNull(getUsersRespJson.getString("data[" + i + "].first_name"), "first name not found" + i);
            softAssert.assertNotNull(getUsersRespJson.getString("data[" + i + "].last_name"), "last name not found" + i);
            softAssert.assertNotNull(getUsersRespJson.getString("data[" + i + "].id"), "id not found" + i);
            softAssert.assertNotNull(getUsersRespJson.getString("data[" + i + "].avatar"), "avatar not found" + i);

        }
        softAssert.assertAll();
    }

    @Given("client token is genrated")
    public void clientTokenIsGenrated() throws IOException {
       /* clientToken1 =given().post(Utils.getPropertyData("bookerUrl") + "/auth").body()
                .log().all().assertThat().statusCode(200).extract().jsonPath().getString("token");
   */    }

    @Given("token is generated")
    public void Genrated() throws IOException {
        System.out.println(PayloadGenerator.token());
       String token= given().header("Content-Type"," application/json").body(PayloadGenerator.token()).post("https://restful-booker.herokuapp.com/auth").then().log().all().toString();
       System.out.println(token);
    }
}
