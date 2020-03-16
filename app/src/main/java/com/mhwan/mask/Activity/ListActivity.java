package com.mhwan.mask.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mhwan.mask.CustomUI.StoreListRecyclerAdapter;
import com.mhwan.mask.Item.Store;
import com.mhwan.mask.R;
import com.mhwan.mask.Util.ItemClickListener;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView textView, filter_info_textview;
    private ArrayList<Store> stores;
    private StoreListRecyclerAdapter recyclerAdapter;
    public static final String STORE_EXTRA = "storesinfoExtras";
    public static final String RESULT_ITEM_POSITION_EXTRA = "itemPositionsExtra";
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
        recyclerAdapter = new StoreListRecyclerAdapter(ListActivity.this, stores, new ItemClickListener() {
            @Override
            public void OnItemClickListener(View v, int position) {
                Intent intent = new Intent();
                intent.putExtra(RESULT_ITEM_POSITION_EXTRA, position);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        if (stores != null && stores.size() > 0) {
            textView.setVisibility(View.GONE);
            filter_info_textview.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
            recyclerView.setAdapter(recyclerAdapter);
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
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

}
