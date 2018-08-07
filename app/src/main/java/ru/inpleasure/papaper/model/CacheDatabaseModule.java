package ru.inpleasure.papaper.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import dagger.Module;
import dagger.Provides;


@Module
public class CacheDatabaseModule extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "news.db";
    private static final int DATABASE_INIT_VERSION = 1;
    public static final String DB_TABLE_NAME = "article";

    public CacheDatabaseModule(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_INIT_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {
        database.execSQL("create table article (" +
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
    public CacheDatabaseModule getDatabaseHelper() {
        return this;
    }
}
