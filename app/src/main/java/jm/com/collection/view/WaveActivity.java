package jm.com.collection.view;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import jm.com.collection.R;
import jm.com.collection.view.dialog.LoadDialog;
import jm.com.collection.view.me.WaveView;


public class WaveActivity extends AppCompatActivity{

    private WaveView waveView;
    private Button btnShowDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave);
        waveView = (WaveView) findViewById(R.id.wave_view);
        btnShowDialog= (Button) findViewById(R.id.btn_show_wave_dialog);
        btnShowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LoadDialog loadDialog = new LoadDialog();
                loadDialog.show(getFragmentManager(),"loading");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadDialog.dismiss();
                    }
                },3000);
            }
        });

        waveView.startAnimator();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                waveView.stopAnimator();
            }
        },10000);


    }
}
