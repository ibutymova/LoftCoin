package com.example.butymovaloftcoin.screens.launch;

import com.example.butymovaloftcoin.data.prefs.Prefs;
import com.example.butymovaloftcoin.di.scopes.ActivityScope;

import javax.inject.Inject;

@ActivityScope
public class LaunchPresenterImpl implements LaunchPresenter {

    private Prefs prefs;
    private LaunchRouter router;

    @Inject
    public LaunchPresenterImpl(Prefs prefs) {
        this.prefs = prefs;
    }

    @Override
    public void attach(LaunchRouter router) {
        this.router = router;

        if (prefs.isFirstLaunch()) {
            router.StartActivityWelcome();
            prefs.setFirstLaunch(true);
        }
        else
            router.startActivityStart();
    }

    @Override
    public void detach() {
        if (router!=null) {
            router.detach();
            router = null;
        }
    }
}
