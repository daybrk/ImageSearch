package com.example.imagesearch;

import com.example.imagesearch.api.SerpApiSearch;

import java.util.Map;

public class GoogleSearch extends SerpApiSearch {

    public GoogleSearch(Map<String, String> parameter, String apiKey) {
        super(parameter, apiKey, "google");
    }

    public GoogleSearch() {
        super("google");
    }

    public GoogleSearch(Map<String, String> parameter) {
        super(parameter, "google");
    }

// end
}