package com.kyc.automation_process.controllers;

import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
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
@RequestMapping("kyc/face")
public class FaceVerificationController {
    @PostMapping(value = "/verify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> verifyFace(
            @RequestParam("selfie") MultipartFile selfie,
            @RequestParam("reference") MultipartFile reference) throws IOException {

        RestTemplate restTemplate = new RestTemplate();

        // Prepare HTTP request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("selfie", new ByteArrayResource(selfie.getBytes()) {
            @Override
            public String getFilename() {
                return selfie.getOriginalFilename();
            }
        });
        body.add("reference", new ByteArrayResource(reference.getBytes()) {
            @Override
            public String getFilename() {
                return reference.getOriginalFilename();
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        String url = "http://localhost:5001/verify-face";

        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        return ResponseEntity.ok(response.getBody());
    }
}
