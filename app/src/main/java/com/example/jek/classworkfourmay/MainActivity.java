package com.example.jek.classworkfourmay;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.jek.classworkfourmay.model.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private User user = null;
    private FrameLayout f1;
    private TextView textView;
    private View container;
    private List<Article> articleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        f1 = (FrameLayout) findViewById(R.id.progress_bar);
        textView = (TextView) findViewById(R.id.myText);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*MyAsyncTask myAsyncTask = new MyAsyncTask("Hello", this);
        myAsyncTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, 1);*/

       /* try {
            MyAsyncTask myAsyncTask = new MyAsyncTask("Hello", this);
            myAsyncTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, 1);
            user = myAsyncTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/
        /*Log.d(*//*TAG*//* "Fsdsda", "resume " + Thread.currentThread().getName());*/
        hideProgressBar();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        Request.Builder builder = new Request.Builder();
        builder.url("https://newsapi.org/v1/articles?source=the-next-web&sortBy=latest&apiKey=219f6cc7fb864a3499614b11664ca40a");
        Request request = builder.build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "call: " + call.toString() + "error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
               /* Log.d(TAG, "code: " + response.code());
                Log.d(TAG, "response: " + response.body().string());*/
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    Log.d(TAG, "source: " + jsonObject.get("source"));
                    JSONArray articles = jsonObject.getJSONArray("articles");
                    for(int i = 0; i < articles.length(); i++){
                        JSONObject jsonObject1 = new JSONObject(articles.get(i).toString());
                        Article article = new Article();
                        article.setAuthor(jsonObject1.get("author").toString());
                        article.setDescription(jsonObject1.get("description").toString());
                        article.setTitle(jsonObject1.get("title").toString());
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


    public void showProgressBar() {
        f1.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        f1.setVisibility(View.GONE);
    }

    public void onUserRecieved(final User user) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String current = textView.getText().toString();
                current = current + "\n" + "User name: " + user.name + "User age: " + user.age;
                textView.setText(current);
            }
        });
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
        StringBuilder stringBuilder = new StringBuilder(articleList.size()*2);
        for(Article article: articleList){
            stringBuilder.append(article.toString());
            stringBuilder.append("\n");
        }
        textView.setText(stringBuilder.toString());
    }
}