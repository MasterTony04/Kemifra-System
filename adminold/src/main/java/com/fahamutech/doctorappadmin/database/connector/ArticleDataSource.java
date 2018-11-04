package com.fahamutech.doctorappadmin.database.connector;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;

public interface ArticleDataSource {
    void getArticles(String categoryId, RecyclerView recyclerView,
                     SwipeRefreshLayout swipeRefreshLayout);
}
