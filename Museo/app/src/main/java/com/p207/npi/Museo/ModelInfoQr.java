package com.p207.npi.Museo;

import android.arch.lifecycle.ViewModel;

public class ModelInfoQr extends ViewModel {

    private String URL;
    private String Name;


    public String getName() {
        return Name;
    }

    public String getURL() {
        return URL;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
