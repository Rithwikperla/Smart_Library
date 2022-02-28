package com.example.smartlibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class Details extends AppCompatActivity {
    String bookName;
    private RequestQueue mRequestQueue;
    private ArrayList<BookInfo> bookInfoArrayList;
    private ProgressBar progressBar;
    private BookAdapter bookadapter;
    private RecyclerView books_list;
    private BookAdapter adapter;
    private LinearLayout rvlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        bookName = intent.getStringExtra("key");
        progressBar = findViewById(R.id.progressBar);
        books_list = findViewById(R.id.Detailslist);
        rvlayout = findViewById(R.id.rvlayout);
        bookInfoArrayList = new ArrayList<>();
        adapter = new BookAdapter(bookInfoArrayList, Details.this);
        books_list.setAdapter(adapter);
        getBooksInfo(bookName);
    }

    private void getBooksInfo(String book) {

        mRequestQueue = Volley.newRequestQueue(Details.this);
        mRequestQueue.getCache().clear();
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + book + "&key=key";
        RequestQueue queue = Volley.newRequestQueue(Details.this);
        JsonObjectRequest booksObjrequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                rvlayout.setVisibility(View.VISIBLE);
                bookInfoArrayList.clear();
                try {
                    JSONArray itemsArray = response.getJSONArray("items");
                    for (int i = 0; i < itemsArray.length(); i++) {
                        JSONObject itemsObj = itemsArray.getJSONObject(i);
                        JSONObject volumeObj = itemsObj.getJSONObject("volumeInfo");
                        String title = volumeObj.getString("title");
                        String subtitle = volumeObj.getString("subtitle");
                        JSONArray authorsArray = volumeObj.getJSONArray("authors");
                        String publisher = volumeObj.getString("publisher");
                        String publishedDate = volumeObj.getString("publishedDate");
                        String description = volumeObj.getString("description");
                        int pageCount = volumeObj.getInt("pageCount");
                        JSONObject imageLinks = volumeObj.getJSONObject("imageLinks");
                        String thumbnail = imageLinks.getString("thumbnail");
                        String previewLink = volumeObj.getString("previewLink");
                        String infoLink = volumeObj.getString("infoLink");
                        JSONObject saleInfoObj = itemsObj.getJSONObject("saleInfo");
                        String buyLink = saleInfoObj.getString("buyLink");
                        ArrayList<String> authorsArrayList = new ArrayList<>();
                        if (authorsArray.length() != 0) {
                            for (int j = 0; j < authorsArray.length(); j++) {
                                authorsArrayList.add(authorsArray.getString(i));
                            }
                        }
                        BookInfo bookInfo = new BookInfo(title, subtitle, authorsArrayList, publisher, publishedDate, description, pageCount, thumbnail, previewLink, infoLink, buyLink);
                        bookInfoArrayList.add(bookInfo);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Details.this, "No Data Found" + e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Details.this, "Error found is " + error, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(booksObjrequest);
    }
}
