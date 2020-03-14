package baomoi.huuutri.com.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import baomoi.huuutri.com.Adapter.AdapterRecyclerDetail;
import baomoi.huuutri.com.Ads.Adbanner;
import baomoi.huuutri.com.LoadData.LoadArticle;
import baomoi.huuutri.com.Model.Content;
import baomoi.huuutri.com.R;

public class LoadDanhDauActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Content> arrayList;
    AdapterRecyclerDetail adapter;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_danh_dau);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.startusColor));
        }
        Toolbar toolbar=findViewById(R.id.toolbar_LoadDanhDau);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_view_load_danh_dau);
        arrayList = new ArrayList<>();
        adapter = new AdapterRecyclerDetail(this, arrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        id = getIntent().getStringExtra("id");
        //Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
        FrameLayout frm=findViewById(R.id.frame_item);
        ProgressBar progressBar = new ProgressBar(this);

        //API mới
        SharedPreferences preferencesUrl = getSharedPreferences("URL", Context.MODE_PRIVATE);
        String url = preferencesUrl.getString("detail", "");
        if (!url.equals("")) {
            Log.d("myservices", "===GetData===");
            new LoadArticle(this,id,arrayList,adapter,frm,progressBar).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,url + id);
        }
        //Ads Banner
        AdView adView=findViewById(R.id.ad_view_load);
        new Adbanner(adView);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
