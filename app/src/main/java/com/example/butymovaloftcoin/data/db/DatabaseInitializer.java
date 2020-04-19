package com.example.butymovaloftcoin.data.db;

import android.content.Context;

import androidx.room.Room;

import com.example.butymovaloftcoin.data.db.room.AppDatabase;
import com.example.butymovaloftcoin.data.db.room.DatabaseImplRoom;

public class DatabaseInitializer  {

    public Database init(Context context){
        AppDatabase appDatabase = Room
                .databaseBuilder(context, AppDatabase.class, "loftschool.db")
                .build();
        return new DatabaseImplRoom(appDatabase);
    }

}
