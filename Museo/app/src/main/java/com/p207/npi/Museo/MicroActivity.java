package com.p207.npi.Museo;

public class MicroActivity extends BaseActivity {

    @Override
    int getContentViewId(){
        return R.layout.micro_layout;
    }
    @Override
    int getNavigationMenuItemId(){
        return R.id.navigation_micro;
    }
    @Override
    void initActivity(){}
}
