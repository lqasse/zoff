package no.lqasse.zoff.Search;

import android.text.Html;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import no.lqasse.zoff.Models.SearchResult;

/**
 * Created by lassedrevland on 24.03.15.
 */
public class YouTubeJSONTranslator {

    public static String toNextPageToken(String JSONString){
        JSONObject json;
        String nextPageToken ="";
        try {
            json = new JSONObject(JSONString);


            if (json.has("nextPageToken")) {
                nextPageToken = json.getString("nextPageToken");

            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        return nextPageToken;
    }

    public static ArrayList<SearchResult> toSearchResults(String JSONString){
        JSONObject json;
        ArrayList<SearchResult> searchResults = new ArrayList<>();
        String nextPageToken = "";
        try {
            json = new JSONObject(JSONString);


            if (json.has("nextPageToken")){
                nextPageToken = json.getString("nextPageToken");

            }

            JSONArray items = json.getJSONArray("items");

            for (int i = 0; i < items.length() - 1; i++) {

                String title;
                String channelTitle;
                String description;
                String videoID;
                String publishedAt;
                String thumbDefault;
                String thumbMedium;
                String thumbHigh;

                json = items.getJSONObject(i);

                JSONObject idObject = json.getJSONObject("id");

                videoID = idObject.getString("videoId");
                //ids.add(videoID);


                json = json.getJSONObject("snippet");
                publishedAt = json.getString("publishedAt");
                title = json.getString("title");
                channelTitle = json.getString("channelTitle");

                //Decode string from html
                title = Html.fromHtml(title).toString();

                description = json.getString("description");

                JSONObject thumbnailsObject = json.getJSONObject("thumbnails");
                JSONObject thumbDefaultObject = thumbnailsObject.getJSONObject("default");
                JSONObject thumbMedObject = thumbnailsObject.getJSONObject("medium");
                JSONObject thumbHighObject = thumbnailsObject.getJSONObject("high");

                thumbDefault = thumbDefaultObject.getString("url");
                thumbMedium = thumbMedObject.getString("url");
                thumbHigh = thumbHighObject.getString("url");



                searchResults.add(
                        new SearchResult(title, channelTitle,description, publishedAt,videoID, thumbDefault, thumbMedium, thumbHigh)
                );
            }




        }catch (JSONException e){
            e.printStackTrace();
        }

        return searchResults;

    }

    public static ArrayList<String[]> toDetails(String JSONString){
        ArrayList<String[]> detailsArray = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(JSONString);
            JSONArray items = json.getJSONArray("items");
            String duration = "";
            String views = "";
            String id = "";

            for (int i = 0; i < items.length(); i++) {
                JSONObject details = items.getJSONObject(i).getJSONObject("contentDetails");
                JSONObject statistics = items.getJSONObject(i).getJSONObject("statistics");

                id = items.getJSONObject(i).getString("id");
                duration = details.getString("duration");
                views = statistics.getString("viewCount");

                String[] entry = {id,duration,views};
                detailsArray.add(entry);


            }



        } catch (Exception e) {
            e.printStackTrace();
        }

        return detailsArray;
    }
}
