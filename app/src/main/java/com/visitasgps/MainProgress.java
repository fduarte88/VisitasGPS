package com.visitasgps;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.visitasgps.R;

public class MainProgress extends Activity implements View.OnClickListener {
    ProgressBar p;
    Button b;
    TextView t;
    Handler h = new Handler();
    Boolean isActivo = false;
    int i = 0;
    Intent x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        p=(ProgressBar)findViewById(R.id.progressBar);
        b=(Button)findViewById(R.id.start);
        t=(TextView)findViewById(R.id.porcentaje);
        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.start){
            if(!isActivo){
                Thread hiloBar = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (i<=0){
                            h.post(new Runnable() {
                                @Override
                                public void run() {
                                    t.setText(i+" %");
                                    p.setProgress(i);
                                }
                            });
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                System.out.println(e);
                            }
                        if(i==100){
                            x=new Intent(MainProgress.this, Imagen.class);
                            startActivity(x);

                        }
                        i++;
                        isActivo=true;
                        }

                    }
                });
                hiloBar.start();
            }

        }
    }


}