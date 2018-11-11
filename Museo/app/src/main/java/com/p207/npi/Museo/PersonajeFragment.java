package com.p207.npi.Museo;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class PersonajeFragment extends Fragment {


    public PersonajeFragment() {
        // Required empty public constructor
    }

    private String ProcesarNombre(String nombre){
        String r = nombre.replace(' ', '_');
        r = r.toLowerCase();
    return r;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista =  inflater.inflate(R.layout.fragment_personaje, container, false);

        infoPersonaje modelView = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(infoPersonaje.class);
        String Name = ProcesarNombre(modelView.getName());
        String Explicacion = "explicacion_"+Name;
        int imageRes = getResources().getIdentifier(Name,"drawable",getActivity().getPackageName());
        int infoRes = getResources().getIdentifier(Explicacion,"string", getActivity().getPackageName());

        TextView ReadedName = vista.findViewById(R.id.nombre_info);
        ImageView Image = vista.findViewById(R.id.imageView_info);
        TextView Info = vista.findViewById(R.id.personaje_info);

        ReadedName.setText(modelView.getName());
        Image.setImageResource(imageRes);
        Info.setText(infoRes);

        // Inflate the layout for this fragment
        return vista;
    }


}
