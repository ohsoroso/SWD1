package app.demo;

import java.util.HashMap;
import java.util.Map;

public class MainAppTest {

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
