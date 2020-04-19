package com.example.butymovaloftcoin.screens.main.rate.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.butymovaloftcoin.R;
import com.example.butymovaloftcoin.data.db.model.CoinEntity;
import com.example.butymovaloftcoin.data.db.model.QuoteEntity;
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
public class RateAdapter extends RecyclerView.Adapter<RateAdapter.RateViewHolder> {

    private List<CoinEntity> coinEntityList = Collections.emptyList();
    private Listener listener;

    private Random random;
    private Prefs prefs;
    private CurrencyFormatter currencyFormatter;
    private int[] colors;

    @Inject
    public RateAdapter(Random random, Prefs prefs, CurrencyFormatter currencyFormatter, int[] colors) {
        this.random = random;
        this.prefs = prefs;
        this.currencyFormatter = currencyFormatter;
        this.colors = colors;
    }

    public void setCoinEntityList(List<CoinEntity> coinEntityList) {
        this.coinEntityList = coinEntityList;
        notifyDataSetChanged();
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public RateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rate, parent, false);
        return new RateViewHolder(view, listener, random, prefs, currencyFormatter, colors);
    }

    @Override
    public void onBindViewHolder(@NonNull RateViewHolder holder, int position) {
        holder.bind(coinEntityList.get(position), position);
    }

    @Override
    public long getItemId(int position) {
        return coinEntityList.get(position).id;
    }

    @Override
    public int getItemCount() {
        return coinEntityList.size();
    }

    static class RateViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.symbol_text)
        TextView symbolText;

        @BindView(R.id.symbol_icon)
        ImageView symbolIcon;

        @BindView(R.id.currency_name)
        TextView name;

        @BindView(R.id.price)
        TextView price;

        @BindView(R.id.percent_change)
        TextView percentChange;

        private Listener listener;
        private Random random;
        private Prefs prefs;
        private CurrencyFormatter currencyFormatter;
        private int[] colors;

        RateViewHolder(View itemView, Listener listener, Random random, Prefs prefs, CurrencyFormatter currencyFormatter, int[] colors) {
            super(itemView);

            this.listener = listener;
            this.random = random;
            this.prefs = prefs;
            this.currencyFormatter = currencyFormatter;
            this.colors = colors;

            ButterKnife.bind(this, itemView);
        }

        private void bind(CoinEntity coinEntity, int position){
            if (coinEntity == null) return;
            bindName(coinEntity);
            bindSymbol(coinEntity);
            bindPrice(coinEntity);
            bindPercentange(coinEntity);
            bindBackground(position);
            bindListener(coinEntity);
        }

        private void bindName(CoinEntity coinEntity){
           name.setText(coinEntity.symbol);
        }

        private void bindSymbol(CoinEntity coinEntity){
            Currency currency = Currency.getCurrency(coinEntity.symbol);
            if (currency != null){
                symbolIcon.setVisibility(View.VISIBLE);
                symbolText.setVisibility(View.GONE);
                symbolIcon.setImageResource(currency.iconRes);
            }
            else
            {
                symbolIcon.setVisibility(View.GONE);
                symbolText.setVisibility(View.VISIBLE);
                symbolText.setText(String.valueOf(coinEntity.symbol.charAt(0)));

                Drawable background = symbolText.getBackground();
                Drawable wrap = DrawableCompat.wrap(background);
                DrawableCompat.setTint(wrap, colors[random.nextInt(colors.length)]);
            }
        }

        private void bindPrice(CoinEntity coinEntity){
            Fiat fiat = prefs.getFiatCurrency();
            QuoteEntity quoteEntity = coinEntity.getQuote(fiat);
            String priceValue = currencyFormatter.format(quoteEntity.price, false);
            price.setText(itemView.getContext().getString(R.string.currency_price, priceValue, fiat.symbol));
        }

        private void bindPercentange(CoinEntity coinEntity){
            Fiat fiat = prefs.getFiatCurrency();
            QuoteEntity quoteEntity = coinEntity.getQuote(fiat);
            Float percentChangeValue = quoteEntity.percentChange24h;
            percentChange.setText(itemView.getContext().getString(R.string.rate_item_percent_change, percentChangeValue));

            if (percentChangeValue > 0)
                percentChange.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.percent_change_up));
            else
                percentChange.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.percent_change_down));
        }

        private void bindBackground(int position){
            if (position%2 == 0)
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.rate_item_background_even));
            else
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.rate_item_background_odd));
        }

        private void bindListener(CoinEntity coinEntity){
            itemView.setOnLongClickListener(v -> {
                if (listener!=null){
                    listener.onLongClick(coinEntity.symbol);
                    return true;
                }
                else
                    return false;
            });
        }
    }

    public interface Listener{
        void onLongClick(String symbol);
    }
}
