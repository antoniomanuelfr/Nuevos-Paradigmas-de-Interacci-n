package com.p207.npi.Museo;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class QrFragment extends Fragment implements View.OnClickListener{
    private  final int SCAN_REQUEST_CODE = 21;

    public QrFragment() {
        // Required empty public constructor
    }

    private Map<String, String> processResult(String result){

        Map<String, String> myMap = new HashMap<>();
        String[] pairs = result.split(",");

        for (String pair : pairs) {

            String[] keyValue = pair.split("->");

            if(keyValue.length==2)
                myMap.put(keyValue[0], keyValue[1]);
            else
                return null;
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

            if (resultCode==QrActivity.RESULT_OK) {
                String result = Data.getStringExtra("result");
                Map<String, String> mapResult = processResult(result);

                if (mapResult == null) {
                    Toast.makeText(getActivity(), "QR no valido.", Toast.LENGTH_LONG).show();

                } else if (mapResult.containsKey(NameKey) && mapResult.containsKey(URLKey)) {

                    String Name = mapResult.get(NameKey);

                    String URL = mapResult.get(URLKey);
                    Toast.makeText(getActivity(), "QR aceptado", Toast.LENGTH_LONG).show();

                    ModelInfoQr modelView = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(ModelInfoQr.class);
                    modelView.setName(Name);
                    modelView.setURL(URL);

                    Fragment readedQr = new ReadedQrFragment();

                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.mainContainer,readedQr, ReadedQrFragment.class.getName());
                    transaction.addToBackStack(ReadedQrFragment.class.getName());

                    transaction.commit();

                } else {
                    Toast.makeText(getActivity(), "No se encontró ningun resultado.", Toast.LENGTH_LONG).show();

                }
            }else{
                Toast.makeText(getActivity(), "No se leyó el QR, pruebe otra vez", Toast.LENGTH_LONG).show();

            }
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_qr, container, false);

        Button buttonScan = vista.findViewById(R.id.buttonScan);

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


