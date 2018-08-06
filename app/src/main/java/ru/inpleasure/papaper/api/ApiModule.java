package ru.inpleasure.papaper.api;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.inpleasure.papaper.IContract;

@Module
public class ApiModule
{
    private static final boolean ENABLE_LOG = true;
    private static final String BASE_API_URL = "https://newsapi.org/v2/";
    private static final String EMPTY_API_URL = "";

    @Provides
    public IContract.IApi getApi()
    {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (ENABLE_LOG)
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        builder.client(client);

        return builder.build().create(IContract.IApi.class);
    }
}
