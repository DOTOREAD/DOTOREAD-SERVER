package likelion.dotoread.service;

import likelion.dotoread.api.code.status.ErrorStatus;
import likelion.dotoread.api.exception.GeneralException;
import likelion.dotoread.domain.Bookmark;
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

    public Long saveFolder(SaveFolderRequest saveFolderRequest){
        Folder folder = Folder.builder()
                .name(saveFolderRequest.name())
                .build();
        Folder savedFolder= folderRepository.save(folder);
        return savedFolder.getId();
    }
}
