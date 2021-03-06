package com.myroom.wesna.polititrak.utilities;

import android.net.Uri;
import android.util.Log;

import com.myroom.wesna.polititrak.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    private static final String LOG_TAG = "NETWORK_UTILS";

    /**
     * Builds the URL to get the recent bills from the API
     * @return A URL to the get-recent-bills API call
     */
    public static URL buildRecentBillsUrl() {
        String url = "https://api.propublica.org/congress/v1/115/both/bills/introduced.json";
        return buildBasicURL(url);
    }

    /**
     * Builds the URL to get the specific bill from the API
     * @param billId The bill id of the bill
     * @return A URL of the specific bill
     */
    public static URL buildSpecificBillUrl(String billId){
        String url = "https://api.propublica.org/congress/v1/115/bills/" + billId + ".json";
        return buildBasicURL(url);
    }

    /**
     * Builds the URL to get a specific member's photo
     * @param size Size of the photo
     * @param bioGuide Member's Id
     * @return A URL of member's photo
     */
    public static String buildMemberPicUrlString(String size, String bioGuide){
        if(size == null){
            size = "original";
        }

        if(bioGuide == null){
            return null;
        }

        return "https://theunitedstates.io/images/congress/" + size + "/" + bioGuide + ".jpg";
    }

    private static URL buildBasicURL(String baseUrl){
        Uri builtUri = Uri.parse(baseUrl).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        String apiKey = BuildConfig.API_KEY;

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.addRequestProperty("x-api-key", apiKey);

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                Log.d(LOG_TAG, "Scanner has input");

                return scanner.next();
            } else {
                Log.d(LOG_TAG, "Scanner does not have input");

                return null;
            }
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        finally {
            urlConnection.disconnect();
        }
    }
}
