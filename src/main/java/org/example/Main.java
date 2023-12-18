package org.example;

import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Main {

    //Variables
    private static BibleAPI bible = new BibleAPI();
    private static Scanner input = new Scanner(System.in);
    private static String bibleId = "";
    private static String testament = "";
    private static String bookId = "";
    private static String chapterId = "";

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
                    return "OLd Testament";
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
    static ArrayList<String> bibleBookList (String testament, String id) {
        try{
            return bible.getBooks(id, testament);
        }catch(Exception err){
            System.out.println(err);
        }
        return null;
    }
    //Get chapters of book
    static ArrayList<String> chaptersOfBook (String bookId, String bibleId){
        try {
            return bible.getChapters(bibleId, bookId);
        }catch(Exception err){
            System.out.println(err);
        }
        return null;
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
            ArrayList<String> tempArray = chaptersOfBook(bookId, bibleId);
            for(String chapter : tempArray){
                System.out.println(chapter);
            }
            System.out.println("Please pick a chapter....");
            String chosenChapter = input.nextLine();
            if(tempArray.contains(chosenChapter)){
                chapterId = chosenChapter;
            }
        }




        while(true) {


            if(bibleId.isEmpty() || bibleId.isBlank()){
                continue;
            } else {
                break;
            }
        }



        //Get Bible Chapters
        System.out.println("Please pick what chapter you wish to use..");
        String chapterInput = input.nextLine();
    }
}