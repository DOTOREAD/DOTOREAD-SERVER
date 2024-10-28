package likelion.dotoread.service;

import likelion.dotoread.api.code.status.ErrorStatus;
import likelion.dotoread.api.exception.GeneralException;
import likelion.dotoread.domain.Folder;
import likelion.dotoread.repository.FolderRepository;
import likelion.dotoread.repository.UserRepository;
import likelion.dotoread.request.SaveFolderRequest;
import org.springframework.stereotype.Service;

@Service
public class FolderService {

    private final UserRepository userRepository;
    private final FolderRepository folderRepository;

    public FolderService(UserRepository userRepository, FolderRepository folderRepository) {
        this.userRepository = userRepository;
        this.folderRepository = folderRepository;
    }

    //후에 자동분류에서의 폴더 생성을 위한 구조 개선
    public Long saveFolder(SaveFolderRequest saveFolderRequest) {
        return saveFolder(saveFolderRequest.name());
    }

    public Long saveFolder(String folderName) {
        Folder folder = Folder.builder()
                .name(folderName)
                .build();
        Folder savedFolder = folderRepository.save(folder);
        return savedFolder.getId();
    }

    public void deleteFolder(Long folderId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._FOLDER_NOT_FOUND));
        folderRepository.delete(folder);
    }
}
