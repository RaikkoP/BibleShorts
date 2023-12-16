package org.example;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

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
    public BibleAPI() {
        apiRoute = dotenv.get("API_ROUTE");
        apiKey = dotenv.get("API_KEY");
    }
    public String getBible(String userInput) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get(this.apiRoute + "/v1/bibles")
                .header("api-key", this.apiKey)
                .asJson();
        JSONObject jsonObject = response.getBody().getObject();
        JSONArray dataArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < dataArray.length(); i++){
            JSONObject obj = dataArray.getJSONObject(i);
            Object parsedObj = obj.get("name");
            parsedObj.toString();
            if(parsedObj.toString().contains(userInput)){
                Object getId = obj.get("id");
                return getId.toString();
            }
        }
        System.out.println("Bible not found!");
        return null;
    }
    public ArrayList<String> getBooks(String id) throws UnirestException {
        ArrayList<String> result = new ArrayList<>();
        HttpResponse<JsonNode> response = Unirest.get(this.apiRoute + "/v1/bibles/" + id + "/books")
                .header("api-key", this.apiKey)
                .asJson();
        JSONObject dataObj = response.getBody().getObject();
        JSONArray dataArray = dataObj.getJSONArray("data");
        for(int i = 39; i < dataArray.length(); i++){
            JSONObject tempData = dataArray.getJSONObject(i);
            String tempDataId = tempData.get("id").toString();
            result.add(tempDataId);
        }
        System.out.println(result);
        return result;
    }
    public ArrayList<String> getChapters(ArrayList<String> books, String id) throws UnirestException {
        ArrayList<String> chapters = new ArrayList<>();
        for(String book : books){
            HttpResponse<JsonNode> response = Unirest.get(apiRoute + "/v1/bibles/" + id + "/books/" + book + "/chapters")
                    .header("api-key", this.apiKey)
                    .asJson();
            JSONObject dataObj = response.getBody().getObject();
            JSONArray dataArray = dataObj.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject tempData = dataArray.getJSONObject(i);
                String tempId = tempData.get("id").toString();
                chapters.add(tempId);
            }
            System.out.println(chapters);
        }
        return chapters;
    }
    public LinkedHashMap<String, ArrayList<Integer>> getSections(){
        return null;
    }
}
