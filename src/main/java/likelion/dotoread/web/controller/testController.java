package likelion.dotoread.web.controller;

import likelion.dotoread.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {
    @GetMapping("/test")
    public ApiResponse test() {
        return ApiResponse.onSuccess("성공입니다.");
    }
}
