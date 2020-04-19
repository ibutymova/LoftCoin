package com.example.butymovaloftcoin.data.model;

import androidx.annotation.DrawableRes;
import com.example.butymovaloftcoin.R;

public enum Currency {
  BTC(R.drawable.ic_btc),
  ETH(R.drawable.ic_eth),
  XRP(R.drawable.ic_xrp),
  XMR(R.drawable.ic_xmr),
  DOGE(R.drawable.ic_doge),
  DASH(R.drawable.ic_dash);

  @DrawableRes
  public int iconRes;

  Currency(int iconRes) {
    this.iconRes = iconRes;
  }

  public static Currency getCurrency(String currency) {
    try {
      return Currency.valueOf(currency);
    }
    catch (IllegalArgumentException e) {
      return null;
    }
  }
}
