package com.g1t2.asgn3;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {
    private LayoutInflater inflater = null;

    private final String[] URLs;
    private final Loader loader;

    public ListAdapter(Activity activity, String[] URLs, Loader loader) {
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.URLs = URLs;
        this.loader = loader;
    }

    @Override
    public int getCount() {
        return URLs.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return URLs[position].hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item, null);
        }

        // set the text value in the item View
        TextView textView = convertView.findViewById(R.id.text);
        ImageView imageView = convertView.findViewById(R.id.image);

        // prevent flashes of old content.
        textView.setText("");
        imageView.setImageBitmap(null);

        loader.loadView(URLs[position], textView, imageView);
        return convertView;
    }
}
