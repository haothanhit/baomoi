package baomoi.huuutri.com.LoadData;

/**
 * Created by Thanh Trung on 29/05/2019.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import baomoi.huuutri.com.Adapter.AdapterRecyclerMain;
import baomoi.huuutri.com.Model.Article;
import baomoi.huuutri.com.Model.URLDATA;

public class GetData extends AsyncTask<String, String, String> {

    ArrayList<Article> arrayList = new ArrayList<>();
    ArrayList<Article> arrayListRelate = new ArrayList<>();
    AdapterRecyclerMain adapter;
    Context context;
    SwipeRefreshLayout swipeRefreshLayout;
    int page;
    String id;
    final String ERROR="Khong ket noi";
    TextView tvAlert;

    public GetData(Context context,String id, ArrayList<Article> arrayList, AdapterRecyclerMain adapter,TextView tv, SwipeRefreshLayout swipeRefreshLayout) {
        this.context = context;
        this.arrayList = arrayList;
        this.adapter = adapter;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.id=id;
        this.page=0;
        this.tvAlert=tv;
    }
    public GetData(Context context,String id, ArrayList<Article> arrayList, AdapterRecyclerMain adapter,TextView tv, SwipeRefreshLayout swipeRefreshLayout,int page) {
        this.context = context;
        this.id=id;
        this.arrayList = arrayList;
        this.adapter = adapter;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.page=page;
        this.tvAlert=tv;
    }

    @Override
    protected String doInBackground(String... strings) {
        StringBuilder content = new StringBuilder();
        try {

            String str=strings[0];
            String[] splitString=str.split("&");
            URL url;
            if(splitString.length==3){
                String s=splitString[0]+"&"+splitString[2];
                url=new URL(s);
            } else{
                url=new URL(strings[0]);
            }

            InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line = "";

            while ((line = bufferedReader.readLine()) != null){
                content.append(line);
            }

            bufferedReader.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return  ERROR;
        } catch (IOException e) {
            e.printStackTrace();
            return  ERROR;
        }
        return content.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        SharedPreferences preferencesCount1=context.getSharedPreferences("retryMain",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1=preferencesCount1.edit();
        editor1.clear().apply();
        if(!s.equals(ERROR)) {
            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id = jsonObject.getString("id");
                    String title = jsonObject.getString("title");
                    String source = jsonObject.getString("source").replace("Nguồn: ", "");
                    String thumb = jsonObject.getString("thumb").replace("w300", "w500");
                    String linkVideo = jsonObject.getString("linkVideo");
                    boolean isVideo = jsonObject.getBoolean("isVideo");

                    if (!title.equals("") && !id.equals("") && !source.equals("") && !thumb.equals("")) {
                        arrayList.add(new Article(id, title, thumb, source, linkVideo, isVideo));
                        if (arrayListRelate.size() < 20) {
                            arrayListRelate.add(new Article(id, title, thumb, source, linkVideo, isVideo));
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            SharedPreferences preferences=context.getSharedPreferences("URL",Context.MODE_PRIVATE);
            String url=preferences.getString("cate","");
            SharedPreferences.Editor editorUrl=preferences.edit();
            SharedPreferences preferencesCount=context.getSharedPreferences("retryMain",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=preferencesCount.edit();
            int dem=preferencesCount.getInt("dem",0);
            if(!url.equals("")){
                if(dem<5) {
                    Log.d("Myservices", "===Doi IP===");
                    if (url.equals(URLDATA.URL_CATE_CHINH)) {
                        editorUrl.putString("cate",URLDATA.URL_CATE_PHU).apply();
                        new GetData(context,id,arrayList,adapter,tvAlert,swipeRefreshLayout,page).executeOnExecutor(THREAD_POOL_EXECUTOR,URLDATA.URL_CATE_PHU+id+"&page="+page);

                    } else {
                        editorUrl.putString("cate",URLDATA.URL_CATE_CHINH).apply();
                        new GetData(context,id,arrayList,adapter,tvAlert,swipeRefreshLayout,page).executeOnExecutor(THREAD_POOL_EXECUTOR,URLDATA.URL_CATE_CHINH+id+"&page="+page);
                    }
                    dem++;
                    editor.putInt("dem", dem);
                    editor.apply();
                } else {
                    tvAlert.setVisibility(View.VISIBLE);
                }
            }
        }
        super.onPostExecute(s);
    }
}