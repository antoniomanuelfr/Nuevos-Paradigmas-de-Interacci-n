package com.p207.npi.Museo;

    import android.os.Bundle;
    import android.support.annotation.NonNull;
    import android.support.v4.app.Fragment;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Empty
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

}
