package com.example.goagro;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsActivity extends AppCompatActivity {
    private RecyclerView recycler;
    private NewsAdapter newsAdapter;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        recycler = findViewById(R.id.recyclerView);
        progress = findViewById(R.id.progressBar);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        newsAdapter = new NewsAdapter(null, this);
        recycler.setAdapter(newsAdapter);

        // Fetch and display news articles
        fetchNewsArticles();
    }

    private void fetchNewsArticles() {
        progress.setVisibility(View.VISIBLE);

        // Create a Retrofit instance
        NewsApiClient.NewsApiService newsApiService = NewsApiClient.getNewsApiService();

        // Make a network request to get top headlines
        Call<NewsResponse> call = newsApiService.getTopHeadlines("in", NewsApiClient.getApiKey());

//        Call<NewsResponse> call = newsApiService.getTopHeadlines("us", NewsApiClient.API_KEY);
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    List<NewsArticle> newsArticles = response.body().getArticles();
                    newsAdapter = new NewsAdapter(newsArticles, NewsActivity.this);
                    recycler.setAdapter(newsAdapter);
                }
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });
    }
}