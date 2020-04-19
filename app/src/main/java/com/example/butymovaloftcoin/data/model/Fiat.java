package com.example.butymovaloftcoin.data.model;

public enum Fiat  {

    EUR("€"),
    USD("$"),
    RUB("\u20BD");

    public String symbol;

    Fiat(String symbol) {
        this.symbol = symbol;
    }
}
