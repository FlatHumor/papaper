package ru.inpleasure.papaper.presenter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.inpleasure.papaper.App;
import ru.inpleasure.papaper.IContract;
import ru.inpleasure.papaper.api.dto.NewsDto;
import ru.inpleasure.papaper.model.CacheModel;
import ru.inpleasure.papaper.model.dbo.Article;


public class ArticlePresenter implements IContract.IPresenter
{
    public static final String CATEGORY_BUSINESS = "business";
    public static final String CATEGORY_ENTERTAINMENT = "entertainment";
    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_HEALTH = "health";
    public static final String CATEGORY_SCIENCE = "science";
    public static final String CATEGORY_SPORTS = "sports";
    public static final String CATEGORY_TECHNOLOGY = "technology";

    private static final String TOKEN = "00";

    private IContract.IView newsActivity;
    @Inject
    protected IContract.IModel model;
    @Inject
    protected IContract.IApi api;

    public ArticlePresenter(IContract.IView view) {
        newsActivity = view;
        App.getComponent().inject(this);
    }

    @Override
    @SuppressLint("CheckResult")
    public void onCreate() {
        model.getArticles(CacheModel.CACHE)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(newsActivity::showArticle);
        model.clearAll();
    }

    @Override
    @SuppressLint("CheckResult")
    public void onCategoryChanged(String category) {
        api.getTopHeadliners(category, TOKEN, "ru")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(newsDto -> {
                    newsActivity.clearArticles();
                    model.clearAll();
                    for (NewsDto.ArticleDto articleDto : newsDto.getArticleDtoList()) {
                        Article article = Article.createFromDto(articleDto);
                        article.setId((int)model.putArticle(article, CacheModel.CACHE));
                        newsActivity.showArticle(article);
                    }
                });
    }

    @Override
    public void onClickSaveButton(Article article) {
        model.putArticle(article, CacheModel.FAVORITE);
        Toast.makeText(newsActivity.getContext(), "Статья добавлена в Избранное",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickShareButton(Article article) {
        new Thread(() -> {
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Picasso.with(newsActivity.getContext())
                        .load(article.getUrlToImage())
                        .resize(500, 500)
                        .get().compress(Bitmap.CompressFormat.JPEG, 70, stream);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_STREAM, stream.toByteArray());
                newsActivity.getContext().startActivity(intent);
                stream.flush();
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void onClickLinkButton(Article article) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, article.getUrl());
        newsActivity.getContext().startActivity(intent);
    }

    @Override
    @SuppressLint("CheckResult")
    public void onClickFavoriteButton() {
        newsActivity.clearArticles();
        model.getArticles(CacheModel.FAVORITE)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(newsActivity::showArticle);
    }
}
