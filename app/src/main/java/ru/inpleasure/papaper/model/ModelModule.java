package ru.inpleasure.papaper.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import io.reactivex.Observable;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import ru.inpleasure.papaper.IContract;
import ru.inpleasure.papaper.model.dbo.Article;


@Module
public class ModelModule implements IContract.IModel
{
    private static final String CACHED_IMAGE_PREFIX = "cached_image_%s";
    private DatabaseHelperModule helper;
    private File cacheDirectory;

    public ModelModule(Context context) {
        helper = new DatabaseHelperModule(context);
        cacheDirectory = context.getCacheDir();
    }


    private List<Article> getArticleList()
    {
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor;
        try {
            cursor = database.query(Article.class.getSimpleName(),
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
    public Observable<Article> getArticles()
    {
        return Observable.fromIterable(getArticleList());
    }

    @Override
    public long putArticle(Article article)
    {
        ContentValues cv = new ContentValues();
        cv.put("source", article.getSource());
        cv.put("author", article.getAuthor());
        cv.put("title", article.getTitle());
        cv.put("description", article.getDescription());
        cv.put("url", article.getUrl());
        cv.put("urlToImage", article.getUrlToImage());
        cv.put("publishedAt", article.getPublishedAt());
        SQLiteDatabase database = helper.getWritableDatabase();
        long row = database.insert(Article.class.getSimpleName(), null, cv);
        database.close();
        return row;
    }

    @Override
    public Article getArticle(int position) {
        SQLiteDatabase database = helper.getReadableDatabase();
        String whereClause = "id = ?";
        String[] whereClauseArgs = {Integer.toString(position)};
        Cursor cursor;
        try {
            cursor = database.query(Article.class.getSimpleName(),
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
    public void clearAll() {
        SQLiteDatabase database = helper.getWritableDatabase();
        database.delete(Article.class.getSimpleName(), null, null);
        database.close();
        File[] cachedIllustrations = cacheDirectory.listFiles();
        for (File illustration : cachedIllustrations)
            illustration.delete();
    }

    @Override
    public Bitmap getArticleIllustration(int id)
    {
        File cachedIllustration = new File(cacheDirectory, String.format(CACHED_IMAGE_PREFIX, id));
        if (cachedIllustration.exists())
            return BitmapFactory.decodeFile(cachedIllustration.getAbsolutePath());
        return null;
    }

    @Override
    public void putArticleIllustration(int id, Bitmap bitmap)
    {
        File articleIllustration = new File(cacheDirectory, String.format(CACHED_IMAGE_PREFIX, id));
        try
        {
            FileOutputStream stream = new FileOutputStream(articleIllustration.getAbsolutePath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, stream);
            stream.flush();
            stream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Provides
    public IContract.IModel getModel() {
        return this;
    }

}
