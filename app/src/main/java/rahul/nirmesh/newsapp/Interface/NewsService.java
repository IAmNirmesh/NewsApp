package rahul.nirmesh.newsapp.Interface;

import rahul.nirmesh.newsapp.model.News;
import rahul.nirmesh.newsapp.model.WebSite;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by NIRMESH on 06-Jan-18.
 */

public interface NewsService {
    @GET("v1/sources?language=en")
    Call<WebSite> getSources();

    @GET
    Call<News> getNewestArticles(@Url String url);
}
