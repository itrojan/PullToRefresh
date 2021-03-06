package com.aihook.pulltorefresh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;

import com.aihook.pulltorefresh.lib.XListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * XListView demo
 */
public class XListViewActivity extends Activity implements XListView.IXListViewListener {
    private XListView mListView;

    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> items = new ArrayList<String>();
    private Handler mHandler;
    private int mIndex = 0;
    private int mRefreshIndex = 0;

    public static void launch(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, XListViewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_list_view);

        geneItems();
        initView();
    }

    private void initView() {
        mHandler = new Handler();

        mListView = (XListView) findViewById(R.id.list_view);
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
        mListView.setAutoLoadEnable(true);
        mListView.setXListViewListener(this);
        mListView.setRefreshTime(getTime());
        mListView.setRefreshDelay(2500);
        mListView.setLoadMoreDelay(2500);

        mAdapter = new ArrayAdapter<String>(this, R.layout.vw_list_item, items);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            mListView.autoRefresh();
        }
    }

    @Override
    public void onRefresh() {
        mIndex = ++mRefreshIndex;
        items.clear();
        geneItems();
        mAdapter = new ArrayAdapter<String>(XListViewActivity.this, R.layout.vw_list_item,
                items);
        mListView.setAdapter(mAdapter);
        onLoad();

    }

    @Override
    public void onLoadMore() {
        geneItems();
        mAdapter.notifyDataSetChanged();
        onLoad();
    }

    private void geneItems() {
        for (int i = 0; i != 20; ++i) {
            items.add("Test XListView item " + (++mIndex));
        }
    }

    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime(getTime());
    }

    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }
}
