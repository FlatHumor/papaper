package ru.inpleasure.papaper.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import io.reactivex.Observable;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import ru.inpleasure.papaper.App;
import ru.inpleasure.papaper.IContract;
import ru.inpleasure.papaper.model.dbo.Article;


@Module
public class ModelModule implements IContract.IModel
{

    private DatabaseHelperModule helper;

    public ModelModule(Context context) {
        helper = new DatabaseHelperModule(context);
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
    public void saveArticle(Article article)
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
        database.insert(Article.class.getSimpleName(), null, cv);
        database.close();
    }

    @Provides
    public IContract.IModel getModel() {
        return this;
    }

}
