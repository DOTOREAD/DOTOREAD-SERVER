package likelion.dotoread.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
public class LdaController {

    private final String FLASK_SERVER_URL = "http://127.0.0.1:5001/lda";

    @PostMapping("/test")
    public String test(@RequestBody Map<String, Object> payload) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(FLASK_SERVER_URL, payload, String.class);
        return response.getBody();
    }
}