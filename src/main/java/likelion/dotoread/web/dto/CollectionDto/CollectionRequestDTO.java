package likelion.dotoread.web.dto.CollectionDto;

import likelion.dotoread.domain.Collection;
import lombok.Getter;

import java.util.List;

public class CollectionRequestDTO {
    @Getter
    public static class CollectionDTO {
        String title;
        String memo;
        List<Long> bookmarkIds;
    }

    @Getter
    public static class CollectionCreateDTO {
        String title;
        String memo;
    }

    @Getter
    public static class CollectionTempDTO {
        List<Long> bookmarkIds;
    }
}
