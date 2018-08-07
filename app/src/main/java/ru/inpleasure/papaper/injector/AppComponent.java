package ru.inpleasure.papaper.injector;

import javax.inject.Singleton;

import dagger.Component;
import ru.inpleasure.papaper.NewsActivity;
import ru.inpleasure.papaper.NewsAdapterModule;
import ru.inpleasure.papaper.api.ApiModule;
import ru.inpleasure.papaper.model.ModelModule;
import ru.inpleasure.papaper.presenter.ArticlePresenter;

@Singleton
@Component(modules = {
        ApiModule.class,
        ModelModule.class})
public interface AppComponent
{
    void inject(ArticlePresenter presenter);
    void inject(NewsActivity newsActivity);
    void inject(NewsAdapterModule adapterModule);
}
