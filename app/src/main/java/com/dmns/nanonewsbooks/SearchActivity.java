package com.dmns.nanonewsbooks;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.newsRadio)
    RadioButton newsR;
    @BindView(R.id.booksRadio)
    RadioButton booksR;
    @BindView(R.id.authorRadio)
    RadioButton authorR;
    @BindView(R.id.titleRadio)
    RadioButton titleR;
    @BindView(R.id.oneO)
    RadioButton show10;
    @BindView(R.id.twoO)
    RadioButton show20;
    @BindView(R.id.threeO)
    RadioButton show30;

    @BindView(R.id.show)
    TextView show;
    @BindView(R.id.scienceRadio)
    RadioButton scienceR;
    @BindView(R.id.styleRadio)
    RadioButton styleR;
    @BindView(R.id.politicsRadio)
    RadioButton politicsR;

    @BindView(R.id.edit_fraze)    SearchView userInput;




    @OnClick(R.id.booksRadio)
    protected void show() {
        show.setVisibility(View.VISIBLE);
        titleR.setVisibility(View.VISIBLE);
        authorR.setVisibility(View.VISIBLE);
        show10.setVisibility(View.VISIBLE);
        show20.setVisibility(View.VISIBLE);
        show30.setVisibility(View.VISIBLE);
        politicsR.setVisibility(View.GONE);
        styleR.setVisibility(View.GONE);
        scienceR.setVisibility(View.GONE);
    }

    @OnClick(R.id.newsRadio)
    protected void hide() {
        show.setVisibility(View.GONE);
        titleR.setVisibility(View.GONE);
        authorR.setVisibility(View.GONE);
        show10.setVisibility(View.GONE);
        show20.setVisibility(View.GONE);
        show30.setVisibility(View.GONE);
        politicsR.setVisibility(View.VISIBLE);
        styleR.setVisibility(View.VISIBLE);
        scienceR.setVisibility(View.VISIBLE);
    }



    @OnClick(R.id.execute_search)
    protected void showTest() {
        if(isNetworkAvailable(this)) {
        String Input = userInput.getQuery().toString();
        String finalString = "";
        String source = "";
        if (!(newsR.isChecked()) && !(booksR.isChecked())) {
            Toast.makeText(this, "Please make a selection.", Toast.LENGTH_SHORT).show();
        }
        if (authorR.isChecked() && booksR.isChecked() && (Input != null && !Input.isEmpty())) {

            String addInput = "https://www.googleapis.com/books/v1/volumes?q=%1$s+inauthor";
            finalString = String.format(addInput, Input);
            source = "books";
        }
        if (titleR.isChecked() && booksR.isChecked() && (Input != null && !Input.isEmpty())) {

            String addInput = "https://www.googleapis.com/books/v1/volumes?q=%1$s+intitle";
            finalString = String.format(addInput, Input);
            source = "books";
        }

        if (!(authorR.isChecked()) && !(titleR.isChecked()) && booksR.isChecked() && (Input != null && !Input.isEmpty())) {
            String addInput = "https://www.googleapis.com/books/v1/volumes?q=%1$s";
            finalString = String.format(addInput, Input);
            source = "books";
        }

        if (newsR.isChecked() && (Input != null && !Input.isEmpty())) {
            String addInput = "https://content.guardianapis.com/search?q=%1$s&order-by=newest&show-tags=contributor&api-key=test";
            finalString = String.format(addInput, Input);
            source = "news";
        }

        if (politicsR.isChecked() && newsR.isChecked()) {
            String base = "https://content.guardianapis.com/search?q=%1$s&%2$s&show-tags=contributor&api-key=test";
            String var = "tag=politics/politics";
            finalString = String.format(base, Input, var);
            source = "news";
        }
        if (styleR.isChecked() && newsR.isChecked()) {
            String base = "https://content.guardianapis.com/search?q=%1$s&%2$s&show-tags=contributor&api-key=test";
            String var = "section=lifeandstyle";
            finalString = String.format(base, Input, var);
            source = "news";
        }

        if (scienceR.isChecked() && newsR.isChecked()) {
            String base = "https://content.guardianapis.com/search?q=%1$s&%2$s&show-tags=contributor&api-key=test";
            String var = "section=technology";
            finalString = String.format(base, Input, var);
            source = "news";
        }

        if (show10.isChecked() && booksR.isChecked()) {
            finalString = finalString + "&maxResults=10";
            source = "books";
        }
        if (show20.isChecked() && booksR.isChecked()) {
            finalString = finalString + "&maxResults=20";
            source = "books";
        }
        if (show30.isChecked() && booksR.isChecked()) {
            finalString = finalString + "&maxResults=30";
            source = "books";
        }

        if (Input.equals("") && Input.isEmpty()) {
            Toast.makeText(this, "Fill search bar", Toast.LENGTH_SHORT).show();
        }

        if ((newsR.isChecked() || booksR.isChecked()) && (Input != null && !Input.isEmpty())) {

            Intent newIntent = new Intent(SearchActivity.this, DisplayList.class);
            newIntent.putExtra("httpRequest", finalString);
            newIntent.putExtra("type", source);
            startActivity(newIntent);
        }}

        else{Toast.makeText(SearchActivity.this, "No internet Connection!", Toast.LENGTH_LONG).show();}

    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        ButterKnife.bind(this);

    }
}