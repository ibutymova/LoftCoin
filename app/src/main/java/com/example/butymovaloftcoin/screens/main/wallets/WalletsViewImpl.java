package com.example.butymovaloftcoin.screens.main.wallets;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.butymovaloftcoin.R;
import com.example.butymovaloftcoin.data.db.model.TransactionModel;
import com.example.butymovaloftcoin.data.db.model.WalletModel;
import com.example.butymovaloftcoin.screens.main.wallets.adapters.TransactionsAdapter;
import com.example.butymovaloftcoin.screens.main.wallets.adapters.WalletsPageAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class WalletsViewImpl implements WalletsView,
                                        View.OnClickListener,
                                        MenuItem.OnMenuItemClickListener {

    private View view;
    private TransactionsAdapter adapter;
    private WalletsPageAdapter pageAdapter;

    private PublishSubject<Object> onNewWalletClickEvent = PublishSubject.create();
    private PublishSubject<Integer> onWalletChangePositionEvent = PublishSubject.create();
    private PublishSubject<Integer> onRemoveWalletClickEvent = PublishSubject.create();
    private PublishSubject<Bundle> initEvent = PublishSubject.create();

    @BindView(R.id.wallets_pager)
    ViewPager pager;

    @BindView(R.id.wallets_toolbar)
    Toolbar toolbar;

    @BindView(R.id.new_wallet)
    ViewGroup newWallets;

    @BindView(R.id.transaction_recycler)
    RecyclerView recycler;

    private Unbinder unbinder;

    @Inject
    WalletsViewImpl(TransactionsAdapter adapter, WalletsPageAdapter pageAdapter) {
        this.adapter = adapter;
        this.pageAdapter = pageAdapter;
    }

    @Override
    public void attach(View view) {
        this.view = view;

        unbinder = ButterKnife.bind(this, view);

        toolbar.setTitle(R.string.wallets_screen_title);
        toolbar.inflateMenu(R.menu.menu_wallets);

        recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recycler.setHasFixedSize(true);
        recycler.setAdapter(adapter);

        int screenWidth = getScreenWidth();
        int walletItemWidth = view.getResources().getDimensionPixelOffset(R.dimen.item_wallet_width);
        int walletItemMargin = view.getResources().getDimensionPixelOffset(R.dimen.item_wallet_margin);
        int pageMargin = screenWidth - walletItemWidth - walletItemMargin;

        pager.setPageMargin(-pageMargin);
        pager.setOffscreenPageLimit(5);
        pager.setAdapter(pageAdapter);
        pager.setPageTransformer(false, new ZoomOutPageTransformer());

        newWallets.setOnClickListener(this);
        toolbar.getMenu().findItem(R.id.menu_item_add_wallet).setOnMenuItemClickListener(this);
        toolbar.getMenu().findItem(R.id.menu_item_remove_wallet).setOnMenuItemClickListener(this);

        ViewPager.SimpleOnPageChangeListener simpleOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                onWalletChangePositionEvent.onNext(position);
            }
        };
        pager.addOnPageChangeListener(simpleOnPageChangeListener);

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initEvent.onNext(savedInstanceState==null ? new Bundle() : savedInstanceState);
    }

    @Override
    public void detach() {
        view = null;
        recycler.setAdapter(null);
        pager.setAdapter(null);
        unbinder.unbind();
    }

    private int getScreenWidth(){
        WindowManager windowManager = (WindowManager) view.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return width;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.new_wallet:{
                onNewWalletClickEvent.onNext(new Object());
            }
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_add_wallet:{
                onNewWalletClickEvent.onNext(new Object());
                return true;
            }
            case R.id.menu_item_remove_wallet:{
                onRemoveWalletClickEvent.onNext(pager.getCurrentItem());
                return true;
            }
            default: return false;
        }
    }

    public static class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(@NonNull View view, float position) {
            if (position < -1) {
                view.setAlpha(0);

            } else if (position <= 1) {
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            } else {
                view.setAlpha(0);
            }
        }
    }

    @Override
    public void setPageAdapterList(List<WalletModel> walletModels) {
        pageAdapter.setList(walletModels);
    }

    @Override
    public void setAdapterList(List<TransactionModel> transactionModels) {
        adapter.setList(transactionModels);
    }

    @Override
    public void setWalletsVisible(Boolean isVisible) {
        pager.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setNewWalletVisible(Boolean isVisible) {
        newWallets.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void moveLastWallet(List<WalletModel> walletModels) {
        pager.setCurrentItem(walletModels.size()-1, true);
    }

    @Override
    public void setMenuItemRemoveVisible(Boolean isVisible) {
        toolbar.getMenu().findItem(R.id.menu_item_remove_wallet).setVisible(isVisible);
    }

    @Override
    public void setCurrentWallet() {
        onWalletChangePositionEvent.onNext(pager.getCurrentItem());
    }

    @Override
    public void setCurrentWallet(Integer walletPosition) {
        if (walletPosition!=pager.getCurrentItem())
            pager.setCurrentItem(walletPosition);
        else
            setCurrentWallet();
    }

    @Override
    public Observable<Object> onNewWalletClickEvent() {
        return onNewWalletClickEvent;
    }

    @Override
    public Observable<Integer> onRemoveWalletClickEvent() {
        return onRemoveWalletClickEvent;
    }

    @Override
    public Observable<Integer> onWalletChangePositionEvent() {
        return onWalletChangePositionEvent;
    }

    @Override
    public PublishSubject<Bundle> initEvent() {
        return initEvent;
    }

}
