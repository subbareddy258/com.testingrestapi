package stepDefinitionsInterfaces;

import io.restassured.response.Response;

import java.util.Map;

public interface Reqres {
    Response getUsers(Map<String,String> queryParams);
}
