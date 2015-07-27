package com.swlee.android.runtimetheme;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wonlees on 5/20/15.
 */
public class RuntimeResourceListAdapter extends BaseAdapter {

    //private static final String LOG_TAG = RuntimeResourceListAdapter.class.getSimpleName();

    private int mListItemLayoutResId;
    private Context mContext;
    private ArrayList<HashMap<String, String>> mListData;

    public RuntimeResourceListAdapter(Context context, int layOutResId, ArrayList<HashMap<String, String>> arrayListData) {
        mContext = context;
        mListItemLayoutResId = layOutResId;
        mListData = arrayListData;
    }

    @Override
    public int getCount() {
        if (null != mListData)
            return mListData.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int index) {
        if (null != mListData && !mListData.isEmpty())
            return mListData.get(index);
        else
            return null;
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (null != convertView)
            view = convertView;
        else
            view = ResourceManager.findLayoutById(mContext, mListItemLayoutResId);

        if (null != view && null != mListData && 0 < mListData.size()) {
            HashMap<String, String> item = (HashMap<String, String>) getItem(position);

            TextView tv1 = (TextView) ResourceManager.findViewById(mContext, view, R.id.tv_text1);
            if (null != tv1 && null != item)
                tv1.setText(item.get("name"));

            TextView tv2 = (TextView) ResourceManager.findViewById(mContext, view, R.id.tv_text2);
            if (null != tv2 && null != item) {
                tv2.setText(item.get("type"));
            }
        }

        return view;
    }
}
