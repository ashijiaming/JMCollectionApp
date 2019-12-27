package jm.com.collection.adapter;

import android.content.Context;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jm.com.collection.R;

/**
 * Created by Administrator on 2019/7/29.
 * Author Name ShiJiaMing
 * Description : adapter main 主界面所有研究项
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>{

    private static final String TAG="LevelAdapter";
    private List<String> mLevels;
    private Context mContext;

    public MainAdapter(Context context, List<String> Levels) {
        this.mLevels = Levels;
        this.mContext=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_main,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String title=mLevels.get(position);
        holder.tv_main_title.setText(position+"："+title);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemOnclickListener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLevels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.tv_main_title)
        TextView tv_main_title;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


    private ItemOnclickListener  itemOnclickListener;
    public interface ItemOnclickListener{
        void onClick(int position);
    }
    public void setItemOnclickListener(ItemOnclickListener itemOnclickListener){
        this.itemOnclickListener=itemOnclickListener;
    }
}
