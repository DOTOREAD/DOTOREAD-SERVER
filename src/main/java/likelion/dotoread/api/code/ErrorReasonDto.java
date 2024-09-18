package likelion.dotoread.api.code;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorReasonDto {
    private String code;
    private String message;
    private HttpStatus httpStatus;
    private Boolean isSuccess;
}

