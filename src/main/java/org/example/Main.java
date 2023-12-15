package org.example;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Main {
    public static void main(String[] args) {
        BibleAPI Bible = new BibleAPI();
        ArrayList<String> books = new ArrayList<>();
        LinkedHashMap<String, ArrayList<String>> chapters = new LinkedHashMap<>();
        try {
            String bibleId = Bible.getBible();
            books = Bible.getBooks(bibleId);
            chapters = Bible.getChapters(books, bibleId);
        }
        catch(Exception err){
            System.out.println(err);
        }

    }
}