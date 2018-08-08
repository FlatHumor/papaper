package ru.inpleasure.papaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import ru.inpleasure.papaper.model.dbo.Article;


public class NewsAdapterModule extends BaseAdapter
    //implements IContract.IModel
{
    private List<Article> articles;
    private LayoutInflater inflater;
    private IContract.IPresenter presenter;


    public NewsAdapterModule(IContract.IView view) {
        articles = new ArrayList<>();
        inflater = (LayoutInflater)view.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        presenter = view.getPresenter();
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
        ((TextView)view.findViewById(R.id.list_item_article_description))
                .setText(article.getDescription());
        ((ImageView)view.findViewById(R.id.list_item_article_save))
                .setOnClickListener(v -> presenter.onClickSaveButton(article));
        ((ImageView)view.findViewById(R.id.list_item_article_link))
                .setOnClickListener(v -> presenter.onClickLinkButton(article));
        final ImageView articleIllustration = (ImageView)view.findViewById(R.id.list_item_article_image);
        Picasso.with(view.getContext())
                .load(article.getUrlToImage())
                .placeholder(R.drawable.mei_placeholder)
                .error(R.drawable.mei_placeholder_error)
                .centerCrop()
                .fit()
                .into(articleIllustration);

        return view;
    }


    public Observable<Article> getArticles() {
        return Observable.fromIterable(articles);
    }


    public long putArticle(Article article) {
        articles.add(article);
        notifyDataSetChanged();
        return 0L;
    }


    public Article getArticle(int position) {
        return articles.get(position);
    }


    public void clearAll() {
        articles.clear();
        notifyDataSetChanged();
    }

    public NewsAdapterModule getNewsAdapterModule() {
        return this;
    }
}
