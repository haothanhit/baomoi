package baomoi.huuutri.com.Adapter;

/**
 * Created by Thanh Trung on 30/05/2019.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;

import baomoi.huuutri.com.Activity.LoadDanhDauActivity;
import baomoi.huuutri.com.Model.Article;
import baomoi.huuutri.com.R;

public class AdapterSaveArticle extends RecyclerView.Adapter<AdapterSaveArticle.MyViewHolder> {

    private ArrayList<Article> data;
    private Activity context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title, source;
        private ImageView img;
        private ImageButton btDelete;
        private CardView cardView;

        private MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.txt_title_save);
            source = view.findViewById(R.id.txt_source_save);
            img = view.findViewById(R.id.img_save);
            btDelete = view.findViewById(R.id.bt_delete_save);
            cardView=view.findViewById(R.id.card_view_save);

        }
    }


    public AdapterSaveArticle(Activity context, ArrayList<Article> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public AdapterSaveArticle.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_item_save_article, parent, false);

        return new AdapterSaveArticle.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdapterSaveArticle.MyViewHolder holder, int position) {
        final int pos = data.size() - position - 1;
        final Article article = data.get(pos);
        holder.title.setText(article.getTitle());
        holder.source.setText(article.getSource());
        Glide.with(context).load(article.getThumb()).into(holder.img);
        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(pos);
                SharedPreferences appSharedPrefs = context.getSharedPreferences("save_article", Context.MODE_PRIVATE);
                Gson gson = new Gson();
                SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
                String jsonSave = gson.toJson(data);
                prefsEditor.putString("save_article", jsonSave);
                prefsEditor.commit();
                prefsEditor.apply();
                notifyDataSetChanged();
                Toast.makeText(context, "Đã Xóa!", Toast.LENGTH_SHORT).show();
                if(data.size()==0){
                    Toast.makeText(context, "Danh sách rỗng", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, LoadDanhDauActivity.class);
                i.putExtra("id", article.getId());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
