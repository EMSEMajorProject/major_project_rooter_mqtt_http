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
        String httpResponse = HTTPHandler.httpGetLightStatus(Util.api+"/lights/"+Util.id_lamp);
        if (httpResponse.equalsIgnoreCase("true")) {
            HTTPHandler.httpPut(Util.api+"/lights/"+Util.id_lamp+"/state","{\"on\":false}");
        } else {
            HTTPHandler.httpPut(Util.api+"/lights/"+Util.id_lamp+"/state","{\"on\":true}");
        }
    }

    public static void setColor() {
        HTTPHandler.httpPut(Util.api+"/lights/"+Util.id_lamp+"/state","{\"bri\":"+Util.bri_value+",\"hue\":"+Util.hue_value+",\"sat\":"+Util.sat_value+"}");
    }

    public static void initColor() {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(Util.api + "/lights/"+Util.id_lamp);
            request.addHeader("content-type", "application/json");
            HttpResponse result = httpClient.execute(request);
            result.toString();

            String json = EntityUtils.toString(result.getEntity(), "UTF-8");
            try {
                JSONParser parser = new JSONParser();
                Object resultObject = parser.parse(json);
                JSONObject obj = (JSONObject) resultObject;
                JSONObject state = (JSONObject) obj.get("state");
                Util.hue_value = Long.parseLong(state.get("hue").toString());
                Util.sat_value = Long.parseLong(state.get("sat").toString());
                Util.bri_value = Long.parseLong(state.get("bri").toString());


            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}

