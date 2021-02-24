package com.fooddrive.google;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

public class GoogleDriveFile {

    private final String appId = "800e18f7-3fba-4124-8e7f-338e04c241a0";
    private String accessToken = "ya29.A0AfH6SMCGtrjJd2Ei2-IPQFFMF-pnEX7ahnH-j4fXZo04hHqjz7J81TTXABU4dNbQjnggA7Wmy-OK_sTL39I6LL-oBZgYSGbuCUEnKdKscpr0Or0M8ZDj3BX42t8QXRyNCVGqiAYoLrGDAj_BOEogkjG6m0bD";

    public String uploadFile(String path, String name){
        if (appId == null) {
            System.out.println("You must initialize Authentication before calling getUserAccessToken");
            return null;
        }


        try{

            java.io.File file = new File(path);
            byte[] fileContent = Files.readAllBytes(file.toPath());
            String result = Base64.getEncoder().encodeToString(fileContent);


            String urlUp ="https://www.googleapis.com/upload/drive/v3/files";

            String boundary = "-------------oiawn4tp89n4e9p5";
            Map<Object, Object> data = new HashMap<>();
            ObjectMapper objectMapper = new ObjectMapper();
            // some form fields
            data.put("name", file.getName());
            data.put("upload_file", true);

            // file upload
            data.put("file", Paths.get(path));

            ObjectMapper objectMapper2 = new ObjectMapper();
            //creamos el cliente de conexion
            HttpClient client2 = HttpClient.newHttpClient();
            //creamos el requerimiento al cliente Http
            HttpRequest request2 = HttpRequest.newBuilder()
                    .uri(URI.create(urlUp))
                    .timeout(Duration.ofSeconds(10))
                    .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                    .header("Authorization", "Bearer "+accessToken)
                    .timeout(Duration.ofMinutes(1))
                    .POST(oMultipartData(data, boundary))
                    .build();

            HttpResponse response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());

            //JsonNode te permite navegar por el JSON como un objeto java donde se le pasan como parametros la url y la clase jsonNode
            JsonNode myJson = objectMapper2.readValue(response2.body().toString(), JsonNode.class);

            return "Ok";
        }catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
            return "ERROR API GOOGLE" + e.getMessage();
        }
    }
    public static HttpRequest.BodyPublisher oMultipartData(Map<Object, Object> data,
                                                           String boundary) throws IOException {
        var byteArrays = new ArrayList<byte[]>();
        byte[] separator = ("--" + boundary
                + "\r\nContent-Disposition: form-data; name=")
                .getBytes(StandardCharsets.UTF_8);
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            byteArrays.add(separator);

            if (entry.getValue() instanceof Path) {
                var path = (Path) entry.getValue();
                String mimeType = Files.probeContentType(path);
                byteArrays.add(("\"" + entry.getKey() + "\"; filename=\""
                        + path.getFileName() + "\"\r\nContent-Type: " + mimeType
                        + "\r\n\r\n").getBytes(StandardCharsets.UTF_8));
                byteArrays.add(Files.readAllBytes(path));
                byteArrays.add("\r\n".getBytes(StandardCharsets.UTF_8));
            } else {
                byteArrays.add(
                        ("\"" + entry.getKey() + "\"\r\n\r\n" + entry.getValue()
                                + "\r\n").getBytes(StandardCharsets.UTF_8));
            }
        }
        byteArrays
                .add(("--" + boundary + "--").getBytes(StandardCharsets.UTF_8));
        return HttpRequest.BodyPublishers.ofByteArrays(byteArrays);
    }

    public String uploadFile2(String path, String name){
        if (appId == null) {
            System.out.println("You must initialize Authentication before calling getUserAccessToken");
            return null;
        }

        try{

            java.io.File file = new File(path);

            String urlUp ="https://www.googleapis.com/upload/drive/v3/files";

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, "{\"name\":\""+file.getName()+"\"}");

            OkHttpClient client = new OkHttpClient();

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addPart(body)
                    .addFormDataPart("file", file.getName(),
                            RequestBody.create(MediaType.parse("application/octet-stream"),file))
                    .build();

            Request request = new Request.Builder()
                    .url(urlUp)
                    .addHeader("Authorization", "Bearer "+accessToken)
                    .post(requestBody)
                    .build();

            Call call = client.newCall(request);
            Response response = call.execute();

            return "Ok";
        }catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
            return "ERROR API GOOGLE" + e.getMessage();
        }
    }

    public String downloadFile2(){
        if (appId == null) {
            System.out.println("You must initialize Authentication before calling getUserAccessToken");
            return null;
        }

        try{

            String urlUp ="https://www.googleapis.com/drive/v2/files/1xTbKUgDnys-elIDjEZP2XG88KJnqXov0?alt=media&source=downloadUrl";

            Path downloadDir = Path.of("/home/jonatan/Documents/FilesTest/");
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(urlUp)
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Failed to download file: " + response);
            }
            FileOutputStream fos = new FileOutputStream("/home/jonatan/Documents/FilesTest/Otro.pdf");
            fos.write(response.body().bytes());
            fos.close();
            return "Ok";
        }catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
            return "ERROR API " + e.getMessage();
        }
    }
}
