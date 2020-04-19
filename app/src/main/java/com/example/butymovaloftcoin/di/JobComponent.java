package com.example.butymovaloftcoin.di;

import com.example.butymovaloftcoin.di.scopes.ServiceScope;
import com.example.butymovaloftcoin.job.SyncRateJobPresenter;
import com.example.butymovaloftcoin.job.SyncRateJobRouter;
import com.example.butymovaloftcoin.job.SyncRateJobService;

import dagger.Component;

@ServiceScope
@Component(dependencies = {AppComponent.class}, modules = {JobModule.class})
public abstract class JobComponent {

    private static volatile JobComponent component;

    public static JobComponent createComponent(AppComponent appComponent){
        if (component==null)
            component = DaggerJobComponent
                    .builder()
                    .withAppComponent(appComponent)
                    .build();
        return component;
    }

    public static void clearComponent(){
        component=null;
    }

    public abstract void inject(SyncRateJobService service);

    public abstract SyncRateJobPresenter getSyncRateJobPresenter();
    public abstract SyncRateJobRouter getSyncRateJobRouter();

    @Component.Builder
    interface Builder{
        JobComponent build();
        JobComponent.Builder withAppComponent(AppComponent appComponent);
    }
}
