package com.example.butymovaloftcoin.screens.welcome;

import com.example.butymovaloftcoin.data.prefs.Prefs;
import com.example.butymovaloftcoin.di.scopes.ActivityScope;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;

@ActivityScope
public class WelcomePresenterImpl implements WelcomePresenter {

    private Prefs prefs;
    private CompositeDisposable compositeDisposable;

    private WelcomeView view;
    private WelcomeRouter router;

    @Inject
    WelcomePresenterImpl(Prefs prefs,
                         CompositeDisposable compositeDisposable) {
        this.prefs = prefs;
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    public void attach(WelcomeView view, WelcomeRouter router) {
         this.view = view;
         this.router = router;

         compositeDisposable.add(view.onStartClickedEvent().subscribe(o -> {
             prefs.setFirstLaunch(false);
             router.startActivityStart();
         }));
    }

    @Override
    public void detach() {
        compositeDisposable.clear();
        if (view!=null){
            view.detach();
            view = null;
        }
        if (router!=null){
            router.detach();
            router = null;
        }
    }
}
