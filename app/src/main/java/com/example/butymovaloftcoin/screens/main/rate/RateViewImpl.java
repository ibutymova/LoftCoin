package com.example.butymovaloftcoin.screens.main.rate;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.butymovaloftcoin.R;
import com.example.butymovaloftcoin.data.db.model.CoinEntity;
import com.example.butymovaloftcoin.screens.main.rate.adapters.RateAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RateViewImpl implements RateView,
                                     Toolbar.OnMenuItemClickListener,
                                     SwipeRefreshLayout.OnRefreshListener,
                                     RateAdapter.Listener{

    static final String MENU_ITEM_NOTIFY_ON = "menu_item_notify_on";
    static final String MENU_ITEM_NOTIFY_OFF = "menu_item_notify_off";
    static final String MENU_ITEM_NOTIFY_SET_RATE = "menu_item_notify_set_rate";

    private View view;
    private RateAdapter adapter;

    @BindView(R.id.rate_content)
    FrameLayout content;

    @BindView(R.id.rate_recycler)
    RecyclerView recycler;

    @BindView(R.id.rate_refresh)
    SwipeRefreshLayout refresh;

    @BindView(R.id.rate_toolbar)
    Toolbar toolbar;

    @BindView(R.id.rate_progress)
    ViewGroup progress;

    private PublishSubject<Object> onMenuItemShareClickEvent = PublishSubject.create();
    private PublishSubject<Object> onMenuItemCurrencyClickEvent = PublishSubject.create();
    private PublishSubject<Object> onMenuItemNotifyClickEvent = PublishSubject.create();
    private PublishSubject<Object> onSwipeRefreshEvent = PublishSubject.create();
    private PublishSubject<String> onLongClickEvent = PublishSubject.create();
    private PublishSubject<Object> initEvent = PublishSubject.create();

    private Unbinder unbinder;

    @Inject
    public RateViewImpl(RateAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void attach(View view) {
        this.view = view;

        adapter.setListener(this);

        unbinder = ButterKnife.bind(this, view);
        refresh.setOnRefreshListener(this);

        toolbar.setTitle(R.string.rate_screen_title);
        toolbar.inflateMenu(R.menu.menu_rate);
        toolbar.setOnMenuItemClickListener(this);

        recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recycler.setHasFixedSize(true);
        recycler.setAdapter(adapter);
    }

    @Override
    public void initView() {
        initEvent.onNext(new Object());
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.rate_menu_item_share:{
                onMenuItemShareClickEvent.onNext(new Object());
                return true;
            }
            case R.id.rate_menu_item_currency: {
                onMenuItemCurrencyClickEvent.onNext(new Object());
                return true;
            }
            case R.id.rate_menu_item_notify: {
                onMenuItemNotifyClickEvent.onNext(new Object());
                return true;
            }
            default: return false;
        }
    }

    @Override
    public void onRefresh() {
        onSwipeRefreshEvent.onNext(new Object());
    }

    @Override
    public void setCoins(List<CoinEntity> coinEntityList) {
        adapter.setCoinEntityList(coinEntityList);
    }

    @Override
    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progress.setVisibility(View.GONE);
    }

    @Override
    public void setRefresh(boolean refreshing) {
        refresh.setRefreshing(refreshing);
    }

    @Override
    public void setNotificationsOn() {
        toolbar.getMenu().findItem(R.id.rate_menu_item_notify).setIcon(R.drawable.ic_notifications_on);
        toolbar.getMenu().findItem(R.id.rate_menu_item_notify).setTitle("");
    }

    @Override
    public void setNotificationsOff() {
        toolbar.getMenu().findItem(R.id.rate_menu_item_notify).setIcon(R.drawable.ic_notifications_off);
        toolbar.getMenu().findItem(R.id.rate_menu_item_notify).setTitle("");
    }

    @Override
    public void setNotificationsRate(String symbol) {
        toolbar.getMenu().findItem(R.id.rate_menu_item_notify).setIcon(null);
        toolbar.getMenu().findItem(R.id.rate_menu_item_notify).setTitle(symbol);
    }

    @Override
    public void detach() {
        adapter.setListener(null);
        recycler.setAdapter(null);

        this.view = null;
        unbinder.unbind();
    }

    @Override
    public Observable<Object> onMenuItemShareClickEvent() {
        return onMenuItemShareClickEvent;
    }

    @Override
    public Observable<Object> onMenuItemCurrencyClickEvent() {
        return onMenuItemCurrencyClickEvent;
    }

    @Override
    public Observable<Object> onSwipeRefreshEvent() {
        return onSwipeRefreshEvent;
    }

    @Override
    public Observable<Object> initEvent() {
        return initEvent;
    }

    @Override
    public Observable<Object> onMenuItemNotifyClickEvent() {
        return onMenuItemNotifyClickEvent;
    }

    @Override
    public Observable<String> onLongClickEvent() {
        return onLongClickEvent;
    }

    @Override
    public void onLongClick(String symbol) {
        onLongClickEvent.onNext(symbol);
    }
}
