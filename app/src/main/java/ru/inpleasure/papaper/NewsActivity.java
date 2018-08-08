package ru.inpleasure.papaper;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import ru.inpleasure.papaper.model.dbo.Article;
import ru.inpleasure.papaper.presenter.ArticlePresenter;

public class NewsActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        IContract.IView
{
    private IContract.IPresenter presenter;
    private ListView newsListView;
    private NewsAdapterModule newsAdapterModule;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        App.getComponent().inject(this);
        presenter = new ArticlePresenter(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        newsListView = (ListView) findViewById(R.id.news_list_view);
        newsAdapterModule = new NewsAdapterModule(this);
        newsListView.setAdapter(newsAdapterModule);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener((v) ->
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        presenter.onCreate();
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_category_business:
                presenter.onCategoryChanged(ArticlePresenter.CATEGORY_BUSINESS);
                break;
            case R.id.menu_category_entertainment:
                presenter.onCategoryChanged(ArticlePresenter.CATEGORY_ENTERTAINMENT);
                break;
            case R.id.menu_category_general:
                presenter.onCategoryChanged(ArticlePresenter.CATEGORY_GENERAL);
                break;
            case R.id.menu_category_health:
                presenter.onCategoryChanged(ArticlePresenter.CATEGORY_HEALTH);
                break;
            case R.id.menu_category_science:
                presenter.onCategoryChanged(ArticlePresenter.CATEGORY_SCIENCE);
                break;
            case R.id.menu_category_sports:
                presenter.onCategoryChanged(ArticlePresenter.CATEGORY_SPORTS);
                break;
            case R.id.menu_category_technology:
                presenter.onCategoryChanged(ArticlePresenter.CATEGORY_TECHNOLOGY);
                break;
            case R.id.menu_item_favorite:
                presenter.onClickFavoriteButton();
                break;
                default: break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void showLoadingProgress() { }

    @Override
    public void hideLoadingProgress() { }

    @Override
    public void showArticle(Article article) {
        newsAdapterModule.putArticle(article);
    }

    @Override
    public void clearArticles() {
        newsAdapterModule.clearAll();
    }

    @Override
    public IContract.IPresenter getPresenter() {
        return presenter;
    }

    @Override
    public Context getContext() {
        return this;
    }
}
