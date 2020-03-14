package baomoi.huuutri.com.LoadData;

/**
 * Created by Thanh Trung on 29/05/2019.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import baomoi.huuutri.com.Model.Article;

public class GetDataRelate extends AsyncTask<String, String, String> {

    ArrayList<Article> arrayList = new ArrayList<>();
    Context context;

    public GetDataRelate(Context context, ArrayList<Article> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            JSONArray jsonArray = new JSONArray(s);
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String title = jsonObject.getString("title");
                String source = jsonObject.getString("source");
                String thumb = jsonObject.getString("thumb").replace("w300", "w500");
                String sourceLink=jsonObject.getString("sourceLink");
                String linkVideo=jsonObject.getString("linkVideo");
                boolean isVideo=jsonObject.getBoolean("isVideo");

                if(!title.equals("") && !id.equals("") && !source.equals("") && !thumb.equals("")){
                    if(arrayList.size()<20){
                        arrayList.add(new Article(id,title, thumb, source,linkVideo,isVideo));
                    }
                }
            }
            SharedPreferences preferences=context.getSharedPreferences("relate",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            Gson gson=new Gson();
            String json=gson.toJson(arrayList);
            editor.putString("relate",json);
            editor.commit();
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        super.onPostExecute(s);
    }
}
