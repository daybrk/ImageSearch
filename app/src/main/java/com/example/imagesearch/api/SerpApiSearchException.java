package com.example.imagesearch.api;

public class SerpApiSearchException extends Exception {

    public SerpApiSearchException() {
        super();
    }

    public SerpApiSearchException(Exception e) {
        super(e);
    }

    public SerpApiSearchException(String message) {
        super(message);
    }
}