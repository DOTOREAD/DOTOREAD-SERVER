package likelion.dotoread.converter;

import likelion.dotoread.domain.AcornAdd;
import likelion.dotoread.domain.mapping.UserMission;

import java.time.LocalDateTime;

public class AcornConverter {
    public static AcornAdd toAcornAdd(Integer addAcorn, UserMission userMission) {
        return AcornAdd.builder()
                .addAcorn(addAcorn)
                .addedAt(LocalDateTime.now())
                .userMission(userMission)
                .build();
    }
}
