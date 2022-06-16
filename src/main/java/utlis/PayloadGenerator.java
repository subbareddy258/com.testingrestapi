package utlis;

import org.json.JSONObject;

public class PayloadGenerator {


    public JSONObject testRequestBody(String test)
    {
        JSONObject testReqBody =  new JSONObject();
        testReqBody.put("test",test);
        return testReqBody;
    }
}
