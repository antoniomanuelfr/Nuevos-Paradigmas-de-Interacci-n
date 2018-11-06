package com.p207.npi.Museo;

import android.arch.lifecycle.ViewModel;

 class ModelInfoQr extends ViewModel {

    private String URL;
    private String Name;


    String getName() {
        return Name;
    }

    String getURL() {
        return URL;
    }

    void setName(String name) {
        Name = name;
    }

    void setURL(String URL) {
        this.URL = URL;
    }
}
