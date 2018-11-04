package com.p207.npi.Museo;


import android.content.Intent;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class QrFragment extends Fragment implements View.OnClickListener{
    private  final int SCAN_REQUEST_CODE = 21;

    private Button buttonScan;
    private TextView textViewName, textViewAddress;

    public QrFragment() {
        // Required empty public constructor
    }

    private Map<String, String> processResult(String result){

        Map<String, String> myMap = new HashMap<>();
        String[] pairs = result.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split("->");
            myMap.put(keyValue[0], keyValue[1]);
        }

        return myMap;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent Data){

        String NameKey="NAME";
        String URLKey="URL";

        super.onActivityResult(requestCode, resultCode, Data);

        //Se ha escaneado un codigo QR correctamente
        if (requestCode==SCAN_REQUEST_CODE){

            if (resultCode==QrActivity.RESULT_OK){
                String result = Data.getStringExtra("result");
                Map<String, String> mapResult = processResult(result);


                if (mapResult.containsKey(NameKey) && mapResult.containsKey(URLKey)) {

                    String Name = mapResult.get(NameKey);

                    String URL = mapResult.get(URLKey);

                    textViewName.setText(Name);
                    textViewAddress.setText(URL);

                }else{
                    Toast.makeText(getActivity(), "QR no valido.", Toast.LENGTH_LONG).show();

                }

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


