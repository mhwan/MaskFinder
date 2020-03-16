package com.mhwan.mask.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mhwan.mask.CustomUI.StoreListRecyclerAdapter;
import com.mhwan.mask.Item.Store;
import com.mhwan.mask.R;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView textView, filter_info_textview;
    private ArrayList<Store> stores;
    public static final String STORE_EXTRA = "storesinfoExtras";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        stores = (ArrayList<Store>) getIntent().getSerializableExtra(STORE_EXTRA);

        setToolbar();
        initView();
    }

    private void initView(){
        textView = (TextView) findViewById(R.id.empty_textview);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        filter_info_textview = (TextView) findViewById(R.id.auto_filter_info_text);
        if (stores != null && stores.size() > 0) {
            textView.setVisibility(View.GONE);
            filter_info_textview.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
            recyclerView.setAdapter(new StoreListRecyclerAdapter(ListActivity.this, stores));
        } else {
            textView.setVisibility(View.VISIBLE);
            filter_info_textview.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    private void setToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.findViewById(R.id.toolbar_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
