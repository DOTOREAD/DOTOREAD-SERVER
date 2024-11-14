package likelion.dotoread.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import likelion.dotoread.api.code.status.ErrorStatus;
import likelion.dotoread.api.exception.GeneralException;
import likelion.dotoread.domain.Bookmark;
import likelion.dotoread.domain.Folder;
import likelion.dotoread.domain.User;
import likelion.dotoread.repository.BookmarkRepository;
import likelion.dotoread.repository.FolderRepository;
import likelion.dotoread.repository.UserRepository;
import likelion.dotoread.request.ClassifyRequest;
import likelion.dotoread.response.BookmarkDetailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClassifyService {

    private final BookmarkRepository bookmarkRepository;
    private final FolderRepository folderRepository;
    private final FolderService folderService;
    private final UserRepository userRepository;
    @Value("${flask.server.url}")
    String flaskUrl;
    private final UserService userService;

    public ClassifyService(BookmarkRepository bookmarkRepository, FolderRepository folderRepository, FolderService folderService, UserRepository userRepository, UserService userService) {
        this.bookmarkRepository = bookmarkRepository;
        this.folderRepository = folderRepository;
        this.folderService = folderService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public List<BookmarkDetailResponse> AIClassify(HttpServletRequest http, ClassifyRequest classifyRequest) {
        User user = userService.findUserByHttpServletRequest(http);
        return classifyRequest.bookmarkIds().stream()
                .map(bookmarkId -> {
                    Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                            .orElseThrow(() -> new GeneralException(ErrorStatus._BOOKMARK_NOT_FOUND));
                    String topic = callAIClassify(bookmarkId);
                    if (topic != null) { // 폴더가 존재하지 않으면 생성하고 존재하면 바로 저장
                        Folder folder = folderService.findOrCreateFolder(topic, user);

                        bookmark.setFolder(folder);
                        bookmarkRepository.save(bookmark);

                        return BookmarkDetailResponse.of(
                                bookmark.getId(),
                                bookmark.getTitle(),
                                bookmark.getUrl(),
                                bookmark.getImg(),
                                bookmark.getCreatedAt(),
                                folder
                        );
                    }
                    return null;
                })
                .filter(response -> response != null)
                .collect(Collectors.toList());
    }

    private String callAIClassify(Long bookmarkId) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> requestBody = new HashMap<>();
        String url = bookmarkRepository.getUrlById(bookmarkId);
        requestBody.put("url", url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    flaskUrl + "/keyword", HttpMethod.POST, entity, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            return rootNode.path("topic").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void cancelClassify(ClassifyRequest classifyRequest) {
        bookmarkRepository.removeFolderIdsByBookmarkIds(classifyRequest.getBookmarkIds());
    }

    public void deleteClassify(Long classifyId) {
        bookmarkRepository.removeFolderIdByBookmarkId(classifyId);
    }

    public void patchClassify(Long bookmarkId, Long folderId) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._BOOKMARK_NOT_FOUND));
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._FOLDER_NOT_FOUND));
        bookmarkRepository.updateFolderIdByBookmarkId(bookmarkId, folder);
    }
}