import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;

public class HTTPHandler {
    public static String httpPut(String url, String body) {

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpPut request = new HttpPut(url);
            StringEntity params = new StringEntity(body);
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse result = httpClient.execute(request);

            String json = EntityUtils.toString(result.getEntity(), "UTF-8");
            try {
                JSONParser parser = new JSONParser();
                Object resultObject = parser.parse(json);

                if (resultObject instanceof JSONArray) {
                    JSONArray array = (JSONArray) resultObject;
                    for (Object object : array) {
                        JSONObject obj = (JSONObject) object;
                    }

                } else if (resultObject instanceof JSONObject) {
                    JSONObject obj = (JSONObject) resultObject;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String httpGetLightStatus(String url) {

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(url);
            request.addHeader("content-type", "application/json");
            HttpResponse result = httpClient.execute(request);
            result.toString();

            String json = EntityUtils.toString(result.getEntity(), "UTF-8");
            try {
                JSONParser parser = new JSONParser();
                Object resultObject = parser.parse(json);


                if (resultObject instanceof JSONArray) {
                    JSONArray array=(JSONArray)resultObject;
                    for (Object object : array) {
                        JSONObject obj =(JSONObject)object;
                        //System.out.println(obj.get("state"));
                    }

                }else if (resultObject instanceof JSONObject) {
                    JSONObject obj =(JSONObject)resultObject;
                    JSONObject state = (JSONObject) obj.get("state");
                    return state.get("on").toString();
                }
                return json;

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void switchlight() {
        String httpResponse = HTTPHandler.httpGetLightStatus(Util.api+"/lights/10");
        if (httpResponse.equalsIgnoreCase("true")) {
            HTTPHandler.httpPut(Util.api+"/lights/10/state","{\"on\":false,\"bri\":254,\"hue\":"+Util.hue_value+"}");
        } else {
            HTTPHandler.httpPut(Util.api+"/lights/10/state","{\"on\":true,\"bri\":254,\"hue\":"+Util.hue_value+"}");
        }
    }

    public static void setColor() {
        String httpResponse = HTTPHandler.httpGetLightStatus(Util.api+"/lights/10");
        if (httpResponse.equalsIgnoreCase("false")) {
            HTTPHandler.httpPut(Util.api+"/lights/10/state","{\"on\":false,\"bri\":254,\"hue\":"+Util.hue_value+"}");
        } else {
            HTTPHandler.httpPut(Util.api+"/lights/10/state","{\"on\":true,\"bri\":254,\"hue\":"+Util.hue_value+"}");
        }
    }
}
