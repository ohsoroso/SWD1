package app.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WordAnalyzer {

    public static Map<String, Integer> analyzeHTML(String htmlContent) {
        Map<String, Integer> wordCountMap = new HashMap<>();

        String plainText = htmlContent.replaceAll("[^\\p{L}\\p{Nd}']+", " ");
        String[] words = plainText.split("\\s+");

        for (String word : words) {
            word = word.toLowerCase();
            wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
        }

        return wordCountMap;
    }

}
