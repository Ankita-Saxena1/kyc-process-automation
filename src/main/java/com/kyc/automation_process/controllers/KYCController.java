package com.kyc.automation_process.controllers; // package declaration

import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@RestController // annotation to indicate that this class is a controller
@RequestMapping("/kyc") // annotation to map web requests onto specific handler classes and/or handler methods
@Tag(name = "KYC Controller", description = "Controller for KYC process") // annotation to provide metadata about the controller

public class KYCController {

    @GetMapping("/test") // annotation for mapping HTTP GET requests onto specific handler methods
    @Operation(summary = "Start KYC process", description = "Starts the KYC process") // annotation to provide metadata about the operation
    public String testKYC() {
        return "KYC process testing";
    }

    @PostMapping("/verify-passport")
    public ResponseEntity<String> verifyPassport(@RequestParam("file") MultipartFile file) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        // Create HTTP request with the image file
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        String url = "http://localhost:5000/extract-mrz";

        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        return ResponseEntity.ok(response.getBody());
    }
}