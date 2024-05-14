package com.restapi;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

import com.google.gson.Gson;

public class Main {
    public static void main(String[] args) throws Exception {

        Transcript transcript = new Transcript();
        transcript.setAudio_url("https://bit.ly/3yxKEIY");
        Gson gson = new Gson();
        String jsonRequest = gson.toJson(transcript);

        System.out.println(jsonRequest);

        // post request
        HttpRequest postRequest = HttpRequest.newBuilder().uri(new URI("https://api.assemblyai.com/v2/transcript"))
                .headers("Authorization", "d8aa0f7225814324a28af2f6ab7a4a7f")
                .POST(BodyPublishers.ofString(jsonRequest)).build();

        // post reponse
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> postReponse = httpClient.send(postRequest, BodyHandlers.ofString());

        postReponse.body();
        System.out.println("The post response is (line 32)" + postReponse.body() + "\n");



        // get id
        transcript = gson.fromJson(postReponse.body(), Transcript.class);
        System.out.println("The id is (line 38))" + transcript.getId() + "\n");



        // get request
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI("https://api.assemblyai.com/v2/transcript/" + transcript.getId()))
                .header("Authorization", "d8aa0f7225814324a28af2f6ab7a4a7f")
                .build();

        // repeat until it is done
        while (true) {
            // get response
            HttpResponse<String> getReponse = httpClient.send(getRequest, BodyHandlers.ofString());
            transcript = gson.fromJson(getReponse.body(), Transcript.class);

            System.out.println("The status is (line 54)" + transcript.getStatus());

            if("completed".equals(transcript.getStatus()) || "error".equals(transcript.getStatus())){
                break;
            }

            Thread.sleep(1000);

        }

        System.out.println("Transcription completed (line 64)");
        System.out.println("The text is (line 65) " +transcript.getText());

    }
}