package baomoi.huuutri.com.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.MobileAds;

import baomoi.huuutri.com.Ads.Adfull;
import baomoi.huuutri.com.Ads.Common;
import baomoi.huuutri.com.MainActivity;
import baomoi.huuutri.com.Model.URLDATA;
import baomoi.huuutri.com.R;
import baomoi.huuutri.com.Service.CheckService;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        MobileAds.initialize(getApplicationContext(),getString(R.string.APP_ID));
        Common.interstitialAd=(new Adfull(getApplicationContext())).getAd();

        xoaDemAdsFull();
        initURL();

        Thread thread=new Thread(){
            @Override
            public void run() {
                try {
                    super.run();
                    //startService(new Intent(SplashActivity.this, CheckService.class));
                    sleep(2000);
                } catch (Exception e) {

                } finally {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        thread.start();
    }

    private void initURL() {
        SharedPreferences preferences=getSharedPreferences("URL",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("cate", URLDATA.URL_CATE_CHINH);
        editor.putString("detail",URLDATA.URL_DATA_CHINH);
        editor.apply();
    }
    private void xoaDemAdsFull() {
        SharedPreferences sharedPreferences=getSharedPreferences("number", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
