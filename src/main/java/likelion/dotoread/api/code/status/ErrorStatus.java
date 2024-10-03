package likelion.dotoread.api.code.status;

import likelion.dotoread.api.code.BaseErrorCode;
import likelion.dotoread.api.code.ErrorReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러입니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    //OAUTH
    _ACCESS_NOT_FOUND(HttpStatus.NOT_FOUND, "OAUTH4001", "Access Token이 없습니다."),
    _REFRESH_NOT_FOUND(HttpStatus.NOT_FOUND, "OAUTH4002", "Refresh Token이 없습니다."),
    _ACCESS_INVALID(HttpStatus.BAD_REQUEST, "OAUTH4003", "Access Token이 유효하지 않습니다."),
    _REFRESH_INVALID(HttpStatus.BAD_REQUEST,"OAUTH4004", "Refresh Token이 유효하지 않습니다."),
    _ACCESS_EXPIRED(HttpStatus.BAD_REQUEST,"OAUTH4005", "Access Token이 만료되었습니다"),
    _REFRESH_EXPIRED(HttpStatus.BAD_REQUEST,"OAUTH4006", "Refresh Token이 만료되었습니다"),


    ;
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDto getReason() {
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDto getReasonHttpStatus() {
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
