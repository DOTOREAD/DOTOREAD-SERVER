package likelion.dotoread.service;
// /opt/homebrew/bin/chromedriver-mac-arm64/chromedriver

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CrawlerService {

    @Value("${chromedriver.path:/usr/bin/chromedriver}")
    private String chromeDriverPath;

    public WebDriver setupDriver() {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        return new ChromeDriver(options);
    }

    public List<String> crawl(String url) {
        WebDriver driver = setupDriver();
        try {
            driver.get(url);
            Thread.sleep(2000);  // 페이지 로딩 대기

            // 모든 텍스트를 크롤링
            List<WebElement> elements = driver.findElements(By.tagName("body"));  // body 전체의 텍스트 가져오기
            return elements.stream().map(WebElement::getText).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            driver.quit();
        }
    }
}