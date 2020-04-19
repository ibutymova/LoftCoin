package com.example.butymovaloftcoin.screens.main.wallets.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.example.butymovaloftcoin.R;
import com.example.butymovaloftcoin.data.db.model.QuoteEntity;
import com.example.butymovaloftcoin.data.db.model.WalletModel;
import com.example.butymovaloftcoin.data.model.Currency;
import com.example.butymovaloftcoin.data.model.Fiat;
import com.example.butymovaloftcoin.data.prefs.Prefs;
import com.example.butymovaloftcoin.di.scopes.FragmentScope;
import com.example.butymovaloftcoin.utils.CurrencyFormatter;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

@FragmentScope
public class WalletsPageAdapter extends PagerAdapter {

    private Random random;
    private Prefs prefs;
    private CurrencyFormatter currencyFormatter;
    private int[] colors;

    private List<WalletModel> wallets = Collections.emptyList();

    @Inject
    public WalletsPageAdapter(Random random, Prefs prefs, CurrencyFormatter currencyFormatter, int[] colors) {
        this.random = random;
        this.prefs = prefs;
        this.currencyFormatter = currencyFormatter;
        this.colors = colors;
    }

    public void setList(List<WalletModel> wallets) {
        this.wallets = wallets;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return wallets.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_wallet, container, false);
        WalletViewHolder walletViewHolder = new WalletViewHolder(view, random, prefs, currencyFormatter,colors);
        walletViewHolder.bind(wallets.get(position));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
       container.removeView((View) object);
    }

    static class WalletViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.symbol_text)
        TextView symbol;

        @BindView(R.id.symbol_icon)
        ImageView icon;

        @BindView(R.id.currency_name)
        TextView name;

        @BindView(R.id.secondary_amount)
        TextView secondaryAmount;

        @BindView(R.id.primary_amount)
        TextView primaryAmount;

        private Random random;
        private Prefs prefs;
        private CurrencyFormatter currencyFormatter;
        private int[] colors;

        WalletViewHolder(View itemView, Random random, Prefs prefs, CurrencyFormatter currencyFormatter, int[] colors) {
            super(itemView);

            this.colors = colors;
            this.random = random;
            this.currencyFormatter = currencyFormatter;
            this.prefs = prefs;

            ButterKnife.bind(this, itemView);
        }

        void bind(WalletModel model){
            bindSymbol(model);
            bindName(model);
            bindSecondaryAmount(model);
            bindPrimaryAmount(model);
        }

        private void bindSymbol(WalletModel model){
            Currency currency = Currency.getCurrency(model.coin.symbol);
            if (currency == null) {
                symbol.setVisibility(View.VISIBLE);
                icon.setVisibility(View.GONE);
                symbol.setText(String.valueOf(model.coin.symbol.charAt(0)));

                Drawable background = symbol.getBackground();
                Drawable wrapped = DrawableCompat.wrap(background);
                DrawableCompat.setTint(wrapped, colors[random.nextInt(colors.length)]);
            }
            else
            {
               symbol.setVisibility(View.GONE);
               icon.setVisibility(View.VISIBLE);
               icon.setImageResource(currency.iconRes);
            }
        }

        private void bindName(WalletModel model){
            name.setText(model.coin.symbol);
        }


        private void bindSecondaryAmount(WalletModel model){
            Fiat fiat = prefs.getFiatCurrency();
            QuoteEntity quoteEntity = model.coin.getQuote(fiat);


            double amount = model.wallet.amount * quoteEntity.price;
            String amountStr = currencyFormatter.format(amount, false);
            secondaryAmount.setText(itemView.getContext().getString(R.string.currency_amount, amountStr, fiat.symbol));
        }

        private void bindPrimaryAmount(WalletModel model){
            String amountStr = currencyFormatter.format(model.wallet.amount, true);
            primaryAmount.setText(itemView.getContext().getString(R.string.currency_amount, amountStr, model.coin.symbol));
        }
    }
}
