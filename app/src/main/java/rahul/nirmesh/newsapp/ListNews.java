package rahul.nirmesh.newsapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.florent37.diagonallayout.DiagonalLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

import dmax.dialog.SpotsDialog;
import rahul.nirmesh.newsapp.Interface.NewsService;
import rahul.nirmesh.newsapp.adapter.ListNewsAdapter;
import rahul.nirmesh.newsapp.common.Common;
import rahul.nirmesh.newsapp.model.Article;
import rahul.nirmesh.newsapp.model.News;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListNews extends AppCompatActivity {

    KenBurnsView kbv;
    DiagonalLayout diagonalLayout;
    AlertDialog dialog;
    NewsService mService;
    TextView top_author, top_title;
    SwipeRefreshLayout swipeRefreshLayout;

    String source = "", sortBy = "", webHotURL = "";

    ListNewsAdapter adapter;
    RecyclerView lstNews;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_news);

        // Service
        mService = Common.getNewsService();

        dialog = new SpotsDialog(this);

        // Views
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNews(source, true);
            }
        });

        diagonalLayout = findViewById(R.id.diagonalLayout);
        diagonalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailIntent = new Intent(getBaseContext(), DetailArticle.class);
                detailIntent.putExtra("webURL", webHotURL);
                startActivity(detailIntent);
            }
        });

        kbv = findViewById(R.id.top_image);
        top_author = findViewById(R.id.top_author);
        top_title = findViewById(R.id.top_title);

        lstNews = findViewById(R.id.lstNews);
        lstNews.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        lstNews.setLayoutManager(layoutManager);

        // Intent
        if (getIntent() != null) {
            source = getIntent().getStringExtra("source");
            sortBy = getIntent().getStringExtra("sortBy");

            if (!source.isEmpty() && !sortBy.isEmpty()) {
                loadNews(source, false);
            }
        }
    }

    private void loadNews(String source, boolean isRefreshed) {
        if (!isRefreshed) {
            dialog.show();

            mService.getNewestArticles(Common.getAPIUrl(source, sortBy, Common.API_KEY))
                    .enqueue(new Callback<News>() {
                        @Override
                        public void onResponse(Call<News> call, Response<News> response) {
                            dialog.dismiss();

                            // Get First Article
                            Picasso.with(getBaseContext())
                                    .load(response.body().getArticles().get(0).getUrlToImage())
                                    .into(kbv);

                            top_title.setText(response.body().getArticles().get(0).getTitle());
                            top_author.setText(response.body().getArticles().get(0).getAuthor());

                            webHotURL = response.body().getArticles().get(0).getUrl();

                            // Load Remaining Articles
                            List<Article> removeFirstItem = response.body().getArticles();

                            // Because we are already loading first items to show on
                            // Diagonal Layout so we need to remove it
                            removeFirstItem.remove(0);

                            adapter = new ListNewsAdapter(removeFirstItem, getBaseContext());
                            adapter.notifyDataSetChanged();
                            lstNews.setAdapter(adapter);
                        }

                        @Override
                        public void onFailure(Call<News> call, Throwable t) {
                            Toast.makeText(ListNews.this, "Sorry! Cannot Load More.", Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            dialog.show();

            mService.getNewestArticles(Common.getAPIUrl(source, sortBy, Common.API_KEY))
                    .enqueue(new Callback<News>() {
                        @Override
                        public void onResponse(Call<News> call, Response<News> response) {
                            dialog.dismiss();

                            // Get First Article
                            Picasso.with(getBaseContext())
                                    .load(response.body().getArticles().get(0).getUrlToImage())
                                    .into(kbv);

                            top_title.setText(response.body().getArticles().get(0).getTitle());
                            top_author.setText(response.body().getArticles().get(0).getAuthor());

                            webHotURL = response.body().getArticles().get(0).getUrl();

                            // Load Remaining Articles
                            List<Article> removeFirstItem = response.body().getArticles();
                            removeFirstItem.remove(0);

                            adapter = new ListNewsAdapter(removeFirstItem, getBaseContext());
                            adapter.notifyDataSetChanged();
                            lstNews.setAdapter(adapter);
                        }

                        @Override
                        public void onFailure(Call<News> call, Throwable t) {
                            Toast.makeText(ListNews.this, "Sorry! Something weird happen.", Toast.LENGTH_SHORT).show();
                        }
                    });

            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
