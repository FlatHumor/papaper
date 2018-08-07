package ru.inpleasure.papaper.injector;

import javax.inject.Singleton;

import dagger.Component;
import ru.inpleasure.papaper.NewsActivity;
import ru.inpleasure.papaper.NewsAdapterModule;
import ru.inpleasure.papaper.api.ApiModule;
import ru.inpleasure.papaper.model.CacheDatabaseModule;
import ru.inpleasure.papaper.model.CacheModel;
import ru.inpleasure.papaper.model.FavoriteDatabaseModule;
import ru.inpleasure.papaper.presenter.ArticlePresenter;

@Singleton
@Component(modules = {
        ApiModule.class,
        CacheModel.class})
public interface AppComponent
{
    void inject(ArticlePresenter presenter);
    void inject(NewsActivity newsActivity);
    void inject(CacheModel model);
    void inject(NewsAdapterModule adapterModule);
}
