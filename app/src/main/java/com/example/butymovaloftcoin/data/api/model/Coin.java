package com.example.butymovaloftcoin.data.api.model;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class Coin {
    public int id;

    public String name;

    public String symbol;

    @SerializedName("last_updated")
    public String updated;

    @SerializedName("date_added")
    public String added;

    public Map<String, Quote> quote;
}
