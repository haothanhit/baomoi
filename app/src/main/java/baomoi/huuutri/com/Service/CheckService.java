package baomoi.huuutri.com.Service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

import baomoi.huuutri.com.Model.Article;

public class CheckService extends Service {
    public CheckService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ArrayList<Article> arrayList=new ArrayList<>();
        new CheckData(getApplicationContext(),arrayList).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"http://45.76.191.33:8090/api/get_arc_by_catid?catid=0&page=0");
        Log.d("Myservices","===Check===");
        /*final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<Article> arrayList=new ArrayList<>();
                SharedPreferences preferences=getSharedPreferences("API",MODE_PRIVATE);
                String ip=preferences.getString("IP","45.76.191.33");
                new CheckData(getApplicationContext(),arrayList).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"http://"+ip+":8090/api/get_arc_by_catid?catid=0&page=0");
                Log.d("Myservices","===Checkip:"+ip+"===");
                handler.postDelayed(this,1000*40);
            }
        },1000*40);*/ // lap lai sau 40s
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }
}
