package likelion.dotoread.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import likelion.dotoread.api.code.status.ErrorStatus;
import likelion.dotoread.api.exception.GeneralException;
import likelion.dotoread.domain.Folder;
import likelion.dotoread.domain.User;
import likelion.dotoread.web.dto.FolderDto.FolderDTO;
import likelion.dotoread.repository.FolderRepository;
import likelion.dotoread.repository.UserRepository;
import likelion.dotoread.web.request.SaveFolderRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FolderService {

    private final UserRepository userRepository;
    private final FolderRepository folderRepository;
    private final UserService userService;

    public FolderService(UserRepository userRepository, FolderRepository folderRepository, UserService userService) {
        this.userRepository = userRepository;
        this.folderRepository = folderRepository;
        this.userService = userService;
    }

    public List<FolderDTO> getFolders(HttpServletRequest http){
        User user = userService.findUserByHttpServletRequest(http);
        List<Folder> folders = folderRepository.findByUser(user);
        return folders.stream().map(FolderDTO::from).collect(Collectors.toList());
    }

    //후에 자동분류에서의 폴더 생성을 위한 구조 개선
    public Long saveFolder(HttpServletRequest http, SaveFolderRequest saveFolderRequest) {
        User user = userService.findUserByHttpServletRequest(http);
        return saveFolder(saveFolderRequest.name(), user);
    }

    @Transactional
    public Long saveFolder(String folderName, User user) {
        // 중복 확인
        Folder existingFolder = folderRepository.findByNameAndUser(folderName, user);
        if (existingFolder != null) {
            return existingFolder.getId();
        }
        // 존재하지 않는 경우
        Folder newFolder = Folder.builder()
                .name(folderName)
                .user(user)
                .build();

        return folderRepository.save(newFolder).getId();
    }

    public void deleteFolder(Long folderId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._FOLDER_NOT_FOUND));
        folderRepository.delete(folder);
    }

    @Transactional
    public Folder findOrCreateFolder(String topic, User user) {
        //ai classify에서 사용
        Folder folder = folderRepository.findByNameAndUser(topic, user);
        if (folder == null) {
            folder = Folder.builder()
                    .name(topic)
                    .user(user)
                    .build();
            folderRepository.save(folder);
        }
        return folder;
    }
}
