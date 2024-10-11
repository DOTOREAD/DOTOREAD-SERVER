package likelion.dotoread.controller;

import likelion.dotoread.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CrawlingController {

    @Autowired
    private CrawlerService blogCrawlerService;

    @GetMapping("/crawl")
    public List<String> crawlNaverBlogs(@RequestParam String url) {
        return blogCrawlerService.crawl(url);
    }
}