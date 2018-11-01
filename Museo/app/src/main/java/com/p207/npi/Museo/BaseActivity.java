package com.p207.npi.Museo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public abstract class BaseActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    protected BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);

        initActivity();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateNavigationBarState();
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int itemId = item.getItemId();
            switch (itemId){
                case R.id.navigation_home:
                    startActivity(new Intent(this, HomeActivity.class));
                    return true;

                case R.id.navigation_qr:
                    startActivity(new Intent(this, QrActivity.class));
                    return true;

                case R.id.navigation_micro:
                    startActivity(new Intent(this, MicroActivity.class));
                    return true;
            }
            return false;
    }

    //Actualiza el elemento seleccionado en la barra
    protected void updateNavigationBarState(){
        int actionId = getNavigationMenuItemId();
        selectBottomNavigationBarItem(actionId);
    }
    //Actualiza seleccion
    void selectBottomNavigationBarItem(int itemId) {
        MenuItem item = navigationView.getMenu().findItem(itemId);
        item.setChecked(true);
    }
    //Funcion para devolver el layout correspondiente a la actividad heredada
    abstract int getContentViewId();
    //Funcion para devolver el boton asociado a la actividad
    abstract int getNavigationMenuItemId();
    //Funcion que lanza la parte no comun de la actividad (si no se hace asi genera problemas
    //con el bottomNavigationBar)
    abstract void initActivity();

}