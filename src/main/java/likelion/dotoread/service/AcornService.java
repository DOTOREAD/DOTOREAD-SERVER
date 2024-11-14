package likelion.dotoread.service;

import likelion.dotoread.converter.AcornConverter;
import likelion.dotoread.domain.AcornAdd;
import likelion.dotoread.domain.AcornUse;
import likelion.dotoread.domain.User;
import likelion.dotoread.repository.AcornAddRepository;
import likelion.dotoread.repository.AcornUseRepository;
import likelion.dotoread.web.dto.AcornDto.AcornResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AcornService {
    private final AcornAddRepository acornAddRepository;
    private final AcornUseRepository acornUseRepository;
    public AcornResponseDTO.AcornAddListDTO getAcornAddHistory(User user, Integer page) {
        PageRequest pageRequest = PageRequest.of(page-1, 10);
        Page<AcornAdd> acornAddList = acornAddRepository.findAllByUser(user, pageRequest);
        return AcornConverter.toAcornAddListDTO(acornAddList);
    }
    public AcornResponseDTO.AcornUsageListDTO getAcornUsageHistory(User user, Integer page) {
        PageRequest pageRequest = PageRequest.of(page-1, 10);
        Page<AcornUse> acornUseList = acornUseRepository.findAllByUser(user, pageRequest);
        return AcornConverter.toAcornUsageListDTO(acornUseList);
    }
}
