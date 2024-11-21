package likelion.dotoread.web.dto.CollectionDto;

import lombok.Getter;

import java.util.List;

public class CollectionRequestDTO {
    @Getter
    public static class CollectionDTO {
        String title;
        String memo;
        List<Long> bookmarkIds;
    }
}
