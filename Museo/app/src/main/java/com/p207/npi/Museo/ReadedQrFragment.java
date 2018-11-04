package com.p207.npi.Museo;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReadedQrFragment extends Fragment implements View.OnClickListener{


    public ReadedQrFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_qr_readed, container, false);

        ModelInfoQr modelView = ViewModelProviders.of(getActivity()).get(ModelInfoQr.class);

        String name = modelView.getName();

        String imageName = name.replace(' ','_').toLowerCase();

        int imageRes = getResources().getIdentifier(imageName,"drawable",getActivity().getPackageName());

        TextView ReadedName = vista.findViewById(R.id.readed_name_from_qr);
        ImageView Image = vista.findViewById(R.id.image_readed_from_qr);
        Button AskBotButton = vista.findViewById(R.id.ask_bot_from_qr);
        Button InfoButton = vista.findViewById(R.id.info_button_from_qr);
        Button WikiButton = vista.findViewById(R.id.go_wiki_from_qr);

        ReadedName.setText(name);
        Image.setImageResource(imageRes);

        AskBotButton.setOnClickListener(this);
        InfoButton.setOnClickListener(this);
        WikiButton.setOnClickListener(this);

        return vista;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.ask_bot_from_qr:

            case R.id.info_button_from_qr:

            case R.id.go_wiki_from_qr:
                ModelInfoQr modelView = ViewModelProviders.of(getActivity()).get(ModelInfoQr.class);


                Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(modelView.getURL()));
                startActivity(browserIntent);


        }

    }
}
