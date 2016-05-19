package com.example.falling.gankio.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.falling.gankio.GankBean;
import com.example.falling.gankio.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    //the constants value of the header view
    static final int TYPE_PLACEHOLDER = Integer.MIN_VALUE;

    //the size taken by the header
    private int mPlaceholderSize = 1;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, Object data);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    List<GankBean.Result> contents;

    public RecyclerViewAdapter(List<GankBean.Result> contents) {
        this.contents = contents;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mPlaceholderSize)
            return TYPE_PLACEHOLDER;
        else
            return (position - mPlaceholderSize); //call getItemViewType on the adapter, less mPlaceholderSize

    }

    @Override
    public int getItemCount() {
        return contents.size() + mPlaceholderSize;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case TYPE_PLACEHOLDER: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(com.github.florent37.materialviewpager.R.layout.material_view_pager_placeholder, parent, false);
                return new RecyclerView.ViewHolder(view) {
                };
            }
            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_card_small, parent, false);
                //将创建的View注册点击事件
                view.setOnClickListener(this);
        }

        return new ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case TYPE_PLACEHOLDER:
                break;
            default:
                ViewHolder viewHolder = (ViewHolder) holder;
                GankBean.Result bean = contents.get(position-mPlaceholderSize);
                viewHolder.draweeView.setImageURI(Uri.parse(bean.getUrl()));
                viewHolder.itemView.setTag(bean);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, v.getTag());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        SimpleDraweeView draweeView;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.my_image_view);

        }
    }
}