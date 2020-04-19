package com.example.butymovaloftcoin.screens.currencies;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.butymovaloftcoin.R;
import com.example.butymovaloftcoin.data.db.model.CoinEntity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class CurrenciesBottomSheetViewImpl implements CurrenciesBottomSheetView, CurrenciesAdapter.Listener {

    private CurrenciesAdapter adapter;
    private CurrenciesBottomSheetView.Listener listener;
    private PublishSubject<Object> initEvent = PublishSubject.create();

    @BindView(R.id.currency_recycler)
    RecyclerView recycler;

    private Unbinder unbinder;

    @Inject
    public CurrenciesBottomSheetViewImpl(CurrenciesAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void attach(View view, Listener listener) {
        this.listener = listener;

        unbinder = ButterKnife.bind(this, view);

        adapter.setListener(this);
        recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recycler.setHasFixedSize(true);
        recycler.setAdapter(adapter);
    }

    @Override
    public void initView() {
        initEvent.onNext(new Object());
    }

    @Override
    public void detach() {
        adapter.setListener(null);
        recycler.setAdapter(null);

        unbinder.unbind();
        if (listener!=null)
            listener = null;
    }

    @Override
    public Observable<Object> initEvent() {
        return initEvent;
    }

    @Override
    public void onCurrencyClick(CoinEntity coinEntity) {
        if (listener!=null)
            listener.onCurrencySelected(coinEntity);
    }

    @Override
    public void setAdapterList(List<CoinEntity> coinEntitis) {
        adapter.setCoinEntityList(coinEntitis);
    }
}
