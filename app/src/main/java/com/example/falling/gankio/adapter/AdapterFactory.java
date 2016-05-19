package com.example.falling.gankio.adapter;

import com.example.falling.gankio.GankBean;

import java.util.List;

/**
 * Created by falling on 16/5/8.
 */
public class AdapterFactory {
    public static RecyclerViewAdapter getInstance(List<GankBean.Result> content){
        return new RecyclerViewAdapter(content);
    }
}
