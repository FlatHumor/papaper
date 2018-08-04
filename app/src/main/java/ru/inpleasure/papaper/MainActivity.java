package ru.inpleasure.papaper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
{

    @Inject
    protected IContract.IApi api;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.main_activity_title));
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.default_tv);
        App.getComponent().inject(this);
        /*
        api.getTopHeadliners("technology", "00", "ru")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(newsDto -> {
                    textView.setText("OK");
                });
                */
    }
}
