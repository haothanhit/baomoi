package baomoi.huuutri.com.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;

import baomoi.huuutri.com.Activity.DetailActivity;
import baomoi.huuutri.com.Model.Article;
import baomoi.huuutri.com.R;

import static android.content.Context.MODE_PRIVATE;

public class AdapterRecyclerRelate extends RecyclerView.Adapter<AdapterRecyclerRelate.MyViewHolder> {

    private ArrayList<Article> data;
    private Activity context;
    private String tabTitle,category;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title, source;
        private ImageView img;
        private CardView cardView;

        private MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.txt_title_relate);
            source = view.findViewById(R.id.txt_source_relate);
            img = view.findViewById(R.id.img_relate);
            cardView=view.findViewById(R.id.card_view_relate);
        }
    }


    public AdapterRecyclerRelate(Activity context, ArrayList<Article> data,String category,String tabtitle) {
        this.context = context;
        this.data = data;
        this.category=category;
        this.tabTitle=tabtitle;
    }

    @Override
    public AdapterRecyclerRelate.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_item_relate, parent, false);

        return new AdapterRecyclerRelate.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdapterRecyclerRelate.MyViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        final Article article = data.get(position);
        holder.title.setText(article.getTitle());
        holder.source.setText(article.getSource());
        Glide.with(context).load(article.getThumb()).into(holder.img);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("id", article.getId());
                intent.putExtra("title", article.getTitle());
                intent.putExtra("source", article.getSource());
                intent.putExtra("thumb", article.getThumb());
                intent.putExtra("tabTitle", tabTitle);
                intent.putExtra("isVideo", article.isVideo());
                intent.putExtra("linkVideo", article.getLinkVideo());
                intent.putExtra("cate", category);
                intent.putExtra("page", 1);

                context.startActivity(intent);
                context.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
