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
    private LinkedHashSet<String> Books;
    private LinkedHashMap<String, ArrayList<String>> Chapters;
    private String id;

    public BibleAPI() {
        Books = new LinkedHashSet<>();
        Chapters = new LinkedHashMap<>();
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
    public void getBooks() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get(this.apiRoute + "/v1/bibles/" + this.id + "/books")
                .header("api-key", this.apiKey)
                .asJson();
        JSONObject dataObj = response.getBody().getObject();
        JSONArray dataArray = dataObj.getJSONArray("data");
        for(int i = 53; i < dataArray.length(); i++){
            JSONObject tempData = dataArray.getJSONObject(i);
            String tempDataId = tempData.get("id").toString();
            this.Books.add(tempDataId);
        }
        System.out.println(this.Books);
    }
    public void getChapters() throws UnirestException {
        for(String key : this.Books){
            ArrayList<String> chapList = new ArrayList<>();
            HttpResponse<JsonNode> response = Unirest.get(apiRoute + "/v1/bibles/" + this.id + "/books/" + key + "/chapters")
                    .header("api-key", this.apiKey)
                    .asJson();
            JSONObject dataObj = response.getBody().getObject();
            JSONArray dataArray = dataObj.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject tempData = dataArray.getJSONObject(i);
                String tempId = tempData.get("id").toString();
                chapList.add(tempId);
            }
            this.Chapters.put(key, chapList);
            System.out.println(this.Chapters);
        }
    }
}
