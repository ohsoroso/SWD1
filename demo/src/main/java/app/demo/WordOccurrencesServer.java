package app.demo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class WordOccurrencesServer {

    private static final int PORT = 12345; // Change this to any available port number

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                // Handle client request in a separate thread
                new Thread(() -> handleClientRequest(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClientRequest(Socket clientSocket) {
        try (ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream())) {

            // Read the HTML content sent by the client
            String htmlContent = (String) inputStream.readObject();

            // Analyze the HTML content and get the word count map
            Map<String, Integer> wordCountMap = WordAnalyzer.analyzeHTML(htmlContent);

            // Send the word count map back to the client
            outputStream.writeObject(wordCountMap);

            System.out.println("Analysis results sent to client.");

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
