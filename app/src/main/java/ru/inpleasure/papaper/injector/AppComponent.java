package ru.inpleasure.papaper.injector;

import javax.inject.Singleton;

import dagger.Component;
import ru.inpleasure.papaper.MainActivity;
import ru.inpleasure.papaper.api.ApiModule;
import ru.inpleasure.papaper.model.ModelModule;

@Singleton
@Component(modules = {
        ApiModule.class,
        ModelModule.class})
public interface AppComponent
{
    void inject(MainActivity mainActivity);
}
