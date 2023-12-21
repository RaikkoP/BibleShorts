package org.example;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
                .queryString("name", userInput)
                .asJson();
        JSONObject tempObject = response.getBody().getObject();
        JSONArray tempArray = tempObject.getJSONArray("data");
        String bibleId = tempArray.getJSONObject(0).get("id").toString();
        return bibleId;
    }

    //Return all books of Testament
    public LinkedHashMap<String, ArrayList<String>> getBooks(String id, String testament) throws UnirestException {
        LinkedHashMap<String, ArrayList<String>> booksList = new LinkedHashMap<>();
        int startingBook = 0;
        int endingBooks = 0;
        HttpResponse<JsonNode> response = Unirest.get(this.apiRoute + "/v1/bibles/" + id + "/books")
                .header("api-key", this.apiKey)
                .queryString("include-chapters", "true")
                .asJson();
        JSONObject tempData = response.getBody().getObject();
        JSONArray tempArray = tempData.getJSONArray("data");
        if(testament.contains("Old Testament")) {
            endingBooks = 39;
        } else if(testament.contains("New Testament")) {
            startingBook = 39;
            endingBooks = tempArray.length();
        }
        for(int i = startingBook; i < endingBooks; i++){
            ArrayList<String> chapters = new ArrayList<>();
            String tempId = tempArray.getJSONObject(i).get("id").toString();
            JSONArray tempChapters = tempArray.getJSONObject(i).getJSONArray("chapters");
            for(int j = 0; j < tempChapters.length(); j++){
                System.out.println(tempChapters.getJSONObject(j));
                String tempChapter = tempChapters.getJSONObject(j).get("id").toString();
                chapters.add(tempChapter);
            }
            booksList.put(tempId, chapters);
        }
        return booksList;
    }
    //Get all verses from chapter
    public LinkedHashMap<String, Integer> getPassages(String chapterId, String bibleId) throws UnirestException{
        LinkedHashMap<String, Integer> sections = new LinkedHashMap<>();
        HttpResponse<JsonNode> response = Unirest.get(this.apiRoute + "/v1/bibles/" + bibleId + "/passages/" + chapterId)
                .header("api-key", this.apiKey)
                .asJson();
        System.out.println(response);
        JSONObject tempData = response.getBody().getObject();
        System.out.println(tempData);
        return null;
    }
}
