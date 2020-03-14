package baomoi.huuutri.com.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import baomoi.huuutri.com.Adapter.AdapterRecyclerDetail;
import baomoi.huuutri.com.Adapter.AdapterRecyclerRelate;
import baomoi.huuutri.com.Ads.Adbanner;
import baomoi.huuutri.com.Ads.Common;
import baomoi.huuutri.com.LoadData.GetDataRelate;
import baomoi.huuutri.com.LoadData.LoadArticle;
import baomoi.huuutri.com.Model.Article;
import baomoi.huuutri.com.Model.Content;
import baomoi.huuutri.com.R;

public class DetailActivity extends AppCompatActivity {

    FrameLayout frmVideo,frmItem;
    UniversalVideoView videoView;
    UniversalMediaController controller;
    ImageView imgthumb;
    ImageButton btnPlay;
    RecyclerView recyclerViewDetail, recyclerViewRelate;
    ProgressBar progressBar;
    RelativeLayout rlRelate;
    TextView textViewTitle;
    AdView adView;

    AdapterRecyclerDetail adapterDetail;
    AdapterRecyclerRelate adapterRelate;

    ArrayList<Article> arrayListRelate,arrayListSave;
    ArrayList<Content> arrayListDetail;
    String id,title,source,thumb,sourceLink,linkVideo,tabTitle,category;
    int page;
    boolean isVideo,isNightMode,isSave,isSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Init();
        id=getIntent().getStringExtra("id");
        title=getIntent().getStringExtra("title");
        source=getIntent().getStringExtra("source");
        thumb=getIntent().getStringExtra("thumb");
        sourceLink=getIntent().getStringExtra("sourceLink");
        tabTitle=getIntent().getStringExtra("tabTitle");
        isVideo=getIntent().getBooleanExtra("isVideo",false);
        category=getIntent().getStringExtra("cate");
        page=getIntent().getIntExtra("page",1);

        //Dark mode
       /* SharedPreferences sharedPreferences = getSharedPreferences("night_mode", MODE_PRIVATE);
        isNightMode=sharedPreferences.getBoolean("night_mode",false);
        if(isNightMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);*/

        //Save article
        SharedPreferences preferencesSaveArt=this.getSharedPreferences("save_article",MODE_PRIVATE);
        Gson gsonsave=new Gson();
        String jsonsave=preferencesSaveArt.getString("save_article","");
        Type typesave=new TypeToken<List<Article>>(){}.getType();
        arrayListSave=new ArrayList<>();
        if(!jsonsave.equals("")){
            arrayListSave=gsonsave.fromJson(jsonsave,typesave);
            for(int i=0;i<arrayListSave.size();i++){
                if(arrayListSave.get(i).getId().equals(id)){
                    isSave=true;
                }
            }
        }

        //Video
        if(isVideo){
            linkVideo=getIntent().getStringExtra("linkVideo");
            frmVideo.setVisibility(View.VISIBLE);
            Glide.with(this).load(thumb).into(imgthumb);
            videoView.setVideoURI(Uri.parse(linkVideo));
            controller.setOnLoadingView(R.layout.custom_load_view);
            videoView.setMediaController(controller);
            btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnPlay.setVisibility(View.GONE);
                    imgthumb.setVisibility(View.GONE);
                    videoView.start();
                }
            });
        } else linkVideo="";

        //Tab title
        textViewTitle.setText(tabTitle);

        //Load relate
        arrayListRelate=new ArrayList<>();
        SharedPreferences preferencesRelate=this.getSharedPreferences("relate",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferencesRelate.getString("relate", "");
        Type type = new TypeToken<List<Article>>(){}.getType();
        if(!json.equals("")){
            arrayListRelate= gson.fromJson(json,type);
        }
        if(arrayListRelate.size()<10){
            new GetDataRelate(getApplicationContext(),arrayListRelate).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, category + "&page=" +page);
        }

        final ArrayList<Article> arrayList=new ArrayList<>();
        for(int i=0;arrayList.size()<10;i++){
            Random ran=new Random();
            int pos=ran.nextInt(arrayListRelate.size()-1);
            if(!arrayListRelate.get(pos).getId().equals(id)){
                arrayList.add(arrayListRelate.get(pos));
                arrayListRelate.remove(pos);
            }
        }
        //Text size
        SharedPreferences sharedPreferences1 = getSharedPreferences("font_size", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences1.edit();
        int size = sharedPreferences1.getInt("size",0);
        if (size!=0 && size!=20) {
            isSize = false;
            editor.putInt("size", 28);
            editor.commit();
        } else {
            isSize = true;
            editor.putInt("size", 20);
            editor.commit();
        }

        //Layout relate
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapterRelate=new AdapterRecyclerRelate(this,arrayList,category,tabTitle);
        recyclerViewRelate.setLayoutManager(layoutManager);
        recyclerViewRelate.setAdapter(adapterRelate);

        //Layout detail
        LinearLayoutManager layoutManager1=new LinearLayoutManager(this);
        adapterDetail=new AdapterRecyclerDetail(this,arrayListDetail);
        recyclerViewDetail.setLayoutManager(layoutManager1);
        recyclerViewDetail.setAdapter(adapterDetail);
        SharedPreferences preferencesUrl = getSharedPreferences("URL", Context.MODE_PRIVATE);
        String url = preferencesUrl.getString("detail", "");
        if (!url.equals("")) {
            Log.d("myservices", "===GetData===");
            new LoadArticle(this,id,arrayListDetail,adapterDetail,frmItem,progressBar).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,url + id);
        }

        //Ad Banner
        new Adbanner(adView);

        //Ad Full
        CountAds();
    }

    private void CountAds() {
        SharedPreferences sharedPreferences = getSharedPreferences("number", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int count = sharedPreferences.getInt("count", 0);
        if(count == 0){
            if (Common.interstitialAd!=null && Common.interstitialAd.isLoaded()){
                Common.interstitialAd.show();
            }
            editor.putInt("count",1);
            editor.commit();
        }
    }

    private void Init(){
        frmVideo=findViewById(R.id.frame_video);
        frmItem=findViewById(R.id.frame_item);
        videoView=findViewById(R.id.videoView);
        controller=findViewById(R.id.media_controller);
        imgthumb=findViewById(R.id.img_thumb);
        btnPlay=findViewById(R.id.button_play);
        recyclerViewDetail=findViewById(R.id.recycler_view_details);
        recyclerViewRelate=findViewById(R.id.recycler_view_relate);
        rlRelate=findViewById(R.id.relative_relate);
        progressBar=findViewById(R.id.progress_bar_details);
        textViewTitle=findViewById(R.id.text_title);
        adView=findViewById(R.id.adView_detail);
        arrayListDetail=new ArrayList<>();
        arrayListRelate=new ArrayList<>();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.detail,menu);
        if(isSave) menu.getItem(0).setIcon(R.drawable.bookmark_after);
        if(isSize){
            menu.getItem(1).setTitle("Tăng kích cỡ");
            menu.getItem(1).setIcon(R.drawable.increase_font);
        }
        else {
            menu.getItem(1).setTitle("Giảm kích cỡ");
            menu.getItem(1).setIcon(R.drawable.decrease_font);
        }
        //if(isNightMode) menu.getItem(2).setIcon(R.drawable.moon_white);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int idmenu=item.getItemId();
        switch (idmenu){
            case android.R.id.home:
                onBackPressed();
                finish();
                break;
            case R.id.action_danh_dau:
                if(isSave){
                    isSave=false;
                    item.setIcon(R.drawable.bookmark_before);
                    for(int i=0;i<arrayListSave.size();i++){
                        if(arrayListSave.get(i).getId().equals(id))
                            arrayListSave.remove(i);
                    }
                    Toast.makeText(DetailActivity.this, "Đã xóa khỏi tin đánh dấu", Toast.LENGTH_SHORT).show();
                }else{
                    isSave=true;
                    item.setIcon(R.drawable.bookmark_after);
                    arrayListSave.add(new Article(id,title,thumb,source,linkVideo,isVideo));
                    Toast.makeText(DetailActivity.this, "Đã thêm vào tin đánh dấu", Toast.LENGTH_SHORT).show();
                }
                SharedPreferences preferences=getSharedPreferences("save_article",MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                Gson gson=new Gson();
                String json=gson.toJson(arrayListSave);
                editor.putString("save_article",json);
                editor.commit();
                editor.apply();
                break;
            case R.id.action_text_size:
                if (isSize) {
                    item.setTitle("Giảm kích cỡ");
                    item.setIcon(R.drawable.decrease_font);
                    SharedPreferences sharedPreferences1 = getSharedPreferences("font_size", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                    int size = sharedPreferences1.getInt("size",0);
                    if (size!=0 && size==20){
                        isSize = false;
                        editor1.putInt("size",28);
                        editor1.commit();
                        recyclerViewDetail.removeAllViews();
                        adapterDetail.notifyDataSetChanged();
                    }
                }
                else{
                    item.setTitle("Tăng kích cỡ");
                    item.setIcon(R.drawable.increase_font);
                    SharedPreferences sharedPreferences1 = getSharedPreferences("font_size", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                    int size = sharedPreferences1.getInt("size",0);
                    if (size!=0 && size==28){
                        isSize = true;
                        editor1.putInt("size",20);
                        editor1.commit();
                        recyclerViewDetail.removeAllViews();
                        adapterDetail.notifyDataSetChanged();
                    }
                }
                break;
            /*case R.id.action_night_mode_detail:
                Boolean NightMode=!isNightMode;
                SharedPreferences sharedPreferences2 = getSharedPreferences("night_mode", MODE_PRIVATE);
                SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                editor2.putBoolean("night_mode",NightMode);
                editor2.commit();
                editor2.apply();
                recreate();
                break;*/
            /*case R.id.action_share_detail:
                Toast.makeText(this, "Vui lòng đợi!", Toast.LENGTH_SHORT).show();
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                //String shareBodyText = "Tin mới nhất";
                //sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, url);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, title+"\n"+sourceLink);
                startActivity(Intent.createChooser(sharingIntent, "Chia sẻ ứng dụng:"));
                break;*/
        }
        return super.onOptionsItemSelected(item);
    }
}
