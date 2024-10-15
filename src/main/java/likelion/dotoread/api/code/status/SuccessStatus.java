package likelion.dotoread.api.code.status;

import likelion.dotoread.api.code.BaseCode;
import likelion.dotoread.api.code.ReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {
    _OK(HttpStatus.OK, "2000", "Ok"),

    //OAUTH
    _GOOGLE_LOGIN_OK(HttpStatus.OK, "OAUTH2001", "구글 소셜 로그인이 완료되었습니다."),
    _REFRESH_OK(HttpStatus.OK, "OAUTH2002", "토큰 재발급이 완료되었습니다."),
    _SIGN_UP_OK(HttpStatus.OK,"OAUTH2003", "회원가입이 완료되었습니다."),
    _LOGOUT_OK(HttpStatus.OK,"OAUTH2004","로그아웃이 완료되었습니다."),

    //폴더 (키워드 추출)
    _KEYWORD_OK(HttpStatus.OK, "FOLDER2001", "키워드 추출이 완료되었습니다."),

    ;
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDto getReason() {
        return ReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ReasonDto getReasonHttpStatus() {
        return ReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
