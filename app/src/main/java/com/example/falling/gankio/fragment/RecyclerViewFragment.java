package com.example.falling.gankio.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.falling.gankio.GankBean;
import com.example.falling.gankio.R;
import com.example.falling.gankio.adapter.AdapterFactory;
import com.example.falling.gankio.adapter.RecyclerViewAdapter;
import com.example.falling.gankio.db.GankDatabase;
import com.example.falling.gankio.db.GankDatabaseUtil;
import com.example.falling.gankio.listener.EndlessRecyclerOnScrollListener;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class RecyclerViewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String URL = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/";
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;


    private List<GankBean.Result> mContentItems = new ArrayList<>();
    private Gson gson = new Gson();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    private int i = 2;

    public static RecyclerViewFragment newInstance() {
        return new RecyclerViewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpUtils
                                .get()
                                .url(URL + i)
                                .build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e) {

                                    }

                                    @Override
                                    public void onResponse(String response) {
                                        GankBean bean = gson.fromJson(response, GankBean.class);
                                        if (TextUtils.equals(bean.getError(), "false")) {
                                            mContentItems.addAll(bean.getResults());
                                            mAdapter.notifyItemInserted(1);
                                        }
                                    }
                                });
                        i++;
                    }
                }).run();
            }
        });
        initSwipeRefreshLayout(view);

        //读取本地数据
        mContentItems = GankDatabaseUtil.queryAll();
        mAdapter = AdapterFactory.getInstance(mContentItems);

        mAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data) {
                Toast.makeText(view.getContext(), ((GankBean.Result) data).getUrl(), Toast.LENGTH_SHORT).show();
            }
        });

        mRecyclerView.setAdapter(mAdapter);

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);
    }

    private void initSwipeRefreshLayout(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.RED, Color.GREEN);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    //移动到顶部
    public void moveToTop() {
        this.onRefresh();
        mLayoutManager.scrollToPosition(0);
    }


    //加载更多
    @Override
    public void onRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils
                        .get()
                        .url(URL + 1)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }

                            @Override
                            public void onResponse(String response) {
                                GankBean bean = gson.fromJson(response, GankBean.class);
                                if (TextUtils.equals(bean.getError(), "false")) {
                                    mContentItems.clear();
                                    mContentItems.addAll(bean.getResults());
                                    mAdapter.notifyItemInserted(1);
                                    mSwipeRefreshLayout.setRefreshing(false);
                                    //插入数据库做缓存。
                                    GankDatabaseUtil.insert(bean);
                                }
                            }
                        });
            }
        }).run();
    }

}
