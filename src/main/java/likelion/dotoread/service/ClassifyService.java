package likelion.dotoread.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import likelion.dotoread.api.code.status.ErrorStatus;
import likelion.dotoread.api.exception.GeneralException;
import likelion.dotoread.domain.Bookmark;
import likelion.dotoread.domain.Folder;
import likelion.dotoread.domain.User;
import likelion.dotoread.repository.BookmarkRepository;
import likelion.dotoread.repository.UserRepository;
import likelion.dotoread.request.ClassifyRequest;
import likelion.dotoread.response.BookmarkDetailResponse;
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
    private final FolderService folderService;
    private final UserRepository userRepository;
    String flaskUrl = "http://3.38.2.223:5001";

    public ClassifyService(BookmarkRepository bookmarkRepository, FolderService folderService, UserRepository userRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.folderService = folderService;
        this.userRepository = userRepository;
    }

    public List<BookmarkDetailResponse> AIClassify(ClassifyRequest classifyRequest) {
        return classifyRequest.bookmarkIds().stream()
                .map(bookmarkId -> {
                    Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                            .orElseThrow(() -> new GeneralException(ErrorStatus._BOOKMARK_NOT_FOUND));
                    String topic = callAIClassify(bookmarkId);
                    if (topic != null) { // 폴더가 존재하지 않으면 생성하고 존재하면 바로 저장
                        Folder folder = checkAndCreateFolder(topic, classifyRequest.userId());

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

    private Folder checkAndCreateFolder(String topic, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));
        return folderService.findOrCreateFolder(topic, user);
    }

    public void cancleClassify(ClassifyRequest classifyRequest) {
        bookmarkRepository.removeFolderIdsByBookmarkIds(classifyRequest.getBookmarkIds());
    }

    public void deleteClassify(Long classifyId) {
        bookmarkRepository.removeFolderIdByBookmarkId(classifyId);
    }
}