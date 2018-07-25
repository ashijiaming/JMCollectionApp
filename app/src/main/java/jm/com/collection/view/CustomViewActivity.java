package jm.com.collection.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jm.com.collection.R;
import jm.com.collection.design.FabActivity;
import jm.com.collection.design.ParallelActivity;


public class CustomViewActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_main);

        listView= findViewById(R.id.lv_dialog_list);
        List<String> titles=new ArrayList<>();
        final List<Class> activities=new ArrayList<>();
        titles.add("水波纹效果");
        titles.add("Fragment转场动画");
        titles.add("平行动画");
        titles.add("Fab效果");

        activities.add(WaveActivity.class);
        activities.add(ViewPagerTransformActivity.class);
        activities.add(ParallelActivity.class);
        activities.add(FabActivity.class);

        ViewTitleAdapter adapter = new ViewTitleAdapter(this, R.layout.custom_list_item, titles);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               startActivity(new Intent(CustomViewActivity.this,activities.get(position)));
            }
        });
    }


    class ViewTitleAdapter extends ArrayAdapter<String>{
        private int mResource;
        public ViewTitleAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
            this.mResource=resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = getLayoutInflater().inflate(mResource, null);
            TextView title = view.findViewById(R.id.tv_list_title);
            title.setText(getItem(position));
            return view;
        }
    }


}
