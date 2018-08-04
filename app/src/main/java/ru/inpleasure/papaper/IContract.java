package ru.inpleasure.papaper;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import io.reactivex.Observable;
import retrofit2.http.Query;
import ru.inpleasure.papaper.api.dto.NewsDto;
import ru.inpleasure.papaper.api.dto.NewsSourceDto;
import ru.inpleasure.papaper.model.dbo.Article;

public interface IContract
{
    interface IApi
    {
        @GET("https://newsapi.org/v2/top-headlines")
        Observable<NewsDto> getTopHeadliners(
                @Query("category") String category,
                @Query("apiKey") String apiKey,
                @Query("country") String country);

        @GET("https://newsapi.org/v2/everything")
        Observable<NewsDto>  getSerachResult(
                @Query("q") String keyword,
                @Query("apiKey") String apiKey,
                @Query("language") String language);

        @GET("https://newsapi.org/v2/sources")
        Observable<NewsSourceDto> getNewsSources(
                @Query("category") String category,
                @Query("apiKey") String apiKey,
                @Query("country") String country);
    }

    interface IModel
    {
        Observable<Article> getArticles();
        void saveArticle(Article article);
    }

    interface IView
    {

    }

    interface IPresenter
    {
        void onCreate();
    }
}
