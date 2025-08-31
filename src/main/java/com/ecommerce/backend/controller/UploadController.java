package com.ecommerce.backend.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Value("${imgbb.api.key}")
    private String imgbbApiKey;

    private static final String IMGBB_URL = "https://api.imgbb.com/1/upload";

    @PostMapping
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        String base64Image = Base64.getEncoder().encodeToString(file.getBytes());

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("key", imgbbApiKey);  
        body.add("image", base64Image);
        body.add("expiration", "15552000"); 

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(IMGBB_URL, request, Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
            String imageUrl = (String) data.get("url");
            return ResponseEntity.ok(imageUrl);
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Upload fehlgeschlagen");
        }
    }
}

