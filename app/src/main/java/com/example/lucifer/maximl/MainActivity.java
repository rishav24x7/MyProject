package com.example.lucifer.maximl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.example.lucifer.maximl.ClipboardMonitorService;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    ProgressBar progressBar;
    private Context context;
    private Activity activity;
    private CoordinatorLayout coordinatorLayout;
    private WebView webView;
    private ImageView splash;

    public static String id1 = "test_channel_01";


    @SuppressLint({"SetJavaScriptEnabled", "ObsoleteSdkInt"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        activity = this;

        progressBar = findViewById(R.id.pb_webLoad);

        coordinatorLayout = findViewById(R.id.cl_webView);

        splash = findViewById(R.id.img_splash);

        webView = findViewById(R.id.wv_nyoloWeb);
        webView.setVisibility(View.GONE);

        loadWeb();
        startService(new Intent(this, ClipboardMonitorService.class));
        startService();

        Uri uri = getIntent().getData();
        if(uri != null)
        {
            List<String> params = uri.getPathSegments();

            System.out.println(params);
        }

    }

    private void loadWeb() {
        final Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(1000);

        final Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setStartOffset(1000);
        fadeOut.setDuration(1000);

        final AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(fadeIn);
        animationSet.addAnimation(fadeOut);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                splash.setAnimation(fadeOut);
                splash.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        splash.setVisibility(View.GONE);
                        webView.setVisibility(View.VISIBLE);
                        webView.setAnimation(fadeIn);
                    }
                }, 1600);
            }
        });

        webView.loadUrl("https://www.facebook.com/");
        webView.setWebChromeClient(new WebChromeClient());

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            settings.setDatabasePath("/data/data" + webView.getContext().getPackageName() + "/databases/");
        }
    }


    public void startService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Maximl's foreground service is running");
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        stopService(serviceIntent);
    }

}


