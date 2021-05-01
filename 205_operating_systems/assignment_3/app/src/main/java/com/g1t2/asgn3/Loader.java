package com.g1t2.asgn3;

import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;

import com.g1t2.asgn3.model.ListItem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Loader {

    private final ChainableStore<String, ListItem> store;
    private final ExecutorService executorService;
    private final Handler handler;

    public Loader(ChainableStore<String, ListItem> store) {
        this.store = store;
        this.executorService = Executors.newFixedThreadPool(5);
        this.handler = new Handler(Looper.getMainLooper());
    }

    public void loadView(String URL, TextView textView, ImageView imageView) {
        store.findById(URL, executorService, (ListItem item) -> {
            handler.post(() -> {
                textView.setText(item.getName());
                imageView.setImageBitmap(item.getImage());
            });
        });
    }
}