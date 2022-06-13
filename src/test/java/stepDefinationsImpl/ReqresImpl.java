package stepDefinationsImpl;

import io.restassured.response.Response;
import stepDefinitions.AbstractStepDefinition;
import stepDefinitionsInterfaces.Reqres;
import utlis.Utils;

import java.io.IOException;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ReqresImpl extends AbstractStepDefinition implements Reqres {


    public Response getListOfUsers(Map<String,String > queryParams, String token)
    {
        try
        {
commonResponse= given(Utils.testRequestSpecification()).header("Authorization","Bearer"+token)
        .queryParams(queryParams).log().all().when()
        .get(Utils.getPropertyData("url"))
        .then().log().all().extract().response();
        } catch (IOException e) {
            e.printStackTrace();
        }
       getListOfUsersResp= commonResponse;
    return commonResponse;
}

    public Response addUsers(String requestBody, String token)
    {
        try
        {
            commonResponse= given(Utils.testRequestSpecification()).header("Authorization","Bearer"+token)
                    .body(requestBody).log().all().when()
                    .get(Utils.getPropertyData("url"))
                    .then().log().all().extract().response();
        } catch (IOException e) {
            e.printStackTrace();
        }
        addUsersResp= commonResponse;
        return commonResponse;
    }

    public Response updateUsers(String requestBody, String token)
    {
        try
        {
            commonResponse= given(Utils.testRequestSpecification()).header("Authorization","Bearer"+token)
                    .body(requestBody).log().all().when()
                    .get(Utils.getPropertyData("url"))
                    .then().log().all().extract().response();
        } catch (IOException e) {
            e.printStackTrace();
        }
        addUsersResp= commonResponse;
        return commonResponse;
    }

@Override
    public Response getUsers(Map<String,String> queryParams)
    {
        try
        {
            commonResponse= given().queryParams(queryParams)
                    .log().all().when()
                    .get(Utils.getPropertyData("url"))
                    .then().log().all().extract().response();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getUsersResp= commonResponse;
        return commonResponse;
    }
}
