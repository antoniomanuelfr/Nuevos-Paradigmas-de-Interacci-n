package com.p207.npi.Museo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class QrActivity extends AppCompatActivity {
    public static int RESULT_OK = 1;
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        qrScan = new IntentIntegrator(this);
        StartScan();

    }
    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "No se encontró ningun resultado", Toast.LENGTH_LONG).show();
                finish();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    String name = obj.getString("name");
                    String address = obj.getString("address");
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("name",name) ;
                    returnIntent.putExtra("address",address);

                    setResult(RESULT_OK);
                    finish();
                    //setting values to textviews
                    //textViewName.setText(obj.getString("name"));
                    //textViewAddress.setText(obj.getString("address"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast

                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void StartScan() {
        //initiating the qr code scan
        qrScan.initiateScan();
    }
}
