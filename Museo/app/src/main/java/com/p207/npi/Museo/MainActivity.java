package com.p207.npi.Museo;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
{
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            switch (item.getItemId()) {

                case R.id.navigation_home:

                    transaction.replace(R.id.mainContainer,new HomeFragment());
                    transaction.commit();

                    return true;

                case R.id.navigation_qr:
                    transaction.replace(R.id.mainContainer,new QrFragment());
                    transaction.commit();

                    return true;

                case R.id.navigation_bot:
                    Fragment prev = manager.findFragmentByTag(Bot.class.getName());
                    //Identificar si el fragment esta guardado
                    if (prev == null) {
                        transaction.replace(R.id.mainContainer, new Bot(), Bot.class.getName());
                        transaction.addToBackStack(Bot.class.getName());
                        transaction.commit();

                        return true;

                    }else{
                        transaction.replace(R.id.mainContainer,prev);
                        transaction.commit();
                        return true;
                    }
            }

            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        ViewModelProviders.of(this).get(ModelInfoQr.class);


        transaction.replace(R.id.mainContainer,new HomeFragment());
        transaction.commit();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onBackPressed() {

        BottomNavigationView nav = findViewById(R.id.navigation);
        int seletedItemId = nav.getSelectedItemId();

        if (R.id.navigation_home == seletedItemId)
            super.onBackPressed();

        else {

            MenuItem item =nav.getMenu().getItem(0);
            mOnNavigationItemSelectedListener.onNavigationItemSelected(item);
            nav.setSelectedItemId(R.id.navigation_home);


        }
    }
}
