package com.p207.npi.Museo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.content.Intent;
public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    public void ApplySelection(Class Act){
        Intent intent = new Intent(this, Act);
        startActivity(intent);
    };
    private BottomNavigationView.OnNavigationItemReselectedListener mNavigationItemReselectedListener
            = new BottomNavigationView.OnNavigationItemReselectedListener() {
        @Override
        public void onNavigationItemReselected(@NonNull MenuItem menuItem) {

        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //intent = new Intent(this, HomeActivity.class);
                    //startActivity(intent);
                    return true;
                case R.id.navigation_qr:
                    ApplySelection(qrActivity.class);
                    return true;
                case R.id.navigation_micro:
                    ApplySelection(microActivity.class);

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
