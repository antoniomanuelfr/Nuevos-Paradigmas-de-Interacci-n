package com.p207.npi.Museo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MapaPoniente extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TouchImageView img = new TouchImageView(this);
        img.setImageResource(R.drawable.map);
        img.setMaxZoom(4f);
        setContentView(img);
    }

}
