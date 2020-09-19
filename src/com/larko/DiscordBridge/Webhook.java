package com.larko.DiscordBridge;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Webhook {
    public static void send(String webhookUrl, String embed) throws IOException {
        URL url = new URL(webhookUrl);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");

        con.setDoOutput(true);
        String jsonInputString = "{\"embeds\": "+ embed +"}";
        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        }
    }
}