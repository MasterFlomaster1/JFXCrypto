package dev.masterflomaster1.jfxc.crypto.passwords;

import dev.masterflomaster1.jfxc.crypto.UnkeyedCryptoHash;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HexFormat;
import java.util.Optional;

public final class HaveIBeenPwnedApiClient {

    private static final String RANGE_URL = "https://api.pwnedpasswords.com/range/";

    private HaveIBeenPwnedApiClient() { }

    public static Optional<Integer> passwordRange(byte[] password) throws IOException, InterruptedException {
        var hashed = HexFormat.of().formatHex(UnkeyedCryptoHash.hash("SHA-1", password)).toUpperCase();

        String section1 = hashed.substring(0, 5);
        String section2 = hashed.substring(5);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(RANGE_URL + section1))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String content = response.body();
        String[] hashes = content.split("\n");

        for (String line : hashes) {
            String[] parts = line.split(":");

            if (parts[0].equals(section2)) {
                return Optional.of(Integer.parseInt(parts[1].trim()));
            }
        }

        return Optional.empty();
    }

}
