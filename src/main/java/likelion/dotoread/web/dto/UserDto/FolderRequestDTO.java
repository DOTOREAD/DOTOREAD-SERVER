package likelion.dotoread.web.dto.UserDto;

import lombok.Getter;

import java.util.List;

public class FolderRequestDTO {
    @Getter
    public static class LdaDTO {
        private String url;
        private List<String> stopwords;
    }
}
