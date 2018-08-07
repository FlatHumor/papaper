package ru.inpleasure.papaper.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import io.reactivex.Observable;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import ru.inpleasure.papaper.App;
import ru.inpleasure.papaper.IContract;
import ru.inpleasure.papaper.model.dbo.Article;


@Module
public class CacheModel implements IContract.IModel
{
    public static final String FAVORITE = FavoriteDatabaseModule.DB_TABLE_NAME;
    public static final String CACHE = CacheDatabaseModule.DB_TABLE_NAME;
    protected CacheDatabaseModule cacheHelper;
    protected FavoriteDatabaseModule favoriteHelper;
    private File cacheDirectory;

    public CacheModel(Context context) {
        cacheDirectory = context.getCacheDir();
        cacheHelper = new CacheDatabaseModule(context);
        favoriteHelper = new FavoriteDatabaseModule(context);
    }


    private List<Article> getArticleList(String tableName)
    {
        SQLiteDatabase database;
        switch (tableName)
        {
            case FAVORITE:
                database = favoriteHelper.getReadableDatabase();
                break;
            case CACHE:
                database = cacheHelper.getReadableDatabase();
                break;
            default: return null;
        }
        Cursor cursor;
        try {
            cursor = database.query(tableName,
                    null, null, null,
                    null, null, null);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        List<Article> articleList = new ArrayList<>(cursor.getCount());
        if (cursor.moveToFirst())
        {
            do {
                Article article = new Article();
                article.setId(cursor.getInt(cursor.getColumnIndex("id")));
                article.setSource(cursor.getString(cursor.getColumnIndex("source")));
                article.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
                article.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                article.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                article.setUrl(cursor.getString(cursor.getColumnIndex("url")));
                article.setUrlToImage(cursor.getString(cursor.getColumnIndex("urlToImage")));
                article.setPublishedAt(cursor.getLong(cursor.getColumnIndex("publishedAt")));
                articleList.add(article);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return articleList;
    }

    @Override
    public Observable<Article> getArticles(String tableName) {
        return Observable.fromIterable(getArticleList(tableName));
    }

    @Override
    public long putArticle(Article article, String tableName)
    {
        SQLiteDatabase database;
        switch (tableName)
        {
            case FAVORITE:
                database = favoriteHelper.getWritableDatabase();
                break;
            case CACHE:
                database = cacheHelper.getWritableDatabase();
                break;
            default: return -1L;
        }
        ContentValues cv = new ContentValues();
        cv.put("source", article.getSource());
        cv.put("author", article.getAuthor());
        cv.put("title", article.getTitle());
        cv.put("description", article.getDescription());
        cv.put("url", article.getUrl());
        cv.put("urlToImage", article.getUrlToImage());
        cv.put("publishedAt", article.getPublishedAt());
        long row = database.insert(tableName, null, cv);
        database.close();
        return row;
    }

    @Override
    public Article getArticle(int position, String tableName) {
        SQLiteDatabase database;
        switch (tableName)
        {
            case FAVORITE:
                database = favoriteHelper.getReadableDatabase();
                break;
            case CACHE:
                database = cacheHelper.getReadableDatabase();
                break;
            default: return null;
        }
        String whereClause = "id = ?";
        String[] whereClauseArgs = {Integer.toString(position)};
        Cursor cursor;
        try {
            cursor = database.query(tableName,
                    null, whereClause, whereClauseArgs,
                    null, null, null);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Article article = null;
        if (cursor.moveToFirst())
        {
            article.setId(cursor.getInt(cursor.getColumnIndex("id")));
            article.setSource(cursor.getString(cursor.getColumnIndex("source")));
            article.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
            article.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            article.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            article.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            article.setUrlToImage(cursor.getString(cursor.getColumnIndex("urlToImage")));
            article.setPublishedAt(cursor.getLong(cursor.getColumnIndex("publishedAt")));
        }
        return article;
    }

    @Override
    public void removeArticle(int id, String tableName)
    {
        SQLiteDatabase database;
        switch (tableName)
        {
            case FAVORITE:
                database = favoriteHelper.getWritableDatabase();
                break;
            case CACHE:
                database = cacheHelper.getWritableDatabase();
                break;
            default: return;
        }
        String whereClause = "id = ?";
        String[] whereClauseArgs = {Integer.toString(id)};
        database.delete(tableName, whereClause, whereClauseArgs);
    }

    @Override
    public void clearAll() {
        SQLiteDatabase database = cacheHelper.getWritableDatabase();
        database.delete(Article.class.getSimpleName(), null, null);
        database.close();
    }

    @Provides
    public IContract.IModel getModel() {
        return this;
    }

}
