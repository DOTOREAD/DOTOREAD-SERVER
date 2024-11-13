package likelion.dotoread.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import likelion.dotoread.api.code.status.ErrorStatus;
import likelion.dotoread.api.exception.GeneralException;
import likelion.dotoread.domain.Bookmark;
import likelion.dotoread.domain.Folder;
import likelion.dotoread.domain.User;
import likelion.dotoread.domain.mapping.UserMission;
import likelion.dotoread.enums.SortType;
import likelion.dotoread.repository.BookmarkRepository;
import likelion.dotoread.repository.FolderRepository;
import likelion.dotoread.repository.UserMissionRepository;
import likelion.dotoread.repository.UserRepository;
import likelion.dotoread.request.SaveBookmarkRequest;
import likelion.dotoread.response.BookmarkDetailResponse;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;
    private final UserService userService;
    private final UserMissionRepository userMissionRepository;
    private final MissionService missionService;
    String flaskUrl = "http://3.38.2.223:5001"; //TODO:환경 파일에 넣어놓기

    public BookmarkService(BookmarkRepository bookmarkRepository, UserRepository userRepository, FolderRepository folderRepository, UserService userService, UserMissionRepository userMissionRepository,
                           MissionService missionService) {
        this.bookmarkRepository = bookmarkRepository;
        this.userRepository = userRepository;
        this.folderRepository = folderRepository;
        this.userService = userService;
        this.userMissionRepository = userMissionRepository;
        this.missionService = missionService;
    }

    public String crawlTitle(String url){
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("url", url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    flaskUrl+"/title", HttpMethod.POST, entity, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            return rootNode.path("title").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String crawlImage(String url){
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("url", url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    flaskUrl+"/images", HttpMethod.POST, entity, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            return rootNode.path("image_url").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteBookmark(Long bookmarkId) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._BOOKMARK_NOT_FOUND));
        bookmarkRepository.delete(bookmark);
    }

    public Long saveBookmark(HttpServletRequest http, SaveBookmarkRequest request){
        User user = userService.findUserByHttpServletRequest(http);

        String title = crawlTitle(request.url());
        String img = crawlImage(request.url());

        Bookmark bookmark = Bookmark.builder()
                .url(request.url())
                .title(title)
                .img(img)
                .user(user)
                .build();
        Bookmark savedBookmark = bookmarkRepository.save(bookmark);
        UserMission userMission = userMissionRepository.findByUserAndMissionId(user, 2L);
        userMission.setCurrent();
        userMissionRepository.save(userMission);
        missionService.missionUpdate(userMission);
        return savedBookmark.getId();
    }

    public BookmarkDetailResponse getBookmarkDetail(HttpServletRequest http, Long bookmarkId) {
        User user = userService.findUserByHttpServletRequest(http);
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._BOOKMARK_NOT_FOUND));
        if(bookmark.getIsVisited() == false && bookmark.getCreatedAt().plusDays(7).isBefore(LocalDateTime.now())) {
            UserMission userMission = userMissionRepository.findByUserAndMissionId(user, 1L);
            userMission.setCurrent();
            userMissionRepository.save(userMission);
            missionService.missionUpdate(userMission);
        }
        else if(bookmark.getVisitedAt().plusDays(7).isBefore(LocalDateTime.now())) {
            UserMission userMission = userMissionRepository.findByUserAndMissionId(user, 1L);
            userMission.setCurrent();
            userMissionRepository.save(userMission);
            missionService.missionUpdate(userMission);
        }

        bookmark.setVisitedAt(LocalDateTime.now());
        bookmark.setIsVisited(true);

        bookmarkRepository.save(bookmark);

        return BookmarkDetailResponse.of(
                bookmark.getId(),
                bookmark.getTitle(),
                bookmark.getUrl(),
                bookmark.getImg(),
                bookmark.getCreatedAt(),
                bookmark.getFolder()
        );
    }

    public List<BookmarkDetailResponse> getAllBookmarks(HttpServletRequest http, SortType sortType) {
        User user = userService.findUserByHttpServletRequest(http);

        Sort sort = Sort.by(Sort.Order.asc("isVisited"),
                sortType == SortType.ASC ? Sort.Order.asc("createdAt") : Sort.Order.desc("createdAt"));
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUser(user, sort);
        return BookmarkDetailResponse.from(bookmarks);
    }

    public List<BookmarkDetailResponse> getBookmarksInFolder(HttpServletRequest http, Long folderId, SortType sortType) {
        User user = userService.findUserByHttpServletRequest(http);

        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._FOLDER_NOT_FOUND));

        Sort sort = Sort.by(Sort.Order.asc("isVisited"),
                sortType == SortType.ASC ? Sort.Order.asc("createdAt") : Sort.Order.desc("createdAt"));
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUserAndFolderId(user, folderId, sort);
        return BookmarkDetailResponse.from(bookmarks);
    }

    public List<BookmarkDetailResponse> getUncategorizedBookmarks(HttpServletRequest http, SortType sortType) {
        User user = userService.findUserByHttpServletRequest(http);

        Sort sort = Sort.by(Sort.Order.asc("isVisited"),
                sortType == SortType.ASC ? Sort.Order.asc("createdAt") : Sort.Order.desc("createdAt"));
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUserAndFolderIsNull(user, sort);
        return BookmarkDetailResponse.from(bookmarks);
    }
}