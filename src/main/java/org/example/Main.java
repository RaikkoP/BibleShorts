package org.example;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.client.methods.HttpPost;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLOutput;
import java.util.*;

public class Main {

    //Variables
    private static BibleAPI bible = new BibleAPI();
    private static Scanner input = new Scanner(System.in);
    private static String bibleId = "";
    private static String testament = "";
    private static String bookId = "";
    private static String chapterId = "";
    private static LinkedHashMap<String, ArrayList<String>> books = new LinkedHashMap<>();

    //Function to get BibleID
    static String getBible(String userInput){
        //Local Variable
        String id = "";
        //Get BibleID Section
        switch(userInput){
            case "bibles":
                try {
                    bible.getBibleList();
                }catch(Exception err) {
                    System.out.println(err);
                }
                break;
            default: {
                try {
                    id = bible.getBible(userInput);
                }catch(Exception err) {
                    System.out.println(err);
                }
                break;
            }
        }
        return id;
    }
    //Function to get Testament
    static String pickTestament(String testamentInput) {
        //Get Testament Version: Either Old Testament or New Testament
        switch(testamentInput){
            case "Old Testament":
                try {
                    return "Old Testament";
                }catch(Exception err){
                    System.out.println(err);
                }
            case "New Testament":
                try{
                    return "New Testament";
                }catch(Exception err){
                    System.out.println(err);
                }
            default: {
                break;
            }
        }
        return null;
    }
    //Get books of Bible
    static ArrayList<String> bibleBookList (String id, String testament) {
        ArrayList<String> bookList = new ArrayList<>();
        try{
            books = bible.getBooks(id, testament);
            for(String book : books.keySet()){
                bookList.add(book);
            }
        }catch(Exception err){
            System.out.println(err);
        }
        return bookList;
    }
    //Get chapters of book
    static ArrayList<String> chaptersOfBook (String bookId){
        return books.get(bookId);
    }

    //Get passage without HTML code
    static String getPassage (){
        String response = "";
        //Get passages
        try {
            response = bible.getPassages(chapterId, bibleId);
        }catch(Exception err){
            System.out.println(err);
        }
        //Split into array at HTML element points
        return response.replaceAll("<.*?>", "").replaceAll("[0-9]", "");
    }
    static String resolvePythonScriptPath(String path){
        File file = new File(path);
        return file.getAbsolutePath();
    }


    //Main Function
    public static void main(String[] args) {

        //Get ID of chosen Bible
        while(bibleId.equals("") || bibleId.isBlank() || bibleId.isEmpty() || bibleId.equals(null)){
            System.out.println("Please enter the translation of the Bible you are interested in...");
            System.out.println("If you wish to see a list type in 'bibles'");
            String biblesInput = input.nextLine();
            bibleId = getBible(biblesInput);
        }
        //Get Testament Version
        while(testament.equals("") || testament.isBlank() || testament.isEmpty() || testament.equals(null)){
            System.out.println("Please select either the New Testament or Old Testament");
            String testamentInput = input.nextLine();
            testament = pickTestament(testamentInput);
        }
        //Get Book ID
        while(bookId.equals("") || bookId.isBlank() || bookId.isEmpty() || bookId.equals(null)) {
            ArrayList<String> tempArray = bibleBookList(bibleId, testament);
            for(String book : tempArray){
                System.out.println(book);
            }
            System.out.println("Please pick a book....");
            String chosenBook = input.nextLine();
            if(tempArray.contains(chosenBook)){
                bookId = chosenBook;
            }
        }
        //Get Chapter ID
        while(chapterId.equals("") || chapterId.isEmpty() || chapterId.isBlank() || chapterId.equals(null)) {
            ArrayList<String> chapterArray = chaptersOfBook(bookId);
            for(String chapter : chapterArray){
                System.out.println(chapter);
            }
            System.out.println("Please pick a chapter....");
            String chosenChapter = input.nextLine();
            if(chapterArray.contains(chosenChapter)){
                chapterId = chosenChapter;
            }
        }
        try {
            HttpResponse<JsonNode> response = Unirest.post("http://127.0.0.1:5000/" + getPassage()).asJson();
        } catch (Exception err){
            System.out.println(err);
        }
        System.out.println(getPassage());
    }
}