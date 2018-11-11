package com.p207.npi.Museo;

    import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Objects;

public class HomeFragment extends Fragment implements View.OnClickListener{

    static boolean allowBack = true;

    public HomeFragment() {
        // Empty
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button a = Objects.requireNonNull(getActivity()).findViewById(R.id.boton_jon);
        a.setOnClickListener(this);
        Button b = getActivity().findViewById(R.id.boton_daenerys);
        b.setOnClickListener(this);
        Button c = getActivity().findViewById(R.id.boton_cersei);
        c.setOnClickListener(this);
        Button d =getActivity().findViewById(R.id.boton_tyrion);
        d.setOnClickListener(this);
        Button e = getActivity().findViewById(R.id.boton_stark);
        e.setOnClickListener(this);
        Button f = getActivity().findViewById(R.id.boton_targaryen);
        f.setOnClickListener(this);
        Button g = getActivity().findViewById(R.id.boton_lannister);
        g.setOnClickListener(this);
        Button h = getActivity().findViewById(R.id.boton_caminantes);
        h.setOnClickListener(this);
        Button i =getActivity().findViewById(R.id.boton_dragones);
        i.setOnClickListener(this);
        Button j = getActivity().findViewById(R.id.boton_vidriagon);
        j.setOnClickListener(this);
        getActivity().findViewById(R.id.boton_acero).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        infoPersonaje modelView = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(infoPersonaje.class);
        Fragment Personaje = new PersonajeFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();


        switch (v.getId()){

            case R.id.boton_jon:
                modelView.setName(getString(R.string.jon));
                break;

            case R.id.boton_daenerys:
                modelView.setName(getString(R.string.daenerys));
                break;

            case R.id.boton_cersei:
                modelView.setName(getString(R.string.cersei));
                break;

            case R.id.boton_tyrion:
                modelView.setName(getString(R.string.tyrion));
                break;


            case R.id.boton_stark:
                modelView.setName(getString(R.string.stark));
                break;

            case R.id.boton_targaryen:
                modelView.setName(getString(R.string.targaryen));
                break;

            case R.id.boton_lannister:
                modelView.setName(getString(R.string.lannister));
                break;

            case R.id.boton_caminantes:
                modelView.setName(getString(R.string.caminantes));
                break;

            case R.id.boton_dragones:
                modelView.setName(getString(R.string.dragones));
                break;

            case R.id.boton_vidriagon:
                modelView.setName(getString(R.string.vidriagon));
                break;
            case R.id.boton_acero:
                modelView.setName(getString(R.string.acero));
                break;
            case R.id.boton_mapa:
                Intent intent = new Intent (getActivity(), MapaPoniente.class);
                startActivity(intent);

        }
        allowBack = false;
        transaction.replace(R.id.mainContainer,Personaje, PersonajeFragment.class.getName());
        transaction.commit();
    }
}

