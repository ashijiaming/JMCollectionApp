package jm.com.collection.design;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jm.com.collection.R;

/**
 * Created by ShiJiaMing on 2018/4/17.
 * Description
 */

public class FabActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerview;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fab);
        toolbar=findViewById(R.id.toolbar);
        recyclerview = findViewById(R.id.recycler_view);
        setSupportActionBar(toolbar);
        setTitle("宜邻社");

        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            list.add("Item"+i);
        }
        RecyclerView.Adapter adapter = new FabRecyclerAdapter(list );
        recyclerview.setAdapter(adapter);
    }

    class FabRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<String> list;

        public FabRecyclerAdapter(List<String> list) {
            // TODO Auto-generated constructor stub
            this.list = list;
        }

        @Override
        public int getItemCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            String str = list.get(position);
            MyViewHolder holder = (MyViewHolder) viewHolder;
            holder.tv.setText(str);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
            // TODO Auto-generated method stub
            View view = LayoutInflater.from(arg0.getContext()).inflate(R.layout.listitem, arg0, false);
            return new MyViewHolder(view);
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView tv;

            public MyViewHolder(View itemView) {
                super(itemView);
                tv = itemView.findViewById(R.id.tv);
            }

        }

    }
}
