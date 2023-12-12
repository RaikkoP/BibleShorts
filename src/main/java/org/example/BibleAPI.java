package org.example;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONArray;
import org.json.JSONObject;


public class BibleAPI {
    private Dotenv dotenv = Dotenv.load();
    private String apiKey;
    private String apiRoute;
    private String id;

    public BibleAPI() {
        apiRoute = dotenv.get("API_ROUTE");
        apiKey = dotenv.get("API_KEY");
    }
    public void getBible() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get(this.apiRoute + "/v1/bibles")
                .header("api-key", this.apiKey)
                .asJson();
        JSONObject jsonObject = response.getBody().getObject();
        JSONArray dataArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < dataArray.length(); i++){
            JSONObject obj = dataArray.getJSONObject(i);
            Object parsedObj = obj.get("name");
            parsedObj.toString();
            if(parsedObj.toString().contains("King James")){
                Object getId = obj.get("id");
                this.id = getId.toString();
            }
        }
    }
    public void getChapters() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get(apiRoute + "/v1/bibles/" + this.id + "/books")
                .header("api-key", this.apiKey)
                .asJson();
        JSONObject dataObj = response.getBody().getObject();
        JSONArray dataArray = dataObj.getJSONArray("data");
        for(int i = 0; i < dataArray.length(); i++){
            JSONObject tempData = dataArray.getJSONObject(i);
            Object tempDataName = tempData.get("nameLong");
            Object tempDataId = tempData.get("id");
            System.out.println(tempDataName.toString() + " " + tempDataId.toString());
        }
    }
}
