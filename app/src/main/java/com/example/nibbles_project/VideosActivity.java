package com.example.nibbles_project;// VideosActivity.java
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nibbles_project.VideosAdapter;
import java.util.ArrayList;
import java.util.List;
public class VideosActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private VideosAdapter adapter;
    private List<String> videoUrls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);
        recyclerView = findViewById(R.id.recycler_view_videos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        videoUrls = new ArrayList<>();
        videoUrls.add("https://www.youtube.com/watch?v=DkI0tFAXHU4&pp=ygUeZGlldGFyeSBndWlkZWxpbmVzIGZvciBpbmRpYW5z"); // replace with actual links
        videoUrls.add("https://www.youtube.com/watch?v=F6RrJ9A6z64&pp=ygUeZGlldGFyeSBndWlkZWxpbmVzIGZvciBpbmRpYW5z");
        videoUrls.add("https://www.youtube.com/watch?v=TjQvhkmpDaQ&pp=ygUeZGlldGFyeSBndWlkZWxpbmVzIGZvciBpbmRpYW5z");
        adapter = new VideosAdapter(videoUrls);
        recyclerView.setAdapter(adapter);
    }
}