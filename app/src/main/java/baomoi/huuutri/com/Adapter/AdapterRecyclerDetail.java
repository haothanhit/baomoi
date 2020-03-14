package baomoi.huuutri.com.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import baomoi.huuutri.com.Ads.Adbanner;
import baomoi.huuutri.com.Model.Content;
import baomoi.huuutri.com.R;

public class AdapterRecyclerDetail extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Content> data;
    private Context context;
    private int VIEW_ITEM = 1, VIEW_ADS = 2;

    public AdapterRecyclerDetail(Context context, ArrayList<Content> data) {
        this.context = context;
        this.data = data;
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        private TextView txt;
        private ImageView img;

        public ItemHolder(View view) {
            super(view);
            txt = view.findViewById(R.id.txt_detail);
            img = view.findViewById(R.id.img_detail);
        }
    }

    private class AdHolder extends RecyclerView.ViewHolder {
        private TextView txt;
        private ImageView img;
        private AdView adView;

        public AdHolder(@NonNull View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.txt_detail);
            img = itemView.findViewById(R.id.img_detail);
            adView = itemView.findViewById(R.id.ad_view_item);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.custom_item_detail, parent, false);

            vh = new ItemHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.custom_item_ads, parent, false);

            vh = new AdHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AdHolder) {
            new Adbanner(((AdHolder) holder).adView);

            final Content content = data.get(position);
            if (content.getText().equals("")) {
                if (!content.getImg().equals("")) {
                    Glide.with(context).load(content.getImg()).into(((AdHolder) holder).img);
                    ((AdHolder) holder).img.setVisibility(View.VISIBLE);
                }
            }
            if (content.getImg().equals("")) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("font_size", Context.MODE_PRIVATE);
                int size = sharedPreferences.getInt("size", 0);
                if (size != 0) {
                    ((AdHolder) holder).txt.setTextSize(size);

                    if (!content.getText().equals("")) {
                        ((AdHolder) holder).txt.setText(content.getText());
                        ((AdHolder) holder).txt.setVisibility(View.VISIBLE);
                    }
                } else {
                    ((AdHolder) holder).txt.setTextSize(20);

                    if (!content.getText().equals("")) {
                        ((AdHolder) holder).txt.setText(content.getText());
                        ((AdHolder) holder).txt.setVisibility(View.VISIBLE);
                    }
                }
            }
        } else {
            final Content content = data.get(position);
            if (content.getText().equals("")) {
                if (!content.getImg().equals("")) {
                    Glide.with(context).load(content.getImg()).into(((ItemHolder) holder).img);
                    ((ItemHolder) holder).img.setVisibility(View.VISIBLE);
                    ((ItemHolder) holder).txt.setVisibility(View.GONE);
                }
            }
            if (content.getImg().equals("")) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("font_size", Context.MODE_PRIVATE);
                int size = sharedPreferences.getInt("size", 0);
                if (size != 0) {
                    ((ItemHolder) holder).txt.setTextSize(size);
                    ((ItemHolder) holder).img.setVisibility(View.GONE);

                    if (position == 0) {  // set title
                        ((ItemHolder) holder).txt.setTextSize(size + 6);
                        ((ItemHolder) holder).txt.setTypeface(null, Typeface.BOLD);
                    }

                    if (position == 1) { //set source
                        ((ItemHolder) holder).txt.setTextSize(size + 2);
                    }

                    if (position == data.size() - 1) {  // set author
                        ((ItemHolder) holder).txt.setTypeface(null, Typeface.BOLD);
                    }

                    if (!content.getText().equals("")) {
                        ((ItemHolder) holder).txt.setText(content.getText());
                        ((ItemHolder) holder).txt.setVisibility(View.VISIBLE);
                    }
                } else {
                    ((ItemHolder) holder).txt.setTextSize(20);

                    if (position == 0) {  // set title
                        ((ItemHolder) holder).txt.setTextSize(26);
                        ((ItemHolder) holder).txt.setTypeface(null, Typeface.BOLD);
                    }

                    if (position == 1) { //set source
                        ((ItemHolder) holder).txt.setTextSize(22);
                    }

                    if (position == data.size() - 1) {  // set author
                        ((ItemHolder) holder).txt.setTypeface(null, Typeface.BOLD);
                    }

                    if (!content.getText().equals("")) {
                        ((ItemHolder) holder).txt.setText(content.getText());
                        ((ItemHolder) holder).txt.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position==6) return VIEW_ADS;
        if(data.size()>30){
            if(position==data.size()/2) return VIEW_ADS;
        }
        return VIEW_ITEM;
    }
}
