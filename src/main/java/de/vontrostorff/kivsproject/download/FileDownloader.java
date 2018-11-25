package de.vontrostorff.kivsproject.download;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileDownloader {
    public static BufferedReader getReaderForFile(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        return bufferedReader;
    }
}
