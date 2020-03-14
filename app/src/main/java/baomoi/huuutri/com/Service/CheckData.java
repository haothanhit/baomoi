package baomoi.huuutri.com.Service;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

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

public class CheckData extends AsyncTask<String, String, String> {
    ArrayList<Article> arrayList = new ArrayList<>();
    Context context;

    public CheckData(Context context, ArrayList<Article> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    protected String doInBackground(String... strings) {
        StringBuilder content = new StringBuilder();
        try {
            //API cũ
            //URL url = new URL(strings[0]);

            //API moi
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
            return "error";
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
        return content.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        if(s.equals("error")){
            SharedPreferences preferences = context.getSharedPreferences("API", Context.MODE_PRIVATE);
            String ip=preferences.getString("IP","45.76.191.33");
            SharedPreferences.Editor editor = preferences.edit();
            if(ip.equals("45.76.191.33")){
                editor.putString("IP", "178.128.24.36");
                Log.d("Myservices", "IP sau:178.128.24.36");
            } else{
                editor.putString("IP", "45.76.191.33");
                Log.d("Myservices", "IP sau:45.76.191.33");
            }
            editor.apply();
        } else {
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
                    }
                }
                Log.d("Myservices", "===Done!=== " + arrayList.size());
                if (arrayList.size() == 0) {
                    SharedPreferences preferences = context.getSharedPreferences("API", Context.MODE_PRIVATE);
                    String ip=preferences.getString("IP","45.76.191.33");
                    SharedPreferences.Editor editor = preferences.edit();
                    if(ip.equals("45.76.191.33")){
                        editor.putString("IP", "178.128.24.36");
                        Log.d("Myservices", "IP sau:178.128.24.36");
                    } else{
                        editor.putString("IP", "45.76.191.33");
                        Log.d("Myservices", "IP sau:45.76.191.33");
                    }
                    editor.apply();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        super.onPostExecute(s);
    }
}
