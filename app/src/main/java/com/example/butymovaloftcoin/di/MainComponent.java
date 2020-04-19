package com.example.butymovaloftcoin.di;

import com.example.butymovaloftcoin.di.scopes.ActivityScope;
import com.example.butymovaloftcoin.screens.main.MainPresenter;
import com.example.butymovaloftcoin.screens.main.MainRouter;
import com.example.butymovaloftcoin.screens.main.MainView;

import dagger.Component;

@Component(dependencies = {AppComponent.class}, modules = {MainModule.class})
@ActivityScope
public abstract class MainComponent {

    private static volatile MainComponent component;

    public static MainComponent createComponent(AppComponent appComponent) {
        if (component == null) {
            synchronized (MainComponent.class) {
                if (component == null) {
                    component = DaggerMainComponent
                            .builder()
                            .withAppComponent(appComponent)
                            .build();
                }
            }
        }
        return component;
    }

    public static void clearComponent(){
        component=null;
    }

    public abstract MainPresenter getMainPresenter();
    public abstract MainRouter getMainRouter();
    public abstract MainView getMainView();

    @Component.Builder
    interface Builder{
        MainComponent build();
        MainComponent.Builder withAppComponent(AppComponent appComponent);
    }
}
