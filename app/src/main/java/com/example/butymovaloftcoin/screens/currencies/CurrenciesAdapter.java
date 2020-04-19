package com.example.butymovaloftcoin.screens.currencies;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.butymovaloftcoin.R;
import com.example.butymovaloftcoin.data.db.model.CoinEntity;
import com.example.butymovaloftcoin.data.model.Currency;
import com.example.butymovaloftcoin.di.scopes.FragmentScope;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

@FragmentScope
public class CurrenciesAdapter extends RecyclerView.Adapter<CurrenciesAdapter.CurrencyViewHolder> {

    private List<CoinEntity> coinEntityList = Collections.emptyList();
    private Listener listener;

    private Random random;
    private int[] colors;

    @Inject
    public CurrenciesAdapter(Random random, int[] colors) {
        this.random = random;
        this.colors = colors;
    }

    void setCoinEntityList(List<CoinEntity> coinEntityList) {
        this.coinEntityList = coinEntityList;
        notifyDataSetChanged();
    }

    void setListener(Listener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public CurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_item, parent, false);
        return new CurrencyViewHolder(view, random, colors);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyViewHolder holder, int position) {
        holder.bind(coinEntityList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return coinEntityList.size();
}

    static class CurrencyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.item_currency_symbol_icon)
        ImageView icon;

        @BindView(R.id.item_currency_symbol_text)
        TextView text;

        @BindView(R.id.item_currency_name)
        TextView name;

        private Random random;
        private int[] colors;

        public CurrencyViewHolder(View itemView, Random random, int[] colors) {
            super(itemView);
            this.colors = colors;
            this.random = random;
            ButterKnife.bind(this, itemView);
        }

        void bind(CoinEntity coinEntity, Listener listener){
            bindIcon(coinEntity);
            bindName(coinEntity);
            itemView.setOnClickListener(v -> {
                if (listener != null)
                    listener.onCurrencyClick(coinEntity);
            });
        }

        private void bindIcon(CoinEntity coinEntity){
            Currency currency = Currency.getCurrency(coinEntity.symbol);
            if (currency != null){
                icon.setVisibility(View.VISIBLE);
                text.setVisibility(View.GONE);
                icon.setImageResource(currency.iconRes);

            }
            else {
                icon.setVisibility(View.GONE);
                text.setVisibility(View.VISIBLE);

                Drawable background = text.getBackground();
                Drawable wrapped = DrawableCompat.wrap(background);
                DrawableCompat.setTint(wrapped, colors[random.nextInt(colors.length)]);
                text.setText(String.valueOf(coinEntity.symbol.charAt(0)));
            }
        }

        private void bindName(CoinEntity coinEntity){
            name.setText(itemView.getContext().getString(R.string.currencies_bottom_sheet_currency_name, coinEntity.symbol, coinEntity.name));
        }
    }

    public interface Listener {
        void onCurrencyClick(CoinEntity coinEntity);
    }
}
