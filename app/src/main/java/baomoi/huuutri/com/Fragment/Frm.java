package baomoi.huuutri.com.Fragment;

/**
 * Created by Thanh Trung on 29/05/2019.
 */

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import baomoi.huuutri.com.Adapter.AdapterRecyclerMain;
import baomoi.huuutri.com.LoadData.GetData;
import baomoi.huuutri.com.Model.Article;
import baomoi.huuutri.com.R;

public class Frm extends Fragment {

    private String tabtitle, category, categorySave;
    private int page;
    boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount, pageLoadMore = 1;
    ArrayList<Article> arrayListSave;
    ArrayList<Article> arrayList, arrayListRelate;
    RecyclerView recyclerView;
    AdapterRecyclerMain adapter;
    FloatingActionButton fab;
    String id;
    TextView tvAlert;

    public static Frm newInstant(int page,String id, String text, String category) {

        Frm frm = new Frm();
        Bundle argBundle = new Bundle();
        argBundle.putInt("page", page);
        argBundle.putString("id",id);
        argBundle.putString("tabtitle", text);
        argBundle.putString("cate", category);
        frm.setArguments(argBundle);
        return frm;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("page");
        id=getArguments().getString("id");
        tabtitle = getArguments().getString("tabtitle");
        category = getArguments().getString("cate");
        categorySave = category.replace("http:", "").replaceAll("/", "").replace(".", "");

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frm, container, false);
        tvAlert=view.findViewById(R.id.text_view_alert);
        tvAlert.setVisibility(View.GONE);
        recyclerView = view.findViewById(R.id.recycler_view_main);
        arrayListSave = new ArrayList<>();
        arrayList = new ArrayList<>();
        arrayListRelate = new ArrayList<>();
        adapter = new AdapterRecyclerMain(getContext(), arrayList, category, tabtitle);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (position){
                    case 0: return 2;
                    case 1:return 2;
                    case 2: return 2;
                }
                switch (position % 4) {
                    case 0: return 1;
                    case 1:return 2;
                    case 2: return 2;
                    case 3: return 1;
                    default:
                        return 1;
                }
            }
        });

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        final ArrayList<String> arrayListID = new ArrayList<>();

        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_to_refesh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                arrayList.clear();
                new GetData(getContext(),id, arrayList, adapter,tvAlert, swipeRefreshLayout).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, category);
            }
        });

        fab=view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                recyclerView.scrollToPosition(0);
                fab.setVisibility(View.GONE);
            }
        });

        new GetData(getContext(),id, arrayList, adapter,tvAlert, swipeRefreshLayout).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, category);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                GridLayoutManager manager;
                manager = gridLayoutManager;
                if(dx==0) fab.setVisibility(View.GONE);
                if (dy > 0) //check for scroll down
                {
                    fab.setVisibility(View.VISIBLE);
                    visibleItemCount = manager.getChildCount();
                    totalItemCount = manager.getItemCount();
                    pastVisiblesItems = manager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            //Do pagination.. i.e. fetch new data.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,category);
                            new GetData(getContext(),id, arrayList, adapter,tvAlert, swipeRefreshLayout, pageLoadMore).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, category + "&page=" + pageLoadMore);
                            pageLoadMore++;
                            loading = true;
                        }
                    }
                }
            }
        });
        tvAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAlert.setVisibility(View.GONE);
                new GetData(getContext(),id, arrayList, adapter,tvAlert, swipeRefreshLayout).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, category);
            }
        });

        return view;
    }
}