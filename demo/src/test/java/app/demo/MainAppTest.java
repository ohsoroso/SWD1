package app.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class MainAppTest {

    @Test
    public void testAnalyzeHTML() {
        MainAppTest mainAppTest = new MainAppTest();

        String htmlContent = "<html><body>This is a sample HTML content.</body></html>";
        Map<String, Integer> wordCountMap = mainAppTest.analyzeHTML(htmlContent);

        // Assert the expected word count
        Assertions.assertEquals(6, wordCountMap.get("this"));
        Assertions.assertEquals(2, wordCountMap.get("is"));
        Assertions.assertEquals(1, wordCountMap.get("a"));
        Assertions.assertEquals(1, wordCountMap.get("sample"));
        Assertions.assertEquals(1, wordCountMap.get("html"));
        Assertions.assertEquals(1, wordCountMap.get("content"));
    }

    public Map<String, Integer> analyzeHTML(String htmlContent) {
        // Remove HTML tags and keep only alphanumeric characters and spaces
        String cleanText = htmlContent.replaceAll("\\<.*?\\>", "").replaceAll("[^a-zA-Z0-9\\s]", "");

        // Split the text into words
        String[] words = cleanText.split("\\s+");

        // Count the occurrences of each word
        Map<String, Integer> wordCountMap = new HashMap<>();
        for (String word : words) {
            word = word.toLowerCase();
            int count = wordCountMap.getOrDefault(word, 0);
            wordCountMap.put(word, count + 1);
        }

        return wordCountMap;
    }
}
