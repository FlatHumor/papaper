package ru.inpleasure.papaper.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import dagger.Module;
import dagger.Provides;

@Module
public class FavoriteDatabaseModule extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_INIT_VERSION = 1;
    public static final String DB_TABLE_NAME = "favorites";

    public FavoriteDatabaseModule(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_INIT_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {
        database.execSQL("create table favorites (" +
                "id integer primary key autoincrement," +
                "source text," +
                "author text," +
                "title text," +
                "description text," +
                "url text," +
                "urlToImage text," +
                "publishedAt real);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    @Provides
    public FavoriteDatabaseModule getDatabaseHelper() {
        return this;
    }
}
