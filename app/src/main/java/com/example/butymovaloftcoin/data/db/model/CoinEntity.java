package com.example.butymovaloftcoin.data.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.butymovaloftcoin.data.model.Fiat;

@Entity(tableName = "Coin")
public class CoinEntity implements Parcelable {
    @PrimaryKey
    public int id;

    public String name;

    public String symbol;

    public String added;

    public String updated;

    @Embedded(prefix = "usd_")
    public QuoteEntity usd;

    @Embedded(prefix = "eur_")
    public QuoteEntity eur;

    @Embedded(prefix = "rub_")
    public QuoteEntity rub;

    public CoinEntity(){

    }

    protected CoinEntity(Parcel in) {
        id = in.readInt();
        name = in.readString();
        symbol = in.readString();
        added = in.readString();
        updated = in.readString();
    }

    public static final Creator<CoinEntity> CREATOR = new Creator<CoinEntity>() {
        @Override
        public CoinEntity createFromParcel(Parcel in) {
            return new CoinEntity(in);
        }

        @Override
        public CoinEntity[] newArray(int size) {
            return new CoinEntity[size];
        }
    };

    public QuoteEntity getQuote(Fiat fiat){
        switch (fiat){
            case USD: return usd;
            case EUR: return eur;
            case RUB: return rub;
            default: return null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(symbol);
        dest.writeString(added);
        dest.writeString(updated);
    }
}
