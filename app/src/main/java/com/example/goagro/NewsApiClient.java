package com.example.goagro;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class NewsApiClient {
    private static final String BASE_URL = "https://newsapi.org/v2/";
    private static final String API_KEY = "dca40751e6404ea9b8c86a9a84e8cf30";

    private static NewsApiService newsApiService;

    public static NewsApiService getNewsApiService() {
        if (newsApiService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            newsApiService = retrofit.create(NewsApiService.class);
        }
        return newsApiService;
    }

    public static String getApiKey() {
        return API_KEY;
    }

    public interface NewsApiService {
        @GET("top-headlines")
        Call<NewsResponse> getTopHeadlines(
                @Query("country") String country,
                @Query("apiKey") String apiKey
        );
    }
}

