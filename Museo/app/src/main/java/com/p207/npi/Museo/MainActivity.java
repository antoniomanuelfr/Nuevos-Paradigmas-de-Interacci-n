// Bot - https://github.com/flatfisher/android-dialogflow-chatbot-sample
// Shake Acelerometro Sensor - http://jasonmcreynolds.com/?p=388
// Proximidad y Giroscopio - https://code.tutsplus.com/es/tutorials/android-sensors-in-depth-proximity-and-gyroscope--cms-28084

package com.p207.npi.Museo;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private Sensor proximitySensor;
    private SensorEventListener proximitySensorListener;
    private boolean salir = false;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_home:

                    Fragment prevHome = manager.findFragmentByTag(HomeFragment.class.getName());

                    if (prevHome == null) {
                        transaction.replace(R.id.mainContainer, new HomeFragment(), HomeFragment.class.getName());
                        transaction.addToBackStack(HomeFragment.class.getName());
                        transaction.commit();
                        return true;

                    }else{
                        transaction.replace(R.id.mainContainer,prevHome);
                        transaction.commit();
                        return true;
                    }

                case R.id.navigation_qr:

                    transaction.replace(R.id.mainContainer,new QrFragment());
                    transaction.commit();
                    return true;

                case R.id.navigation_bot:
                    Fragment prevBot = manager.findFragmentByTag(Bot.class.getName());
                    if (prevBot == null) {

                        transaction.replace(R.id.mainContainer, new Bot(), Bot.class.getName());
                        transaction.addToBackStack(Bot.class.getName());
                        transaction.commit();
                        return true;

                    }else{
                        transaction.replace(R.id.mainContainer,prevBot);
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
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();

        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                //Accion Shake - Mostrar Mapa
            }
        });

        proximitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if(proximitySensor == null) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        BottomNavigationView nav = findViewById(R.id.navigation);
        int seletedItemId = nav.getSelectedItemId();

        if (R.id.navigation_home == seletedItemId){
            if (!salir){
                Toast.makeText(this, "Pulse otra vez para salir", Toast.LENGTH_LONG).show();
                salir =true;

            }else
                finish();

        }else {

            nav.setSelectedItemId(R.id.navigation_home);
            FragmentManager Manager = getSupportFragmentManager();
            Fragment HomeFragment = Manager.findFragmentByTag(HomeFragment.class.getName());
            FragmentTransaction transaction = Manager.beginTransaction();

            if (HomeFragment==null){

                transaction.replace(R.id.mainContainer, new HomeFragment(), HomeFragment.class.getName());
                transaction.addToBackStack(Bot.class.getName());
                transaction.commit();
                }else{
                    transaction.replace(R.id.mainContainer,HomeFragment);
                    transaction.commit();

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
        proximitySensorListener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if(sensorEvent.values[0] < proximitySensor.getMaximumRange()) {
                    // Accion Proximidad - Activar QR
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                // Empty
            }
        };
        mSensorManager.registerListener(proximitySensorListener, proximitySensor, 2 * 1000 * 1000);
    }

    @Override
    public void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        mSensorManager.unregisterListener(proximitySensorListener);
        super.onPause();
    }
}
