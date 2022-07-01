package utlis;

import org.json.JSONObject;

public class PayloadGenerator {
String payload;

    public JSONObject testRequestBody(String test)
    {
        JSONObject testReqBody =  new JSONObject();
        testReqBody.put("test",test);
        return testReqBody;
    }
    public static JSONObject token()
    {
        JSONObject tokenRequestBody =  new JSONObject();
        tokenRequestBody.put("password","password123");
        tokenRequestBody.put("username","admin");
        return tokenRequestBody;
    }
}
