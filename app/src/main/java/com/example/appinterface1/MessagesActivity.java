package com.example.appinterface1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.Intent;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MessagesActivity extends AppCompatActivity {

    private RssAdapter adapter;
    private List<RssItem> rssItemList = new ArrayList<>();
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_messages);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.rssRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RssAdapter(rssItemList);
        recyclerView.setAdapter(adapter);

        // Initialize ExecutorService
        executorService = Executors.newSingleThreadExecutor();

        // Fetch RSS Feed
        fetchRssFeed("https://feeds.bbci.co.uk/news/world/rss.xml");  // Replace with a valid RSS feed URL
    }

    private void fetchRssFeed(String urlString) {
        executorService.execute(() -> {
            List<RssItem> rssItems = fetchRssData(urlString);
            runOnUiThread(() -> {
                rssItemList.clear();
                rssItemList.addAll(rssItems);
                adapter.notifyDataSetChanged();
            });
        });
    }

    private List<RssItem> fetchRssData(String urlString) {
        List<RssItem> rssItems = new ArrayList<>();
        try {
            // Fetch the RSS feed using OkHttp
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(urlString).build();
            InputStream inputStream;
            try (Response response = client.newCall(request).execute()) {
                assert response.body() != null;
                inputStream = response.body().byteStream();
            }

            // Parse the RSS feed
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(inputStream, null);

            int eventType = parser.getEventType();
            RssItem currentItem = null;
            String text = "";

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("item".equals(tagName)) {
                            currentItem = new RssItem("", "", "", "");
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (currentItem != null) {
                            if ("title".equals(tagName)) {
                                currentItem = new RssItem(text, currentItem.getLink(), currentItem.getDescription(), currentItem.getPubDate());
                            } else if ("link".equals(tagName)) {
                                currentItem = new RssItem(currentItem.getTitle(), text, currentItem.getDescription(), currentItem.getPubDate());
                            } else if ("description".equals(tagName)) {
                                currentItem = new RssItem(currentItem.getTitle(), currentItem.getLink(), text, currentItem.getPubDate());
                            } else if ("pubDate".equals(tagName)) {
                                currentItem = new RssItem(currentItem.getTitle(), currentItem.getLink(), currentItem.getDescription(), text);
                            } else if ("item".equals(tagName)) {
                                rssItems.add(currentItem);
                                currentItem = null;
                            }
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rssItems;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Shutdown the executor when the activity is destroyed to avoid memory leaks
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    public void switchToHome(View v) {
        // Switch to home screen
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);

    }

    public void switchToHome(View v) {
        // Switch to home screen
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);

    }
}