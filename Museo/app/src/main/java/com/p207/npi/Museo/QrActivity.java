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
                    String ScanRes = result.getContents();

//                  JSONObject obj = new JSONObject(result.getContents());
//                  String name = obj.getString("N");
//                  String address = obj.getString("ADR");


                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("name", ScanRes) ;
                    returnIntent.putExtra("address","0");

                    setResult(RESULT_OK, returnIntent);
                    finish();

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
