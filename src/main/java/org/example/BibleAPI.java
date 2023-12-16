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

    //Prints out list of Bibles
    public void getBibleList() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get(this.apiRoute + "/v1/bibles")
                .header("api-key", this.apiKey)
                .asJson();
        JSONObject jsonObject = response.getBody().getObject();
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject tempObj = jsonArray.getJSONObject(i);
            String bibleName = tempObj.get("name").toString();
            System.out.println(bibleName);
        }
    }

    //Get chosen Bibles ID
    public String getBible(String userInput) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get(this.apiRoute + "/v1/bibles")
                .header("api-key", this.apiKey)
                .asJson();
        JSONObject jsonObject = response.getBody().getObject();
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject obj = jsonArray.getJSONObject(i);
            Object parsedObj = obj.get("name").toString();
            if(parsedObj.toString().contains(userInput)){
                Object getId = obj.get("id");
                return getId.toString();
            }
        }
        System.out.println("Bible not found!");
        return null;
    }

    //Return all books of Bible
    public void getBooks(String id, String testament) throws UnirestException {
        int startingBook = 0;
        int endingBooks = 0;
        HttpResponse<JsonNode> response = Unirest.get(this.apiRoute + "/v1/bibles/" + id + "/books")
                .header("api-key", this.apiKey)
                .asJson();
        JSONObject dataObj = response.getBody().getObject();
        JSONArray jsonArray = dataObj.getJSONArray("data");
        if(testament.contains("Old Testament")) {
            endingBooks = 39;
        } else if(testament.contains("New Testament")) {
            startingBook = 39;
            endingBooks = jsonArray.length();
        }
        for(int i = startingBook; i < endingBooks; i++){
            JSONObject tempData = jsonArray.getJSONObject(i);
            String tempDataId = tempData.get("id").toString();
            System.out.println(tempDataId);
        }
    }

    public ArrayList<String> getChapters(ArrayList<String> books, String id) throws UnirestException {
        ArrayList<String> chapters = new ArrayList<>();
        for(String book : books){
            HttpResponse<JsonNode> response = Unirest.get(apiRoute + "/v1/bibles/" + id + "/books/" + book + "/chapters")
                    .header("api-key", this.apiKey)
                    .asJson();
            JSONObject dataObj = response.getBody().getObject();
            JSONArray jsonArray = dataObj.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject tempData = jsonArray.getJSONObject(i);
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
