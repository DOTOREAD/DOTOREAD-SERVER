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

    //미션
    _MISSION_GET_OK(HttpStatus.OK, "MISSON2001", "미션 조회가 완료되었습니다."),

    //상점(후원 및 스토리지 늘리기)
    _FUND_OK(HttpStatus.OK, "STORE2001", "후원이 완료되었습니다."),
    _UPGRADE_STORAGE_OK(HttpStatus.OK, "STORE2002","스토리지 구매가 완료되었습니다."),

    //유저
    _GET_OWNACORN_OK(HttpStatus.OK,"USER2001", "보유 도토리 개수와 기부 도토리 개수 조회가 완료되었습니다."),
    _GET_STORAGE_OK(HttpStatus.OK, "USER2002", "스토리지 개수 조회가 완료되었습니다."),
    _GET_READ_BOOKMARK_OK(HttpStatus.OK,"USER2003","총 북마크 개수와 읽은 북마크 개수 조회를 완료했습니다"),

    //도토리
    _GET_ACORNADD_HISTORY_OK(HttpStatus.OK,"ACORN2001", "도토리 적립 내역 조회가 완료되었습니다."),
    _GET_ACORNUSE_HISTORY_OK(HttpStatus.OK,"ACORN2002", "도토리 사용 내역 조회가 완료되었습니다."),

    //북마크
    _GET_FRESH_OK(HttpStatus.OK, "ARTICLE2001", "FRESH ARTICLE 조회가 완료되었습니다."),
    _GET_ROTTEN_OK(HttpStatus.OK, "ARTICLE2002", "ROTTEN ARTICLE 조회가 완료되었습니다."),
    _SEARCH_BOOKMARK_OK(HttpStatus.OK,"BOOKMARK2001", "북마크 검색이 완료되었습니다."),

    //컬렉션
    _COLLECTION_CREATE_OK(HttpStatus.OK, "COLLECTION2001", "새 글 작성이 완료되었습니다."),
    _GET_COLLECTION_(HttpStatus.OK, "COLLECTION2002", "글 상세조회가 완료되었습니다."),
    _GET_LIST_COLLECTION_OK(HttpStatus.OK, "COLLECTION2003", "전체 글(컬렉션) 목록 조회가 완료되었습니다."),
    _DELETE_COLLECTION_OK(HttpStatus.OK, "COLLECTION2004", "글(컬렉션) 삭제가 완료되었습니다."),




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
