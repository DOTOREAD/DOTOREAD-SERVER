package likelion.dotoread.domain.enums;
import java.util.Arrays;

public enum SortType {
    ASC,
    DESC;

    public static SortType from(String direction) {
        return Arrays.stream(SortType.values())
                .filter(d -> d.name().equalsIgnoreCase(direction))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 정렬 방향입니다."));
    }
}