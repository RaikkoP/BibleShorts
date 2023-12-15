package org.example;

public class Main {
    public static void main(String[] args) {
        BibleAPI Bible = new BibleAPI();
        try {
            Bible.getBible();
            Bible.getBooks();
            Bible.getChapters();
        }
        catch(Exception err){
            System.out.println(err);
        }

    }
}