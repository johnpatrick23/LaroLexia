package com.oneclique.larolexia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oneclique.larolexia.R;

import java.util.List;

public class TitikGridViewAdapter extends BaseAdapter {

    private Context context;
    private final List<String> Letters;

    public TitikGridViewAdapter(Context context, List<String> Letters){
        this.context = context;
        this.Letters = Letters;
    }

    @Override
    public int getCount() {
        return Letters.size();
    }

    @Override
    public Object getItem(int position) {
        return Letters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if(convertView == null){
            gridView = inflater.inflate(R.layout.item_titik, null);

            TextView textView = gridView.findViewById(R.id.mTextViewLetter);
            textView.setText(Letters.get(position));

        }else {
            gridView = convertView;
        }

        return gridView;
    }
}
