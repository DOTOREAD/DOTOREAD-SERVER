package likelion.dotoread.domain.enums;

import lombok.Generated;

public enum Rating {
    LOW("나쁨"),
    MIDDLE("보통"),
    HIGH("좋음");
    private final String viewName;
    Rating(String viewName){this.viewName = viewName;}
    @Generated
    public String getViewName(){return this.viewName;}
}
