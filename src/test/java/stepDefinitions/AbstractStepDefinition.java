package stepDefinitions;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class AbstractStepDefinition {
    public static final  String COMPONENT_USER= "userget";
    static  String clientToken1,clientToken2,testId,userId;
    public static Response commonResponse;
    public static Response getListOfUsersResp,addUsersResp,getUsersResp;
    static Response getswaggerValidationResp;
    static JsonPath orderResponse,getUsersRespJson;
}
