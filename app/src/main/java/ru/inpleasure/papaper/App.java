package ru.inpleasure.papaper;

import android.app.Application;

import ru.inpleasure.papaper.api.ApiModule;
import ru.inpleasure.papaper.injector.AppComponent;
import ru.inpleasure.papaper.injector.DaggerAppComponent;
import ru.inpleasure.papaper.model.ModelModule;

public class App extends Application
{
    private static final String UI_THREAD = "UI_THREAD";
    private static final String IO_THREAD = "IO_THREAD";

    private static AppComponent component;

    public static AppComponent getComponent() {
        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        component = buildComponent();
    }

    protected AppComponent buildComponent() {
        return DaggerAppComponent.builder()
                .apiModule(new ApiModule())
                .modelModule(new ModelModule(this))
                .build();
    }
}
