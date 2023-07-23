package app.demo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class MainApp extends Application {

    private TextArea textArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(@SuppressWarnings("exports") Stage primaryStage) {
        primaryStage.setTitle("Word Occurrences Analyzer");

        Label label = new Label("Select HTML File:");
        Button openButton = new Button("Open File");
        textArea = new TextArea();
        Button analyzeButton = new Button("Analyze");

        openButton.setOnAction(e -> openFile(primaryStage));
        analyzeButton.setOnAction(e -> analyzeText());

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(label, openButton, textArea, analyzeButton);

        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openFile(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("HTML Files", "*.html", "*.htm"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            try {
                String htmlContent = readHTMLFile(selectedFile.getAbsolutePath(), "startID", "endID");
                textArea.setText(htmlContent);
            } catch (IOException e) {
                System.err.println("Error reading the HTML file: " + e.getMessage());
            }
        }
    }

    private String readHTMLFile(String filePath, String startElementId, String endElementId) throws IOException {
        Document document = Jsoup.parse(new File(filePath), "UTF-8");
        Element startElement = document.getElementById(startElementId);
        Element endElement = document.getElementById(endElementId);

        if (startElement == null || endElement == null) {
            throw new IllegalArgumentException("Invalid start or end element ID");
        }

        StringBuilder sb = new StringBuilder();
        boolean rangeStarted = false;

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

    private void analyzeText() {
        String htmlContent = textArea.getText();
        Map<String, Integer> wordCountMap = analyzeHTML(htmlContent);
        displayTop20Words(wordCountMap);
    }

    private Map<String, Integer> analyzeHTML(String htmlContent) {
        Map<String, Integer> wordCountMap = new HashMap<>();

        String plainText = htmlContent.replaceAll("[^\\p{L}\\p{Nd}']+", " ");
        String[] words = plainText.split("\\s+");

        for (String word : words) {
            word = word.toLowerCase();
            wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
        }

        return wordCountMap;
    }

    private void displayTop20Words(Map<String, Integer> wordCountMap) {
        try (Socket clientSocket = new Socket("localhost", 12345);
             ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream())) {

            // Send the HTML content to the server
            outputStream.writeObject(textArea.getText());

            // Receive the word count map from the server
            @SuppressWarnings("unchecked")
			Map<String, Integer> serverWordCountMap = (Map<String, Integer>) inputStream.readObject();

            // Display the top 20 words from the server's word count map
            Stage topWordsStage = new Stage();
            topWordsStage.setTitle("Top 20 Words");
            VBox topWordsVBox = new VBox(10);
            topWordsVBox.setPadding(new Insets(10));

            int count = 0;
            for (Map.Entry<String, Integer> entry : serverWordCountMap.entrySet()) {
                if (count >= 20) break;
                String word = entry.getKey();
                int frequency = entry.getValue();
                Label label = new Label(word + ": " + frequency);
                topWordsVBox.getChildren().add(label);
                count++;
            }

            Scene topWordsScene = new Scene(topWordsVBox, 400, 300);
            topWordsStage.setScene(topWordsScene);
            topWordsStage.show();

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error communicating with the server: " + e.getMessage());
        }
    }
}
