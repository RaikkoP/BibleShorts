package org.example;

import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        while(true) {

            //Variables
            BibleAPI Bible = new BibleAPI();
            ArrayList<String> books = new ArrayList<>();
            ArrayList<String> chapters = new ArrayList<>();
            Scanner input = new Scanner(System.in);
            String bibleId = "";

            //Main Program Loop
            System.out.println("Please enter the translation of the Bible you are interested in...");
            System.out.println("If you wish to see a list type in 'bibles'");
            String biblesInput = input.nextLine();

            //Get BibleID Section
            switch(biblesInput){
                case "bibles":
                    try {

                    } catch(Exception err) {
                        System.out.println(err);
                    }
                    break;
                default: {
                    try {
                        bibleId = Bible.getBible(biblesInput);
                    } catch(Exception err) {
                        System.out.println(err);
                    }
                    break;
                }
            }
            //Get Bible Books
            //Get Bible Chapters
            try {
                books = Bible.getBooks(bibleId);
                chapters = Bible.getChapters(books, bibleId);
            }
            catch(Exception err){
                System.out.println(err);
            }
        }
    }
}