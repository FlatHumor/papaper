package ru.inpleasure.papaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Observable;
import ru.inpleasure.papaper.api.ImageLoaderModule;
import ru.inpleasure.papaper.model.dbo.Article;



public class NewsAdapterModule extends BaseAdapter
    implements IContract.IModel
{
    private List<Article> articles;
    private LayoutInflater inflater;
    private IContract.IPresenter presenter;

    @Inject
    protected ImageLoaderModule imageLoader;

    public NewsAdapterModule(IContract.IView view) {
        articles = new ArrayList<>();
        inflater = (LayoutInflater)view.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        presenter = view.getPresenter();
        App.getComponent().inject(this);
    }

    @Override
    public int getCount() {
        return articles.size();
    }

    @Override
    public Object getItem(int position) {
        return articles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("CheckResult")
    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        if (view == null)
            view = inflater.inflate(R.layout.list_item_article, parent, false);
        Article article = articles.get(position);

        ((TextView)view.findViewById(R.id.list_item_article_title))
                .setText(article.getTitle());
        ((TextView)view.findViewById(R.id.list_item_article_published_at))
                .setText(article.getFormattedPublishedAt());
        final ImageView articleIllustration = (ImageView)view.findViewById(R.id.list_item_article_image);
        if (!presenter.findIllustration(article.getId(), articleIllustration)) {
            try {
                imageLoader.loadArticleIllustration(article.getUrlToImage())
                        .subscribe(bitmap -> {
                            articleIllustration.setImageBitmap(bitmap);
                            presenter.cacheIllustration(article.getId(), bitmap);
                        });
            }
            catch (Exception e) { }
            System.out.print(String.valueOf(article.getId()) + " loaded from internet");
        }
        System.out.println(String.valueOf(position) + " builded");
        return view;
    }

    @Override
    public Observable<Article> getArticles() {
        return Observable.fromIterable(articles);
    }

    @Override
    public long putArticle(Article article) {
        articles.add(article);
        notifyDataSetChanged();
        return 0L;
    }

    @Override
    public Article getArticle(int position) {
        return articles.get(position);
    }

    @Override
    public void clearAll() {
        articles.clear();
    }

    @Override
    public Bitmap getArticleIllustration(int id) {
        return null;
    }

    @Override
    public void putArticleIllustration(int id, Bitmap bitmap) {

    }

    public NewsAdapterModule getNewsAdapterModule() {
        return this;
    }
}
