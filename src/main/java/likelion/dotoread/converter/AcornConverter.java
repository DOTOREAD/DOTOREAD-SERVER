package likelion.dotoread.converter;

import likelion.dotoread.domain.AcornAdd;
import likelion.dotoread.domain.AcornUse;
import likelion.dotoread.domain.Store;
import likelion.dotoread.domain.User;
import likelion.dotoread.domain.mapping.UserMission;
import likelion.dotoread.web.dto.AcornDto.AcornResponseDTO;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AcornConverter {
    public static AcornAdd toAcornAdd(Integer addAcorn, UserMission userMission) {
        return AcornAdd.builder()
                .addAcorn(addAcorn)
                .addedAt(LocalDateTime.now())
                .userMission(userMission)
                .user(userMission.getUser())
                .build();
    }

    public static AcornUse toAcornUse(Integer useAcorn, Store store, User user) {
        return AcornUse.builder()
                .useAcorn(useAcorn)
                .usedAt(LocalDateTime.now())
                .store(store)
                .user(user)
                .build();
    }

    public static AcornResponseDTO.AcornAddDTO toAcornAddDTO(AcornAdd acornAdd){
        return AcornResponseDTO.AcornAddDTO.builder()
                .acorn(acornAdd.getAddAcorn())
                .content(acornAdd.getUserMission().getMission().getContent())
                .addedAt(acornAdd.getAddedAt().toLocalDate())
                .build();
    }
    public static AcornResponseDTO.AcornAddListDTO toAcornAddListDTO(Page<AcornAdd> acornAdds) {
        List<AcornResponseDTO.AcornAddDTO> acornAddDTOList = acornAdds.stream()
                .map(AcornConverter::toAcornAddDTO)
                .collect(Collectors.toList());
        return AcornResponseDTO.AcornAddListDTO.builder()
                .acornAddDTOList(acornAddDTOList)
                .isFirst(acornAdds.isFirst())
                .isLast(acornAdds.isLast())
                .listSize(acornAdds.getSize())
                .totalElements(acornAdds.getTotalElements())
                .totalPage(acornAdds.getTotalPages())
                .build();
    }

    public static AcornResponseDTO.AcornUsageDTO toAcornUsageDTO(AcornUse acornUse){
        return AcornResponseDTO.AcornUsageDTO.builder()
                .acorn(acornUse.getUseAcorn())
                .content(acornUse.getStore().getContent())
                .usedAt(acornUse.getUsedAt().toLocalDate())
                .build();
    }
    public static AcornResponseDTO.AcornUsageListDTO toAcornUsageListDTO(Page<AcornUse> acornUses) {
        List<AcornResponseDTO.AcornUsageDTO> acornUsageDTOList = acornUses.stream()
                .map(AcornConverter::toAcornUsageDTO)
                .collect(Collectors.toList());
        return AcornResponseDTO.AcornUsageListDTO.builder()
                .acornUsageDTOList(acornUsageDTOList)
                .isFirst(acornUses.isFirst())
                .isLast(acornUses.isLast())
                .listSize(acornUses.getSize())
                .totalElements(acornUses.getTotalElements())
                .totalPage(acornUses.getTotalPages())
                .build();
    }

}
