package com.oneclique.larolexia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oneclique.larolexia.R;

import java.util.List;

public class PantigGridViewAdapter extends BaseAdapter {

    private Context context;
    private final List<String> Pantig;

    public PantigGridViewAdapter(Context context, List<String> Pantig){
        this.context = context;
        this.Pantig = Pantig;
    }

    @Override
    public int getCount() {
        return Pantig.size();
    }

    @Override
    public Object getItem(int position) {
        return Pantig.get(position);
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
            textView.setText(Pantig.get(position));

        }else {
            gridView = convertView;
        }

        return gridView;
    }
}
