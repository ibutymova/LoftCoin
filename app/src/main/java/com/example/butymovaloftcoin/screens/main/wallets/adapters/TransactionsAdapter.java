package com.example.butymovaloftcoin.screens.main.wallets.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.butymovaloftcoin.R;
import com.example.butymovaloftcoin.data.db.model.QuoteEntity;
import com.example.butymovaloftcoin.data.db.model.TransactionModel;
import com.example.butymovaloftcoin.data.model.Fiat;
import com.example.butymovaloftcoin.data.prefs.Prefs;
import com.example.butymovaloftcoin.di.scopes.FragmentScope;
import com.example.butymovaloftcoin.utils.CurrencyFormatter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import static java.lang.Math.abs;

@FragmentScope
public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder> {

    private Prefs prefs;
    private CurrencyFormatter currencyFormatter;
    private SimpleDateFormat simpleDateFormat;
    private List<TransactionModel> transactions = Collections.emptyList();

    @Inject
    public TransactionsAdapter(Prefs prefs, CurrencyFormatter currencyFormatter, SimpleDateFormat simpleDateFormat) {
        this.prefs = prefs;
        this.currencyFormatter = currencyFormatter;
        this.simpleDateFormat = simpleDateFormat;
        this.setHasStableIds(true);
    }

    public void setList(List<TransactionModel> transactions){
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view, prefs, currencyFormatter, simpleDateFormat);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
      holder.bind(transactions.get(position));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    static class  TransactionViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.transaction_crypto_amount)
        TextView cryptoAmount;

        @BindView(R.id.transaction_fiat_amount)
        TextView fiatAmount;

        @BindView(R.id.transaction_date)
        TextView date;

        @BindView(R.id.transaction_icon)
        ImageView icon;

        private Prefs prefs;
        private CurrencyFormatter currencyFormatter;
        private SimpleDateFormat simpleDateFormat;

        TransactionViewHolder(View itemView, Prefs prefs, CurrencyFormatter currencyFormatter, SimpleDateFormat simpleDateFormat) {
            super(itemView);

            this.prefs = prefs;
            this.currencyFormatter = currencyFormatter;
            this.simpleDateFormat = simpleDateFormat;

            ButterKnife.bind(this, itemView);
        }

        public void bind(TransactionModel model){
            bindIcon(model);
            bindDate(model);
            bindCryptoAmount(model);
            bindFiatAmount(model);
        }

        private void bindIcon(TransactionModel model){
            if (model.transaction.amount < 0)
                icon.setImageResource(R.drawable.ic_transaction_expense);
            else
                icon.setImageResource(R.drawable.ic_transaction_income);
        }

        private void bindDate(TransactionModel model){
          Date date = new Date(model.transaction.date);
          this.date.setText(simpleDateFormat.format(date));
        }

        private void bindCryptoAmount(TransactionModel model){
            String valueAmount;

            if (model.transaction.amount < 0)
                valueAmount = '-' + currencyFormatter.format(abs(model.transaction.amount), true);
            else
                valueAmount = '+' + currencyFormatter.format(abs(model.transaction.amount), true);
            cryptoAmount.setText(itemView.getContext().getString(R.string.currency_amount, valueAmount, model.coin.symbol));
        }

        private void bindFiatAmount(TransactionModel model){
            Fiat fiat = prefs.getFiatCurrency();
            QuoteEntity quoteEntity = model.coin.getQuote(fiat);
            double amount;
            String amountStr;

            if (model.transaction.amount < 0){
                fiatAmount.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.transaction_expense));
                amount = abs(model.transaction.amount) * quoteEntity.price;
                amountStr = '-' + currencyFormatter.format(amount, false);
                fiatAmount.setText(itemView.getContext().getString(R.string.currency_amount, amountStr, fiat.symbol));
            }
            else {
                fiatAmount.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.transaction_income));
                amount = abs(model.transaction.amount) * quoteEntity.price;
                amountStr = '+' + currencyFormatter.format(amount, false);
                fiatAmount.setText(itemView.getContext().getString(R.string.currency_amount, amountStr, fiat.symbol));
            }
        }
    }
}
