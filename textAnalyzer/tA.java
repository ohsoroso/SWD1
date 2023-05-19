package textAnalyzer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class tA {
    public static void main(String[] args) {
        String filePath = "textAnalyzer\\TheRaven.htm"; // Specify the path to your HTML file
        String startElementId = "startID"; // Specify the starting element ID
        String endElementId = "endID"; // Specify the ending element ID

        try {
            String htmlContent = readHTMLFile(filePath, startElementId, endElementId);
            Map<String, Integer> wordCountMap = analyzeHTML(htmlContent);
            displayWordCount(wordCountMap);
        } catch (IOException e) {
            System.err.println("Error reading the HTML file: " + e.getMessage());
        }
    }

    private static String readHTMLFile(String filePath, String startElementId, String endElementId) throws IOException {
        Document document = Jsoup.parse(new File(filePath), "UTF-8");
        Element startElement = document.getElementById(startElementId);
        Element endElement = document.getElementById(endElementId);

        if (startElement == null || endElement == null) {
            throw new IllegalArgumentException("Invalid start or end element ID");
        }

        StringBuilder sb = new StringBuilder();
        boolean rangeStarted = false;

        for (Element element : startElement.parents()) {
            if (element == endElement) {
                rangeStarted = true;
                break;
            }
        }

        for (Element element : document.getAllElements()) {
            if (element == startElement) {
                rangeStarted = true;
            }

            if (rangeStarted) {
                sb.append(element.text()).append(" ");

                if (element == endElement) {
                    break;
                }
            }
        }

        return sb.toString().trim();
    }

    private static Map<String, Integer> analyzeHTML(String htmlContent) {
        Map<String, Integer> wordCountMap = new HashMap<>();

        // Remove special characters and punctuation (except apostrophes)
        String plainText = htmlContent.replaceAll("[^\\p{L}\\p{Nd}']+", " ");

        // Split the content into words
        String[] words = plainText.split("\\s+");

        // Count the occurrences of each word
        for (String word : words) {
            word = word.toLowerCase();
            wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
        }

        return wordCountMap;
    }

    private static void displayWordCount(Map<String, Integer> wordCountMap) {
        System.out.println("Top 20 Words Used:");
        System.out.println("Word\t\tCount");
        System.out.println("---------------------");
    
        wordCountMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(20)
                .forEach(entry -> System.out.println(entry.getKey() + "\t\t" + entry.getValue()));
    }
    
}