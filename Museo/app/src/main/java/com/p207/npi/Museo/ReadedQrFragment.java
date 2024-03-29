package com.p207.npi.Museo;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class ReadedQrFragment extends Fragment {

    public ReadedQrFragment() {
        // Empty
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_qr_readed, container, false);
        ModelInfoQr modelView = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(ModelInfoQr.class);
        String name = modelView.getName();
        String imageName = name.replace(' ','_').toLowerCase();
        int imageRes = getResources().getIdentifier(imageName,"drawable",getActivity().getPackageName());

        TextView ReadedName = vista.findViewById(R.id.readed_name_from_qr);
        ImageView Image = vista.findViewById(R.id.image_readed_from_qr);
        vista.findViewById(R.id.ask_bot_from_qr).setOnClickListener(mListener);
        vista.findViewById(R.id.info_button_from_qr).setOnClickListener(mListener);
        vista.findViewById(R.id.go_wiki_from_qr).setOnClickListener(mListener);

        ReadedName.setText(name);
        Image.setImageResource(imageRes);

        return vista;
    }

    private final View.OnClickListener mListener = new View.OnClickListener() {
        public void onClick(View view) {
            ModelInfoQr modelView = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(ModelInfoQr.class);
            String Name = modelView.getName();

            switch (view.getId()) {
                case R.id.ask_bot_from_qr:

                    BottomNavigationView nav = getActivity().findViewById(R.id.navigation);
                    nav.setSelectedItemId(R.id.navigation_bot);

                    Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag(Bot.class.getName());
                    if (prev == null){
                        modelView.setAskBot(true);
                        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                                .replace(R.id.mainContainer,new Bot())
                                .commit();

                    }else {
                        Bot bot = (Bot)prev;
                        bot.sendRequest("Hablame de " + Name);
                        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                                .replace(R.id.mainContainer,prev)
                                .commit();
                    }
                    break;
                case R.id.info_button_from_qr:
                    infoPersonaje infoPersonaje = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(infoPersonaje.class);
                    infoPersonaje.setName(Name);
                    BottomNavigationView nav2 = getActivity().findViewById(R.id.navigation);
                    nav2.setSelectedItemId(R.id.navigation_home);
                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainContainer,new PersonajeFragment())
                            .commit();


                    break;
                case R.id.go_wiki_from_qr:

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(modelView.getURL()));
                    startActivity(browserIntent);
                    break;
            }
        }
    };

}