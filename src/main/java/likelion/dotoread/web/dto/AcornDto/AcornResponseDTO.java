package likelion.dotoread.web.dto.AcornDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class AcornResponseDTO {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AcornUsageListDTO {
        List<AcornUsageDTO> acornUsageDTOList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;

    }
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AcornAddListDTO {
        List<AcornAddDTO> acornAddDTOList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;

    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AcornUsageDTO {
        Integer acorn;
        String content;
        LocalDate usedAt;

    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AcornAddDTO {
        Integer acorn;
        String content;
        LocalDate addedAt;
    }
}
