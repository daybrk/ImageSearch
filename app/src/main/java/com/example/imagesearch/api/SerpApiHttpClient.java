package com.example.imagesearch.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * HTTPS search for Serp API
 */
public class SerpApiHttpClient {
    // http request configuration
    private int httpConnectionTimeout = 5000;
    private int httpReadTimeout = 5000;

    public static String VERSION = "2.0.1";
    public static String BACKEND = "https://serpapi.com";

    // initialize gson
    private static Gson gson = new Gson();

    // path
    public String path;

    public SerpApiHttpClient(String path) {
        this.path = path;
    }

    public HttpURLConnection buildConnection(String path, Map<String, String> parameter) throws SerpApiSearchException {
        HttpURLConnection con;
        try {
//            allowHTTPS();
            String query = ParameterStringBuilder.getParamsString(parameter);
            URL url = new URL(BACKEND + path + "?" + query);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            // TODO Enable to set different timeout
            con.setConnectTimeout(getHttpConnectionTimeout());
            con.setReadTimeout(getHttpReadTimeout());
            con.connect();
        } catch (IOException e) {
            throw new SerpApiSearchException(e);
        }
        return con;
    }

    /***
     * Get results
     *
     * @param parameter
     * @return http response body
     */
    public String getResults(Map<String, String> parameter) throws SerpApiSearchException {
        HttpURLConnection con = buildConnection(this.path, parameter);

        // Get HTTP status
        int statusCode = -1;
        // Hold response stream
        InputStream is = null;
        // Read buffer
        BufferedReader in = null;
        try {
            statusCode = con.getResponseCode();

            if (statusCode == 200) {
                is = con.getInputStream();
            } else {
                is = con.getErrorStream();
            }
            Reader reader = new InputStreamReader(is);
            in = new BufferedReader(reader);
        } catch (IOException e) {
            throw new SerpApiSearchException(e);
        }
        String inputLine;
        StringBuffer content = new StringBuffer();
        try {
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            throw new SerpApiSearchException(e);
        }

        System.out.println(content.toString());
        // Disconnect
        con.disconnect();

        if (statusCode != 200) {
            triggerSerpApiClientException(content.toString());
        }
        return content.toString();
    }

    public void triggerSerpApiClientException(String content) throws SerpApiSearchException {
        String errorMessage;
        try {
            JsonObject element = gson.fromJson(content, JsonObject.class);
            errorMessage = element.get("error").getAsString();
        } catch (Exception e) {
            throw new AssertionError("invalid response format: " + content);
        }
        throw new SerpApiSearchException(errorMessage);
    }

    public int getHttpConnectionTimeout() {
        return httpConnectionTimeout;
    }

    public void setHttpConnectionTimeout(int httpConnectionTimeout) {
        this.httpConnectionTimeout = httpConnectionTimeout;
    }

    public int getHttpReadTimeout() {
        return httpReadTimeout;
    }

    public void setHttpReadTimeout(int httpReadTimeout) {
        this.httpReadTimeout = httpReadTimeout;
    }

}