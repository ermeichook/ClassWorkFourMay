package com.example.jek.classworkfourmay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;


import com.example.jek.classworkfourmay.model.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    public static final String TAG = MainActivity.class.getSimpleName();
    private User user = null;
    private FrameLayout f1;
    private View container;
    private List<Article> articleList = new ArrayList<>();
    private Spinner spinner;
    private List<String> list = new ArrayList<>();
    private RecyclerView recyclerView;
    String urlBegin = "https://newsapi.org/v1/articles?source=";
    String source = "the-next-web";
    String urlEnd = "&sortBy=top&apiKey=12414510e7604ffa99dcd5c6fa256a92";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item);
        list.add("abc-news-au");
        list.add("ars-technica");
        list.add("bbc-news");
        list.add("bild");
        list.add("al-jazeera-english");
        arrayAdapter.addAll(list);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getArticles();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

//        hideProgressBar();

    }


    public void showProgressBar() {
        f1.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        f1.setVisibility(View.GONE);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         source = list.get(position);
        Log.d("sdcs", "source: " + source);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static class User {
        String name;
        int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

    private void setArticles(){
       // StringBuilder stringBuilder = new StringBuilder(articleList.size()*2);
       // for(Article article: articleList){
       //     stringBuilder.append(article.toString());
        //    stringBuilder.append("\n");
       // }
        RecViewAdapter recViewAdapter = new RecViewAdapter(articleList);
        recyclerView.setAdapter(recViewAdapter);

    }
    private void getArticles(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        Request.Builder builder = new Request.Builder();
        builder.url(urlBegin + source + urlEnd);
        Request request = builder.build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "call: " + call.toString() + "error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.d(TAG, "code: " + response.code());
//                Log.d(TAG, "response: " + response.body().string());
               articleList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                  //  Log.d(TAG, "source: " + jsonObject.get("source"));
                    JSONArray articles = jsonObject.getJSONArray("articles");
                    for(int i = 0; i < articles.length(); i++){
                        JSONObject jsonObject1 = new JSONObject(articles.get(i).toString());
                        Article article = new Article();
                        article.setAuthor(jsonObject1.get("author").toString());
                        article.setDescription(jsonObject1.get("description").toString());
                        article.setTitle(jsonObject1.get("title").toString());
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd 'T' HH:mm", Locale.getDefault());


                        article.setPublishedAt(jsonObject1.get("publishedAt").toString());
                        articleList.add(article);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setArticles();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}