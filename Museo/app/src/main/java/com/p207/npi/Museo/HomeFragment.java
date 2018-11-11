package com.p207.npi.Museo;

    import android.os.Bundle;
    import android.support.annotation.NonNull;
    import android.support.annotation.Nullable;
    import android.support.v4.app.Fragment;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.Toast;

public class HomeFragment extends Fragment implements View.OnClickListener{

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
        Button a = getActivity().findViewById(R.id.boton_jon);
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

        switch (v.getId()){

            case R.id.boton_jon:

            case R.id.boton_daenerys:

            case R.id.boton_cersei:

            case R.id.boton_tyrion:

            case R.id.boton_stark:

            case R.id.boton_targaryen:

            case R.id.boton_lannister:

            case R.id.boton_caminantes:

            case R.id.boton_dragones:

            case R.id.boton_vidriagon:

        }

    }
}

