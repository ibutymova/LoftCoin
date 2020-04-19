package com.example.butymovaloftcoin.di;

import com.example.butymovaloftcoin.di.scopes.ActivityScope;
import com.example.butymovaloftcoin.screens.welcome.WelcomePresenter;
import com.example.butymovaloftcoin.screens.welcome.WelcomeRouter;
import com.example.butymovaloftcoin.screens.welcome.WelcomeView;

import dagger.Component;

@Component(dependencies = {AppComponent.class}, modules = {WelcomeModule.class})
@ActivityScope
public abstract class WelcomeComponent {

    private static volatile WelcomeComponent component;

    public static WelcomeComponent createComponent(AppComponent appComponent){
        if (component==null){
            component = DaggerWelcomeComponent
                    .builder()
                    .withAppComponent(appComponent)
                    .build();
        }

        return component;
    }

    public static void clearComponent(){
        component=null;
    }

    public abstract WelcomePresenter getWelcomePresenter();
    public abstract WelcomeRouter getWelcomeRouter();
    public abstract WelcomeView getWelcomeView();

    @Component.Builder
    interface Builder {
        WelcomeComponent build();
        WelcomeComponent.Builder withAppComponent(AppComponent appComponent);
    }
}
