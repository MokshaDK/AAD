package com.example.nibbles_project;// VideosAdapter.java
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {

    private List<String> videoUrls;

    public VideosAdapter(List<String> videoUrls) {
        this.videoUrls = videoUrls;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        String videoUrl = videoUrls.get(position);
        holder.bind(videoUrl);
    }

    @Override
    public int getItemCount() {
        return videoUrls.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {

        private WebView webView;
        private TextView videoTitle;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoTitle = itemView.findViewById(R.id.tv_video_title);
            webView = itemView.findViewById(R.id.wv_video);
        }

        public void bind(String videoUrl) {
            videoTitle.setText("Nutrition Video " + (getAdapterPosition() + 1));

            webView.setWebViewClient(new WebViewClient());
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            // Load the embedded YouTube video URL
            webView.loadUrl(videoUrl);
        }
    }
}
