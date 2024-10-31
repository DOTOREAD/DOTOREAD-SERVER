package likelion.dotoread.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import likelion.dotoread.api.code.status.ErrorStatus;
import likelion.dotoread.api.exception.GeneralException;
import likelion.dotoread.domain.Bookmark;
import likelion.dotoread.domain.User;
import likelion.dotoread.enums.SortType;
import likelion.dotoread.repository.BookmarkRepository;
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
    String flaskUrl = "http://3.38.2.223:5001"; //TODO:환경 파일에 넣어놓기

    public BookmarkService(BookmarkRepository bookmarkRepository, UserRepository userRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.userRepository = userRepository;
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

    public Long saveBookmark(SaveBookmarkRequest request){
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

        String title = crawlTitle(request.url());
        String img = crawlImage(request.url());

        Bookmark bookmark = Bookmark.builder()
                .url(request.url())
                .title(title)
                .img(img)
                .user(user)
                .build();
        Bookmark savedBookmark = bookmarkRepository.save(bookmark);
        return savedBookmark.getId();
    }

    public BookmarkDetailResponse getBookmarkDetail(Long bookmarkId) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._BOOKMARK_NOT_FOUND));

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

    public List<BookmarkDetailResponse> getAllBookmarks(Long userId, SortType sortType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

        Sort sort = sortType == SortType.ASC ? Sort.by("createdAt").ascending() : Sort.by("createdAt").descending();
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUserId(userId, sort);
        return BookmarkDetailResponse.from(bookmarks);
    }

    public List<BookmarkDetailResponse> getUncategorizedBookmarks(Long userId, SortType sortType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

        Sort sort = sortType == SortType.ASC ? Sort.by("createdAt").ascending() : Sort.by("createdAt").descending();
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUserIdAndFolderIsNull(userId, sort);
        return BookmarkDetailResponse.from(bookmarks);
    }
}