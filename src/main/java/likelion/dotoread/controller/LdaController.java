package likelion.dotoread.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
public class LdaController {

    @Value("${flask.server.url}")
    private String FLASK_SERVER_URL;

    @PostMapping("/test")
    public String test(@RequestBody Map<String, Object> payload) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(FLASK_SERVER_URL + "/lda", payload, String.class);
        return response.getBody();
    }
}