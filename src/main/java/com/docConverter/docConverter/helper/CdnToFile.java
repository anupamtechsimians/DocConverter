package com.docConverter.docConverter.helper;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
        import java.nio.file.Path;
        import java.nio.file.StandardCopyOption;

public class CdnToFile {

    public static MultipartFile convertFromCDN(String cdnLink) throws IOException {
        URL url = new URL(cdnLink);
        try (InputStream inputStream = url.openStream()) {
            // Use MockMultipartFile to create a MultipartFile instance
            return new MockMultipartFile(
                    "file",          // parameter name
                    url.getFile(),   // original file name
                    "application/octet-stream",  // content type
                    inputStream
            );
        }
    }
    public static Path getDocumentStream(String cdnDocumentUrl) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(cdnDocumentUrl))
                .GET()
                .build();

        HttpResponse<InputStream> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());

        if (httpResponse.statusCode() == 200) {
            Path path = saveToTempFile(httpResponse.body());
            return path;
        }
        else if (httpResponse.statusCode()==403){
            throw new FileNotFoundException("not Found");
        }
        else {
            throw new IOException("Failed to fetch document. HTTP status code: " + httpResponse.statusCode());
        }
    }
    private static Path saveToTempFile(InputStream inputStream) throws IOException {
        // Save the content of InputStream to a temporary file
        Path tempFilePath = Files.createTempFile("temp-document", ".docx");
        Files.copy(inputStream, tempFilePath, StandardCopyOption.REPLACE_EXISTING);
        return tempFilePath;
    }
}
