package src.test.java.webcrawler;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.main.java.webcrawler.WebCrawler;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WebCrawlerTest {
    //1. initialization
    private WebCrawler webCrawler;


    @BeforeEach
    void setUp() {

        webCrawler = new WebCrawler("http://example.com", 2){
           @Override
            protected String fetchUrlContent(String url) {
                return MockURLFetcher.fetch(url);
            }

           @Override
            protected List<String> processUrlContent(String content) {
                return MockURLProcessor.process(content);
            }
        }; // Adjust max depth as needed
    }

    @AfterEach
    void tearDown() {
        // Add cleanup if necessary
    }

    @Test
    void testCrawl() {
        //2. act
        // Start crawling
        webCrawler.startCrawling();
        //3. verification/assertion
        Set<String> visitedUrls = webCrawler.getVisitedUrls();
        System.out.println("set: " + visitedUrls);
        // Assert the expected visited URLs
        assertEquals(2, visitedUrls.size()); // Adjust based on expected URLs
        assertTrue(visitedUrls.contains("http://example.com"));
        assertTrue(visitedUrls.contains("http://example.com/page1"));

    }
    @Test
    void testEmptyPage() {
        // Arrange
        WebCrawler emptyPageCrawler = new WebCrawler("http://example.com/page1", 1) {
            @Override
            protected String fetchUrlContent(String url) {
                return MockURLFetcher.fetch(url);
            }

            @Override
            protected List<String> processUrlContent(String content) {
                return MockURLProcessor.process(content);
            }
        };

        // Act
        emptyPageCrawler.startCrawling();

        // Assert
        Set<String> visitedUrls = emptyPageCrawler.getVisitedUrls();
        assertEquals(1, visitedUrls.size());
        assertTrue(visitedUrls.contains("http://example.com/page1"));
    }

    @Test
    void testMaxDepthLimit() {
        // Arrange
        WebCrawler depthLimitCrawler = new WebCrawler("http://example.com", 1) {
            @Override
            protected String fetchUrlContent(String url) {
                return MockURLFetcher.fetch(url);
            }

            @Override
            protected List<String> processUrlContent(String content) {
                return MockURLProcessor.process(content);
            }
        };

        // Act
        depthLimitCrawler.startCrawling();

        // Assert
        Set<String> visitedUrls = depthLimitCrawler.getVisitedUrls();
        assertEquals(1, visitedUrls.size());
        assertTrue(visitedUrls.contains("http://example.com"));
    }
}
