package com.example.butymovaloftcoin.di;

import com.example.butymovaloftcoin.di.scopes.ActivityScope;
import com.example.butymovaloftcoin.screens.launch.LaunchPresenter;
import com.example.butymovaloftcoin.screens.launch.LaunchRouter;

import dagger.Component;

@ActivityScope
@Component(dependencies = {AppComponent.class}, modules = {LaunchModule.class})
public abstract class LaunchComponent {

    private static volatile LaunchComponent component;


    public static LaunchComponent createComponent(AppComponent appComponent){
        if (component==null)
            component = DaggerLaunchComponent
                    .builder()
                    .withAppComponent(appComponent)
                    .build();
        return component;
    }

    public static void clearComponent(){
        component=null;
    }

    public abstract LaunchPresenter getLaunchPresenter();
    public abstract LaunchRouter getLaunchRouter();

    @Component.Builder
    interface Builder{
        LaunchComponent build();
        LaunchComponent.Builder withAppComponent(AppComponent appComponent);
    }
}
