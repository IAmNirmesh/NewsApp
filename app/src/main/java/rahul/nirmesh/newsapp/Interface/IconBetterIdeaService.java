package rahul.nirmesh.newsapp.Interface;

import rahul.nirmesh.newsapp.model.IconBetterIdea;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by NIRMESH on 06-Jan-18.
 */

public interface IconBetterIdeaService {
    @GET
    Call<IconBetterIdea> getIconUrl(@Url String url);
}
