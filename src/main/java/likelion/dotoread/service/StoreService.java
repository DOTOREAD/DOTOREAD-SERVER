package likelion.dotoread.service;

import likelion.dotoread.api.code.status.ErrorStatus;
import likelion.dotoread.api.exception.handler.UserHandler;
import likelion.dotoread.converter.AcornConverter;
import likelion.dotoread.domain.AcornUse;
import likelion.dotoread.domain.Store;
import likelion.dotoread.domain.User;
import likelion.dotoread.repository.AcornUseRepository;
import likelion.dotoread.repository.StoreRepository;
import likelion.dotoread.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final AcornUseRepository acornUseRepository;
    private final UserRepository userRepository;
    public void acornUse(User user, Integer useAcorn, Long storeId) { //store- 1:wwf, 2-카라, 3-스토리지구매
        if(user.getAcornCount() < useAcorn) {
            throw new UserHandler(ErrorStatus._ACORN_LACK);
        }
        Store store = storeRepository.findById(storeId)
                .orElseThrow(()->new RuntimeException("store가 존재하지 않습니다."));
        AcornUse acornUse = AcornConverter.toAcornUse(useAcorn, store, user);
        user.useAcorn(useAcorn);
        user.addDonated(useAcorn);
        acornUseRepository.save(acornUse);
        userRepository.save(user);
    }

    public void upgradeStorage(User user, Integer useAcorn) {
        if(user.getAcornCount() < useAcorn) {
            throw new UserHandler(ErrorStatus._ACORN_LACK);
        }
        Store store = storeRepository.findById(3L).
                orElseThrow(()->new RuntimeException("store가 존재하지 않습니다."));
        AcornUse acornUse = AcornConverter.toAcornUse(useAcorn, store, user);
        user.useAcorn(useAcorn);
        user.addStorage(5*useAcorn);
        acornUseRepository.save(acornUse);
        userRepository.save(user);
    }
}
