package com.p207.npi.Museo;

    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.content.Intent;

    import com.google.zxing.integration.android.IntentIntegrator;
    import com.google.zxing.integration.android.IntentResult;

public class QrActivity extends AppCompatActivity {

    public static int RESULT_OK = 1;
    public static int RESULT_NOT_OK = 0;
    public static int NULL_RESULT = -1;

    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        qrScan = new IntentIntegrator(this);
        StartScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Intent returnIntent = new Intent();
                setResult(RESULT_NOT_OK, returnIntent);
                finish();
            } else {
                String ScanRes = result.getContents();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", ScanRes) ;
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        } else {
            Intent returnIntent = new Intent();
            setResult(NULL_RESULT, returnIntent);
            finish();
        }
    }

    public void StartScan() {
        qrScan.initiateScan();
    }
}