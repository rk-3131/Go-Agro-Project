package com.example.goagro;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private List<NewsArticle> newsList;
    private Context context;

    public NewsAdapter(List<NewsArticle> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news_article, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsArticle newsArticle = newsList.get(position);

        Translation_API title_translate = new Translation_API();

        title_translate.setOnTranslationCompleteListener(new Translation_API.OnTranslationCompleteListener() {
            @Override
            public void onStartTranslation() {
                // Here, you can perform initial work before translating the text, like displaying a progress bar.
            }

            @Override
            public void onCompleted(String text) {
                // Log the translated text to check if it's coming correctly.
                Log.d("Translation", "Translated Text: " + text);
                // Update the UI with the translated text.
                holder.titleTextView.setText(text);
//                holder.descriptionTextView.setText(text);
            }

            @Override
            public void onError(Exception e) {
                // Handle translation error if needed.
            }
        });

        // Translate the news article's title.
        title_translate.execute(newsArticle.getTitle(), "en", "mr");
//        translate.execute(newsArticle.getDescription(), "en", "mr");

        Translation_API description_translate = new Translation_API();
        description_translate.setOnTranslationCompleteListener(new Translation_API.OnTranslationCompleteListener() {
            @Override
            public void onStartTranslation() {

            }

            @Override
            public void onCompleted(String text) {
                holder.descriptionTextView.setText(text);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        description_translate.execute(newsArticle.getDescription(), "en", "mr");

        // Load the article image using Picasso (or any other image-loading library)
        String imageUrl = newsArticle.getImageUrl();
        Log.d("ImageURL", "URL: " + imageUrl);
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.no_image)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return newsList != null ? newsList.size() : 0; // Ensure newsList is not null
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView descriptionTextView;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.newsImage);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }
    }
}
