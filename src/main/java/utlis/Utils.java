package utlis;

import io.cucumber.datatable.DataTable;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;

import java.util.Map;
import java.util.Properties;

public class Utils {
    public static String stack, browser;
    static Properties prop = new Properties();

    public static String getStackForSim() {
        if (StringUtils.isNotBlank(System.getProperty("stack"))) {
            return System.getProperty("testing");
        }
        if (stack == null) {
            stack = System.getProperty("stack", "testing").toLowerCase();
        }
        if ("testers".equalsIgnoreCase(stack)) {
            return "testsre";
        } else {
            return stack;
        }
    }

    public static String getStack() {
        if (stack == null) {
            stack = System.getProperty("stack", "test").toLowerCase();
        }
        return stack;
    }

    public static RequestSpecification testRequestSpecification() throws IOException {
        if ("no".equalsIgnoreCase(System.getProperty("proxy"))) {
            return new RequestSpecBuilder().setBaseUri(Utils.getPropertyData("environment url"))
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("User-Agent", getPropertyData("agent")).setUrlEncodingEnabled(false).setRelaxedHTTPSValidation()
                    .build();
        } else {
            return new RequestSpecBuilder().setBaseUri(Utils.getPropertyData("environment url"))
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("User-Agent", getPropertyData("agent")).setUrlEncodingEnabled(false).setRelaxedHTTPSValidation()
                    .build();
        }
    }

    public static RequestSpecification RequestSpecification() throws IOException {
        if ("no".equalsIgnoreCase(System.getProperty("proxy"))) {
            return new RequestSpecBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("User-Agent", getPropertyData("agent")).setUrlEncodingEnabled(false).setRelaxedHTTPSValidation()
                    .build();
        } else {
            return new RequestSpecBuilder().setProxy(getProxyHostName())
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("User-Agent", getPropertyData("agent")).setUrlEncodingEnabled(false).setRelaxedHTTPSValidation()
                    .build();
        }
    }

    public static String getPropertyData(String getData) throws IOException {
        String path;
        switch (getStack()) {
            case "test":
                path = new File("src/main/java/config/test.properties").getAbsolutePath();
                break;
            default:
                throw new IllegalStateException("test fail");
        }
        FileInputStream in = new FileInputStream(path);
        prop.load(in);
        return prop.getProperty(getData);
    }

    public static boolean swaggerValidationRequired() {
        return "yes".equalsIgnoreCase(System.getProperty("swagger.validation"));
    }

    public static Response getswaggerValidation(String component, Response response, String schema) throws IOException {
        return given(RequestSpecification()).pathParam("component", component).pathParam("schemaType", schema)
                .body(response.then().extract().response().asString())
                .post(getPropertyData("endPoint") + "/swagger/validate/{component}/{SchemaType")
                .then().assertThat().statusCode(200).extract().response();
    }


    public static String getProxyHostName() {
        return System.getProperty("proxy", "test").toLowerCase();
    }

    public static String getXrequestId(Response response) {
        return response.getHeader("x-request-id");
    }

    public static JSONArray getExpectedErrorArray(DataTable errorArrayAsTable, Response response) {
        JSONArray array = new JSONArray();
        for (Map<String, String> data : errorArrayAsTable.asMaps(String.class, String.class)) {
            JSONObject jsonobject = new JSONObject();
            String errorMessage = data.get("ErrorMessageArray").toString();
            jsonobject.put("code", data.get("ErrorCode").toString());
            jsonobject.put("message", errorMessage.contains("%s") ? String.format(errorMessage, getXrequestId(response)) : errorMessage);
            array.put(jsonobject);
        }
        return array;
    }


    public static JSONArray getActualErrorArray(Response response)
    {
        return new JSONArray(response.jsonPath().getList("errors"));
    }
}
