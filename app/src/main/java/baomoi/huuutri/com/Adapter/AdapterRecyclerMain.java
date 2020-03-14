package baomoi.huuutri.com.Adapter;

/**
 * Created by Thanh Trung on 29/05/2019.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;

import java.util.ArrayList;

import baomoi.huuutri.com.Activity.DetailActivity;
import baomoi.huuutri.com.Ads.Adbanner;
import baomoi.huuutri.com.Model.Article;
import baomoi.huuutri.com.R;

import static android.content.Context.MODE_PRIVATE;

public class AdapterRecyclerMain extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Article> data;
    private Context context;
    private String category, tabtitle;

    private final int VIEW_ITEM_0=0, VIEW_ITEM_1 = 1, VIEW_ITEM_2 = 2, VIEW_ADS = 3;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title, source;
        private ImageView img;
        private CardView cardView;
        private AdView adView;

        private MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.txt_title_item_main);
            source = view.findViewById(R.id.txt_source_item_main);
            img = view.findViewById(R.id.img_item_main);
            adView=view.findViewById(R.id.ad_view_main);
            cardView=view.findViewById(R.id.card_view_main);
        }
    }

    public class AdHolder extends RecyclerView.ViewHolder {
        private TextView title, source;
        private ImageView img;
        private AdView adView;
        private CardView cardView;

        public AdHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txt_title_item_main);
            source = itemView.findViewById(R.id.txt_source_item_main);
            img = itemView.findViewById(R.id.img_item_main);
            adView = itemView.findViewById(R.id.ad_view_main);
            cardView = itemView.findViewById(R.id.card_view_main);

        }
    }

    public AdapterRecyclerMain(Context context, ArrayList<Article> data, String category, String Tab) {
        this.context = context;
        this.data = data;
        this.category = category;
        this.tabtitle = Tab;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if(viewType==VIEW_ITEM_0){
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_item_recycler_view_main_first, parent, false);
            vh = new MyViewHolder(itemView);
        }else if (viewType == VIEW_ITEM_1) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_item_recycler_view_main_1, parent, false);
            vh = new MyViewHolder(itemView);
        } else if (viewType == VIEW_ITEM_2) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_item_recycler_view_main_2, parent, false);
            vh = new MyViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_item_recycler_view_ads, parent, false);
            vh = new AdHolder(itemView);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AdHolder) {
            new Adbanner(((AdHolder) holder).adView);
            final Article article = data.get(position);
            ((AdHolder) holder).title.setText(article.getTitle());
            ((AdHolder) holder).source.setText(article.getSource().replace("- ", ""));
            ((AdHolder) holder).img.setVisibility(View.VISIBLE);
            Glide.with(context).load(article.getThumb()).placeholder(R.drawable.img_loading).into(((AdHolder) holder).img);
            ((AdHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("id", article.getId());
                    intent.putExtra("title", article.getTitle());
                    intent.putExtra("source", article.getSource());
                    intent.putExtra("thumb", article.getThumb());
                    intent.putExtra("tabTitle", tabtitle);
                    intent.putExtra("isVideo", article.isVideo());
                    intent.putExtra("linkVideo", article.getLinkVideo());
                    intent.putExtra("cate", category);
                    intent.putExtra("page", 1);

                    SharedPreferences appSharedPrefs = context.getSharedPreferences("relate", MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(data);
                    prefsEditor.putString("relate", json);
                    prefsEditor.commit();
                    prefsEditor.apply();

                    context.startActivity(intent);
                }
            });

        } else {
            if(position==1){
                ((MyViewHolder) holder).adView.setVisibility(View.VISIBLE);
                new Adbanner(((MyViewHolder) holder).adView);
            } else ((MyViewHolder) holder).adView.setVisibility(View.GONE);

            final Article article = data.get(position);
            ((MyViewHolder) holder).title.setText(article.getTitle());
            ((MyViewHolder) holder).source.setText(article.getSource().replace("- ", ""));
            ((MyViewHolder) holder).img.setVisibility(View.VISIBLE);
            Glide.with(context).load(article.getThumb()).placeholder(R.drawable.img_loading).into(((MyViewHolder) holder).img);
            ((MyViewHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("id", article.getId());
                    intent.putExtra("title", article.getTitle());
                    intent.putExtra("source", article.getSource());
                    intent.putExtra("thumb", article.getThumb());
                    intent.putExtra("tabTitle", tabtitle);
                    intent.putExtra("isVideo", article.isVideo());
                    intent.putExtra("linkVideo", article.getLinkVideo());
                    intent.putExtra("cate", category);
                    intent.putExtra("page", 1);

                    SharedPreferences appSharedPrefs = context.getSharedPreferences("relate", MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(data);
                    prefsEditor.putString("relate", json);
                    prefsEditor.commit();
                    prefsEditor.apply();

                    context.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (position){
            case 0:return VIEW_ITEM_0;
            case 1:return VIEW_ITEM_2;
            case 2:return VIEW_ITEM_2;
            case 3:return VIEW_ITEM_1;
            case 4:return VIEW_ITEM_1;
            case 5:return VIEW_ITEM_2;
            case 6:return VIEW_ADS;
        }
        switch (position%8){
            case 0: return VIEW_ITEM_1;
            case 1:return VIEW_ITEM_2;
            case 2:return VIEW_ITEM_2;
            case 3: return VIEW_ITEM_1;
            case 4: return VIEW_ITEM_1;
            case 5:return VIEW_ITEM_2;
            case 6:return VIEW_ADS;
            case 7: return VIEW_ITEM_1;
            default: return VIEW_ITEM_2;
        }
    }
}
