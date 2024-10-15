package likelion.dotoread.web.controller;

import likelion.dotoread.api.ApiResponse;
import likelion.dotoread.api.code.status.SuccessStatus;
import likelion.dotoread.web.dto.UserDto.FolderRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class LdaController {

    @Value("${flask.server.url}")
    private String FLASK_SERVER_URL;

    //TODO : url 수정 필요
    @PostMapping("/test")
    public ApiResponse<String> lda(@RequestBody FolderRequestDTO.LdaDTO request) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(FLASK_SERVER_URL + "/lda", request, String.class);
        String result = response.getBody();
        return ApiResponse.of(SuccessStatus._KEYWORD_OK, result);
    }
}