package com.example.imagesearch;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GestureDetectorCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imagesearch.api.SerpApiSearchException;
import com.example.imagesearch.pojos.ImageResult;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Lifecycle lifecycle;

    static RecyclerView imageRecycler;
    static ImageView swapImage;
    WebView imageWeb;
    static Button searchBut;

    private static List<ImageResult> imageResultList = new ArrayList<>();
    private static List<ImageResult> imageResult = new ArrayList<>();
    private static Context context;
    private GestureDetectorCompat mGestureDetector;

    static int mPosition = 0;
    LiveData<List<ImageResult>> imM;
    MutableLiveData<List<ImageResult>> im = new MutableLiveData<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        mGestureDetector = new GestureDetectorCompat(this, new GestureListener());

        imageWeb = findViewById(R.id.image_web);
        imageWeb.setVisibility(View.INVISIBLE);
        swapImage = findViewById(R.id.image_swap);
        swapImage.setVisibility(View.INVISIBLE);

        searchBut = findViewById(R.id.search_but);
        searchBut.setVisibility(View.INVISIBLE);
        searchBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageWeb.setVisibility(View.VISIBLE);
                imageWeb.getSettings().setJavaScriptEnabled(true);
                imageWeb.loadUrl(imageResult.get(mPosition).getLink());

                WebViewClient webViewClient = new WebViewClient() {
                    @SuppressWarnings("deprecation") @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }

                    @TargetApi(Build.VERSION_CODES.N) @Override
                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                        view.loadUrl(request.getUrl().toString());
                        return true;
                    }

                };
                imageWeb.setWebViewClient(webViewClient);
                searchBut.setVisibility(View.INVISIBLE);
            }
        });



        imageRecycler = findViewById(R.id.rec_im);
        imageRecycler.setLayoutManager(new GridLayoutManager(this, 3));

        imM = im;
        imM.observe(this, new Observer<List<ImageResult>>() {
            @Override
            public void onChanged(List<ImageResult> imageResults) {
                imageRecycler.setAdapter(new ImVRecAdapter(imageResultList));
            }
        });
    }

    public static void setImage(ImageView imageView, int position, List<ImageResult> imageResults) {

        swapImage.setImageDrawable(imageView.getDrawable());
        swapImage.setVisibility(View.VISIBLE);
        searchBut.setVisibility(View.VISIBLE);
        imageRecycler.setVisibility(View.INVISIBLE);

        imageResult = imageResults;
        mPosition = position;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search photo here");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                imageResultList.clear();

                Thread gfgThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try  {
                            Map<String, String> parameter = new HashMap<>();

                            // parameters
                            parameter.put("q", query);
                            parameter.put("num", "20");
                            parameter.put("tbm", "isch");
//                            parameter.put("ijn", "0");
                            parameter.put("api_key", "bc7fed1a4c4359aaaf2ad45f4047efda43c50fc07824bd3b6a58effc95314db0");

                            GoogleSearch search = new GoogleSearch(parameter);

                            try {
                                JsonObject data = search.getJson();
                                JsonArray results = data.get("images_results").getAsJsonArray();
                                for (int i = 0; i < results.size(); i++) {
                                    ImageResult imageResult = new ImageResult();
                                    imageResult.setPosition(results.get(i).getAsJsonObject().get("position").getAsString());
                                    imageResult.setTitle(results.get(i).getAsJsonObject().get("title").getAsString());
                                    imageResult.setLink((results.get(i).getAsJsonObject().get("link").getAsString()));
                                    imageResult.setOriginal(results.get(i).getAsJsonObject().get("original").getAsString());
                                    imageResult.setSource(results.get(i).getAsJsonObject().get("source").getAsString());
                                    imageResult.setThumbnail(results.get(i).getAsJsonObject().get("thumbnail").getAsString());
                                    imageResultList.add(imageResult);

                                }
                                im.postValue(imageResultList);
                            }
                            catch (SerpApiSearchException ex) {
                                System.out.println("oops exception detected!");
                                ex.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });
                gfgThread.start();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            if (velocityX > 1000) {
                if (mPosition != 0) {
                    mPosition--;
                    Picasso.with(MainActivity.getAppContext())
                            .load(imageResult.get(mPosition).getOriginal())
                            .into(swapImage);
                } else {
                    Toast.makeText(MainActivity.this, "Это первое изображение назад некуда", Toast.LENGTH_SHORT).show();
                }
            } else if (velocityX < -1000) {
                mPosition++;
                Picasso.with(MainActivity.getAppContext())
                        .load(imageResult.get(mPosition).getOriginal())
                        .into(swapImage);
            }


            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        if (swapImage.getVisibility() == View.VISIBLE) {
            Log.i("back", "onBack");
            swapImage.setVisibility(View.INVISIBLE);
            imageRecycler.setVisibility(View.VISIBLE);
        } else if (imageWeb.getVisibility() == View.VISIBLE) {
            imageWeb.setVisibility(View.INVISIBLE);
        } else {
            super.onBackPressed();

        }
    }

    public static Context getAppContext() {
        return MainActivity.context;
    }
}