package com.rmuti.project.dogfeeder.connections;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Sitthiphong on 2/12/2017 AD.
 */

public class UrlConnectionHelper {

    private HttpURLConnection urlConnection;

    public String get(String url) {
        StringBuilder result = new StringBuilder();

        try {
            URL urlGet = new URL(url);
            urlConnection = (HttpURLConnection) urlGet.openConnection();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            in.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return result.toString();
    }
}
