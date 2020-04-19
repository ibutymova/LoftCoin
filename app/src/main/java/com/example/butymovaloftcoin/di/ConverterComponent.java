package com.example.butymovaloftcoin.di;

import com.example.butymovaloftcoin.di.scopes.FragmentScope;
import com.example.butymovaloftcoin.screens.main.converter.ConverterPresenter;
import com.example.butymovaloftcoin.screens.main.converter.ConverterRouter;
import com.example.butymovaloftcoin.screens.main.converter.ConverterView;

import dagger.Component;

@FragmentScope
@Component(dependencies = {AppComponent.class}, modules = {ConverterModule.class})
public abstract class ConverterComponent {
    private static volatile ConverterComponent component;

    public static ConverterComponent createComponent(AppComponent appComponent){
        if (component==null){
            component = DaggerConverterComponent
                    .builder()
                    .withAppComponent(appComponent)
                    .build();
        }
        return component;
    }

    public static void clearComponent(){
        component=null;
    }

    public abstract ConverterPresenter getConverterPresenter();
    public abstract ConverterRouter getConverterRouter();
    public abstract ConverterView getConverterView();

    @Component.Builder
    interface Builder{
        ConverterComponent build();
        ConverterComponent.Builder withAppComponent(AppComponent appComponent);
    }
}
