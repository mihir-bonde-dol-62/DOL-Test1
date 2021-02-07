package com.example.android.books;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    static List<Book> fetchBooks(String requestUrl)
    {
        URL url = createUrl(requestUrl);

        // str to hold the parsed JSON response
        String jsonResponse = "";

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request for the search criteria");
        }

        // Return list of books
        return QueryUtils.extractFeatures(jsonResponse);
    }

    /** Returns new URL object */
    private static URL createUrl(String stringUrl) {
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the url!");
        }

        return url;
    }

    private static List<Book> extractFeatures(String booksJSON) {
        // Exit early
        if (TextUtils.isEmpty(booksJSON))
        {
            return null;
        }

        // list of strings to hold the extracted books
        List<Book> allBooks = new ArrayList<>();

        try {
            JSONObject rawJSONResponse = new JSONObject(booksJSON);

            // array that holds the books
            JSONArray books = rawJSONResponse.getJSONArray("items");
            for (int i = 0; i < books.length(); i++) {
                // Get the current book
                JSONObject book = books.getJSONObject(i);
                JSONObject volume = book.getJSONObject("volumeInfo");
                String bookTitle = volume.getString("title");

                // Extract information on authors of the book
                // Initialize str to hold authors of the book
                StringBuilder authors = new StringBuilder();
                // Check whether the JSON results contain info on authors of the book
                if (volume.has("authors")) {
                    JSONArray jsonAuthors = volume.getJSONArray("authors");
                    // Find and store the number of authors present in the authors array
                    int numberOfAuthors = jsonAuthors.length();
                    // Set max number of authors to display
                    int maxAuthors = 3;

                    String cAuthors = "";
                    String[] allAuthors =  null;

                    int numberOfLetters = jsonAuthors.get(0).toString().length();
                    if (numberOfLetters > 40) {
                        cAuthors = jsonAuthors.toString().substring(2, numberOfLetters - 1);
                        // Split on semi-colons or commas
                        allAuthors = cAuthors.split("[;,]");
                        for (int j = 0; j < allAuthors.length && j < maxAuthors; j++) {
                            authors.append(allAuthors[j].trim()).append("\n");
                        }

                    } else {
                        for (int j = 0; j < numberOfAuthors && j < maxAuthors; j++) {
                            authors.append(jsonAuthors.getString(j)).append("\n");
                        }
                    }
                }

                // Ivariable to hold current book ratings
                float bookRating = 0f;
                if (volume.has("averageRating")) {
                    // Get the average rating of the book from the JSON response
                    bookRating = (float) volume.getDouble("averageRating");
                }

                // Get the book's sale information
                JSONObject saleInfo = book.getJSONObject("saleInfo");
                String saleability = saleInfo.getString("saleability");
                boolean isSold = saleability.equals("FOR_SALE");
                float bookPrice = 0f;
                // Extract sale price only when book is available for sale
                if (isSold) {
                    JSONObject priceInfo = saleInfo.getJSONObject("retailPrice");
                    bookPrice = (float) priceInfo.getDouble("amount");
                }

                // Add book to the list
                allBooks.add(new Book(bookTitle, authors.toString(), bookRating, bookPrice));
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the google books JSON results", e);
        }

        return allBooks;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;

        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");

            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);

            urlConnection.connect();

            // Check for successful connection
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error while connecting. Error Code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.getMessage();
            Log.e(LOG_TAG, "Problem encountered while retrieving book results");
        } finally {
            if (urlConnection != null) {
                // Disconnect the connection
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException
    {
        StringBuilder output = new StringBuilder();
        if (inputStream != null)
        {
            // Decode the bits
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));

            // Buffer the decoded characters
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = reader.readLine();

            while (line != null)
            {
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }
}
