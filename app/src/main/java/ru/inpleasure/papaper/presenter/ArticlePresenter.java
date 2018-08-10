package ru.inpleasure.papaper.presenter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

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

    public static final String EXTRA_ARTICLE_URL = "EXTRA_ARTICLE_URL";

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

    @Override
    public void onClickMoreButton(String sourceUrl) {
        Uri uri = Uri.parse(sourceUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        newsActivity.getContext().startActivity(intent);

        /*
        Intent intent = new Intent(newsActivity.getContext(), ArticleActivity.class);
        intent.putExtra(EXTRA_ARTICLE_URL, sourceUrl);
        newsActivity.getContext().startActivity(intent);
        */
    }

    @Override
    @SuppressLint("CheckResult")
    public void onClickSearchButton(String searchText)
    {
        api.getSearchResult(searchText, TOKEN, "ru")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(newsDto -> {
                    newsActivity.clearArticles();
                    for (NewsDto.ArticleDto articleDto : newsDto.getArticleDtoList())
                        newsActivity.showArticle(Article.createFromDto(articleDto));
                });
    }
}
