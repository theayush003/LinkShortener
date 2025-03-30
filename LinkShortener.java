package Link;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Random;

public class LinkShortener {
    private Map<String, String> urlMap;  // Stores shortURL to longURL mappings
    private Map<String, String> reverseUrlMap;  // Stores longURL to shortURL mappings
    private static final String BASE_URL = "https://short.link/";
    private static final int SHORT_URL_LENGTH = 6;
    private Random random;

    public LinkShortener() {
        urlMap = new HashMap<>();
        reverseUrlMap = new HashMap<>();
        random = new Random();
    }

    // Generate a random short URL
    private String generateShortUrl() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder shortUrl = new StringBuilder();

        for (int i = 0; i < SHORT_URL_LENGTH; i++) {
            shortUrl.append(characters.charAt(random.nextInt(characters.length())));
        }

        return shortUrl.toString();
    }

    // Shorten a long URL
    public String shortenUrl(String longUrl) {
        // Check if URL already has a short version
        if (reverseUrlMap.containsKey(longUrl)) {
            return BASE_URL + reverseUrlMap.get(longUrl);
        }

        // Generate a unique short URL
        String shortUrl;
        do {
            shortUrl = generateShortUrl();
        } while (urlMap.containsKey(shortUrl));

        // Store the mapping
        urlMap.put(shortUrl, longUrl);
        reverseUrlMap.put(longUrl, shortUrl);

        return BASE_URL + shortUrl;
    }

    // Expand a short URL to its original form
    public String expandUrl(String shortUrl) throws IllegalArgumentException {
        if (!shortUrl.startsWith(BASE_URL)) {
            throw new IllegalArgumentException("Invalid short URL format");
        }

        String shortCode = shortUrl.substring(BASE_URL.length());
        String longUrl = urlMap.get(shortCode);

        if (longUrl == null) {
            throw new IllegalArgumentException("Short URL not found");
        }

        return longUrl;
    }

    // Command-line interface
    public void runCLI() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Java Link Shortener!");
        System.out.println("Commands: shorten [url], expand [short-url], exit");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            try {
                if (input.toLowerCase().startsWith("shorten ")) {
                    String url = input.substring(8).trim();
                    if (url.isEmpty()) {
                        System.out.println("Error: Please provide a URL to shorten");
                        continue;
                    }
                    String shortUrl = shortenUrl(url);
                    System.out.println("Shortened URL: " + shortUrl);
                } else if (input.toLowerCase().startsWith("expand ")) {
                    String shortUrl = input.substring(7).trim();
                    if (shortUrl.isEmpty()) {
                        System.out.println("Error: Please provide a short URL to expand");
                        continue;
                    }
                    String longUrl = expandUrl(shortUrl);
                    System.out.println("Original URL: " + longUrl);
                } else {
                    System.out.println("Invalid command. Available commands: shorten [url], expand [short-url], exit");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
        System.out.println("Thank you for using Java Link Shortener!");
    }

    public static void main(String[] args) {
        LinkShortener shortener = new LinkShortener();
        shortener.runCLI();
    }
}
