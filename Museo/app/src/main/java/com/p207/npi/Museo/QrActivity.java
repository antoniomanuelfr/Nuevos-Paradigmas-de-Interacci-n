package com.p207.npi.Museo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class QrActivity extends BaseActivity {

    @Override
    int getContentViewId(){
        return R.layout.qr_layout;
    }
    @Override
    int getNavigationMenuItemId(){
        return R.id.navigation_qr;
    }
}
