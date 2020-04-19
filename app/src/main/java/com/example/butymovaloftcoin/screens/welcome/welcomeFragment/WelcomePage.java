package com.example.butymovaloftcoin.screens.welcome.welcomeFragment;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

public class WelcomePage implements Parcelable {

    @DrawableRes
    private int icon;
    @StringRes
    private int title;
    @StringRes
    private  int subtitle;

    WelcomePage(int icon, int title, int subtitle) {
        this.icon = icon;
        this.title = title;
        this.subtitle = subtitle;
    }

    private WelcomePage(Parcel in) {
        icon = in.readInt();
        title = in.readInt();
        subtitle = in.readInt();
    }

    public static final Creator<WelcomePage> CREATOR = new Creator<WelcomePage>() {
        @Override
        public WelcomePage createFromParcel(Parcel in) {
            return new WelcomePage(in);
        }

        @Override
        public WelcomePage[] newArray(int size) {
            return new WelcomePage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(icon);
        dest.writeInt(title);
        dest.writeInt(subtitle);
    }


    public int getIcon() {
        return icon;
    }

    public int getTitle() {
        return title;
    }

    public int getSubtitle() {
        return subtitle;
    }
}
