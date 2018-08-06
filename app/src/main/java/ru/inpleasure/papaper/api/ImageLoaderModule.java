package ru.inpleasure.papaper.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;

@Module
public class ImageLoaderModule
{
    public Observable<Bitmap> loadArticleIllustration(String url)
    {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        return Observable.fromCallable(() -> {
                    try {
                        return BitmapFactory.decodeStream(
                                client.newCall(request).execute().body().byteStream());
                    }
                    catch (Exception e) { return null; }
                }
        )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Provides
    public ImageLoaderModule getImageLoader() {
        return this;
    }
}
