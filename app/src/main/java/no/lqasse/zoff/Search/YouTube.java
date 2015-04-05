package no.lqasse.zoff.Search;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import no.lqasse.zoff.Models.SearchResult;
import no.lqasse.zoff.Server.Server;

/**
 * Created by lassedrevland on 26.03.15.
 */
public class YouTube {
    private Handler handler = new Handler();
    private Runnable delaySearch;
    private static ArrayList<SearchResult> searchResults = new ArrayList<>();
    private static HashMap<String,SearchResult> searchResultHashMap = new HashMap<>();
    private Context context;

    public static ArrayList<SearchResult> getSearchResults() {
        return searchResults;
    }

    public static void search(Context context,String query,Boolean allsongs, Boolean longsongs){
        YouTubeServer.search(context,query,allsongs,longsongs);

    }

    public static  void firstPageReceived(Context context, ArrayList<SearchResult> results, String nextPageToken){

        searchResultHashMap.clear();
        searchResults.clear();



        pageReceived(context, results, nextPageToken);




    }
    public static void pageReceived(Context context, ArrayList<SearchResult> results, String nextPageToken){
        //NEXT_PAGE_TOKEN = nextPageToken;
        searchResults.addAll(results);

        for (SearchResult r : results){
            searchResultHashMap.put(r.getVideoID(), r);
        }



        ((YouTubeListener)context).notifyDatasetChanged();
        YouTubeServer.getDetails(context,results);

    }

    public static void detailsReceived(Context context, ArrayList<String[]> details){

        for (String[] s : details){
            SearchResult result = searchResultHashMap.get(s[0]);
            if (result != null ){
                result.setDuration(s[1]);
                result.setViews(s[2]);
            } else {
                Log.d("YouTube", "Could not find searchresult");
            }

        }

        ((YouTubeListener)context).notifyDatasetChanged();


    }


    public static void addVideo(int index) {


        String videoID = searchResults.get(index).getVideoID();
        String videoTitle = searchResults.get(index).getTitle();
        try {
            videoTitle = URLEncoder.encode(videoTitle, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Server.add(videoID, videoTitle);

    }

}
