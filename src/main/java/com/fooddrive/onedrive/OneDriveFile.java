package com.fooddrive.onedrive;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Base64;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

public class OneDriveFile {
    private final String appId = "800e18f7-3fba-4124-8e7f-338e04c241a0";
    private final String accessToken = "EwCIA8l6BAAU6k7+XVQzkGyMv7VHB/h4cHbJYRAAAbZ3gkYqwQA9lHczgsO9VkXt0imCSV/YS5ILKR7zJiEAkcIiALOBzIi74rREHYWRl3U8pMYN1IWWv/VGEOiZ8LnzEQaWGayLVbg8TMwqvcrs3huwam12cJm9Y5exoSvXh72nIqD5Lmp+HbOiL0E7HHm0GpglXGfF5AAL6n97z3Lf9IAj/ZSrFRvh1WwJRzU3p+7AlSMvS2UoVSuHVMooT8Qz6uehsuqztcIB7cY6pRQqVsdGZTJwz3Hes2pYmTWWCXrYK14Ad02htDsHYL+C3xtW0nT9Gt0Izj2JUfziVOkF2R0jDynq3uXyeFOvol1Knc1AhsQmd/TerRkeLkn09F4DZgAACAJfj/USWDJwWAKuscIgbdYFVXhreu96/TcKy2Ai0oMr9ChcZOmz7AxIsVYXTmXveAKNS9wkSUxYROcoHFPvBqGERSfHwBPOkOHOO8ALchEfaBVTqS93DnON9MVcfpWPyvrCCuO1dNP9tihI3hvpuDK5xT6FULmfSA1gcBdRBuwTLTEWynelKbA9Bby25qDPfoAxtacwO210TOxVOFxvVq+/OUDO+qIJJnFt4bidC1DVnRANpfM7EGMAdmhF/x0KCZrt6hg5H+vurwtNrfKbpU2mboM2tGfZOKZlnOUdP6kJIyT5nKow+jP/9HnBe+wTagHkMrwijHmFWZblbxYO4WObSgaj5vu24jHugFSiV7bQfB7SlS8gr6PksHD5GnemD9gmnPR1CqkYgHPV5KV2/M0L167wFRq4LYCH1N0FqqhKWEQnlj/b91X34TXz4PsSaETD60pZpAb1WEU8RskW5QRlSr6S2GsZR3EKndrOQubMj9486hL/4a3gc8D7a8cD9yn8In4u0xt5acml8tsWHet3VMChuqJbD7Gbsgti872CH9wRRp5eW4sRTMAY9BOH7BPpHorziM1n4zc4J5PgCOtdgWf30quctrx+xVi+FEFUh2lEMWbtexDuHmhA930gMvVwCQnt9whsM8T4JtCGWr/O4D9al+ege0VlZGTytOkYhp6AsbWZGDAAcwUo7eOyKBtM4mwt5BhAjM/NsqJzW4zV5vutO7aj0oWOq2KXV/aM7ysbBm/D3NERkDEqkWIJnyz1onALAeNVkOR7gZvGla5GhSRudoYN61g2g5PK2vY2sICgAg==";

    public String uploadFile(String path, String name) {
        if (appId == null) {
            System.out.println("You must initialize Authentication before calling getUserAccessToken");
            return null;
        }

        try{

            File file = new File(path);

            String urlUp ="https://graph.microsoft.com/v1.0/me/drive/root:/" + name + ":/content";

            ObjectMapper objectMapper2 = new ObjectMapper();
            //creamos el cliente de conexion
            HttpClient client2 = HttpClient.newHttpClient();
            //creamos el requerimiento al cliente Http
            HttpRequest request2 = HttpRequest.newBuilder()
                    .uri(URI.create(urlUp))
                    .timeout(Duration.ofSeconds(10))
                    .header("Content-Type", "multipart/form-data")
                    .header("Authorization", "Bearer "+accessToken)
                    .PUT(HttpRequest.BodyPublishers.ofFile(file.toPath()))
                    .build();
            HttpResponse response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());

            //JsonNode te permite navegar por el JSON como un objeto java donde se le pasan como parametros la url y la clase jsonNode
            JsonNode myJson = objectMapper2.readValue(response2.body().toString(), JsonNode.class);

            return "Ok";
        }catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
            return "ERROR API" + e.getMessage();
        }
    }

    public String downloadFile(String path, String name){
        if (appId == null) {
            System.out.println("You must initialize Authentication before calling getUserAccessToken");
            return null;
        }

        try{

            File file = new File("");
            String urlUp ="https://graph.microsoft.com/v1.0/me/drive/root:/" + path + ":/content";

            Path downloadDir = Path.of("/home/jonatan/Documents/FilesTest/");
            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .followRedirects(Redirect.ALWAYS)
                    .build();
            //creamos el cliente de conexion
            HttpClient client2 = HttpClient.newHttpClient();
            //creamos el requerimiento al cliente Http
            HttpRequest request2 = HttpRequest.newBuilder()
                    .uri(URI.create(urlUp))
                    .header("Authorization", "Bearer "+accessToken)
                    .GET()
                    .build();
            HttpResponse<Path> response2 = client.send(request2, HttpResponse.BodyHandlers.ofFileDownload(downloadDir,CREATE,WRITE));

            Path downloadedFile = response2.body();

            return "Ok";
        }catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
            return "ERROR API " + e.getMessage();
        }
    }
}
