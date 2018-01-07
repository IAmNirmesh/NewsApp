package rahul.nirmesh.newsapp.common;

import rahul.nirmesh.newsapp.Interface.IconBetterIdeaService;
import rahul.nirmesh.newsapp.Interface.NewsService;
import rahul.nirmesh.newsapp.remote.IconBetterIdeaClient;
import rahul.nirmesh.newsapp.remote.RetrofitClient;

/**
 * Created by NIRMESH on 06-Jan-18.
 */

public class Common {
    private static final String BASE_URL = "https://newsapi.org/";

    public static final String API_KEY = "44966ca54b7244fabb68902958ef037e";

    public static NewsService getNewsService() {
        return RetrofitClient.getClient(BASE_URL).create(NewsService.class);
    }

    public static IconBetterIdeaService getIconService() {
        return IconBetterIdeaClient.getClient().create(IconBetterIdeaService.class);
    }

    // https://newsapi.org/v1/articles?source=the-next-web&sortBy=latest&apiKey=44966ca54b7244fabb68902958ef037e
    public static String getAPIUrl(String source, String sortBy, String apiKEY) {
        StringBuilder apiUrl = new StringBuilder("https://newsapi.org/v1/articles?source=");
        return apiUrl.append(source)
                    .append("&sortBy=")
                    .append(sortBy)
                    .append("&apiKey=")
                    .append(apiKEY)
                    .toString();
    }
}
