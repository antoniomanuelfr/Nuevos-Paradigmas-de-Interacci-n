package com.p207.npi.Museo;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class QrFragment extends Fragment implements View.OnClickListener{
    private  final int SCAN_REQUEST_CODE = 21;

    private Button buttonScan;
    private TextView textViewName, textViewAddress;

    public QrFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent Data){
        super.onActivityResult(requestCode, resultCode, Data);

        //Se ha escaneado un codigo QR correctamente
        if (requestCode==SCAN_REQUEST_CODE){

            if (resultCode==QrActivity.RESULT_OK){
                String result = Data.getStringExtra("result");
                String Name = result.substring(0,result.indexOf("\n"));
                String URL = result.substring(result.indexOf("\n"),result.length());

                textViewName.setText(Name);
                textViewAddress.setText(URL);

        //No se escaneo ningun codigo qr
            }else {
                Toast.makeText(getActivity(), "No se encontró ningun resultado", Toast.LENGTH_LONG).show();

            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_qr, container, false);

        buttonScan =  vista.findViewById(R.id.buttonScan);
        textViewName =  vista.findViewById(R.id.textViewName);
        textViewAddress =  vista.findViewById(R.id.textViewAddress);

        buttonScan.setOnClickListener(this);
        return vista;
    }

    @Override
    public void onClick(View v) {
        StartScanner();

    }
    private void StartScanner(){

        Intent i = new Intent(getActivity(),QrActivity.class);
        startActivityForResult(i,SCAN_REQUEST_CODE);
    }

}


