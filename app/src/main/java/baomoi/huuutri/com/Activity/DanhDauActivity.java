package baomoi.huuutri.com.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import baomoi.huuutri.com.Adapter.AdapterSaveArticle;
import baomoi.huuutri.com.Model.Article;
import baomoi.huuutri.com.R;

public class DanhDauActivity extends AppCompatActivity {

    ArrayList<Article> arrayList = new ArrayList<>();
    RecyclerView recyclerView;
    AdapterSaveArticle adapter;
    TextView tv_khong_co_tin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.startusColor));
        }
        setContentView(R.layout.activity_danh_dau);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences appSharedPrefs = getSharedPreferences("save_article", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("save_article", "");
        Type type = new TypeToken<List<Article>>(){}.getType();
        if(!json.equals("")){
            arrayList = gson.fromJson(json, type);
        }
        init();
        if(arrayList.size()==0){
            tv_khong_co_tin.setVisibility(View.VISIBLE);
        }
        //Load data
        adapter = new AdapterSaveArticle(this, arrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.danhdau,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                onBackPressed();
                break;
            case R.id.action_delete_all:
                if(arrayList.size()!=0){
                    arrayList.clear();
                    SharedPreferences appSharedPrefs1 = getSharedPreferences("save_article", Context.MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor1 = appSharedPrefs1.edit();
                    prefsEditor1.remove("save_article");
                    prefsEditor1.commit();
                    prefsEditor1.apply();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(),"Xóa thành công",Toast.LENGTH_SHORT).show();
                    tv_khong_co_tin.setVisibility(View.VISIBLE);
                } else Toast.makeText(DanhDauActivity.this, "Danh sách tin rỗng", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init(){
        tv_khong_co_tin=findViewById(R.id.txt_khong_co_tin);
        recyclerView = findViewById(R.id.recycler_view_danh_dau);
    }

}
