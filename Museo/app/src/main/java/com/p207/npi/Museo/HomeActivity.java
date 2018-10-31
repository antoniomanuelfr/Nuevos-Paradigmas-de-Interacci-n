package com.p207.npi.Museo;

public class HomeActivity extends BaseActivity{

    @Override
    int getContentViewId(){
        return R.layout.home_layout;
    }
    @Override
    int getNavigationMenuItemId(){
        return R.id.navigation_home;
    }
}
