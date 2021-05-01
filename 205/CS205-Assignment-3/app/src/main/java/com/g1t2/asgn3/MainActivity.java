package com.g1t2.asgn3;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Consumer;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.g1t2.asgn3.model.LRUCache;
import com.g1t2.asgn3.repo.CloudRepo;
import com.g1t2.asgn3.model.ListItem;
import com.g1t2.asgn3.repo.DiskRepo;
import com.g1t2.asgn3.repo.LRURepo;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class MainActivity extends AppCompatActivity {

    private Handler handler;

    private ChainableStore<String, ListItem> cloudStore;
    private ChainableStore<String, ListItem> diskStore;
    private ChainableStore<String, ListItem> lruStore;
    private Loader loader;

    private ListAdapter listAdapter;
    private ListView list;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler(Looper.getMainLooper());

        cloudStore = new ChainableStore<>(new CloudRepo());
        diskStore = new ChainableStore<>(new DiskRepo(
                getExternalFilesDir(null) + "/images"), cloudStore);
        lruStore = new ChainableStore<>(new LRURepo(cacheListener), diskStore);
        loader = new Loader(lruStore);

        listAdapter = new ListAdapter(MainActivity.this, URLs, loader);
        list = findViewById(R.id.list);
        textView = findViewById(R.id.textView);

        // map the screen layout's Button 1 to the variable b1
        Button b1 = findViewById(R.id.Button1);
        b1.setOnClickListener(webListener);
        Button b2 = findViewById(R.id.Button2);
        b2.setOnClickListener(clearCacheListener);
        Button b3 = findViewById(R.id.Button3);
        b3.setOnClickListener(deleteFilesListener);
    }

    public void popUpMessage(String text) {
        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void sendMessageUI(String text) {
        handler.post(() -> {
            textView.setText(text);
        });
    }

    public View.OnClickListener webListener = (View view) -> {
        popUpMessage("Downloading from Web");
        if (list.getAdapter() == null) {
            list.setAdapter(listAdapter);
        }
    };

    public View.OnClickListener clearCacheListener = (View view) -> {
        popUpMessage("Clearing cache");
        lruStore.deleteAll();
        sendMessageUI("Cache cleared");
    };

    public View.OnClickListener deleteFilesListener = (View view) -> {
        popUpMessage("Deleting files");
        diskStore.deleteAll();
        sendMessageUI("Deleted files");
    };

    public Consumer<LRUCache<String, ListItem>> cacheListener = (LRUCache<String, ListItem> cache) -> {
        sendMessageUI(String.format("%s KB used out of available cache of %s KB",
                cache.getMemSize() / 1024, cache.getMemLimit() / 1024));
    };

    @Override
    public void onDestroy() {
        list.setAdapter(null);
        super.onDestroy();
    }

    private static String[] URLs = {
        "https://i.ibb.co/8PpYkH4/airplane.png",
        "https://i.ibb.co/GtvS9Kv/arctichare.png",
        "https://i.ibb.co/R68YhSN/baboon.png",
        "https://i.ibb.co/s1sgt1h/barbara.png",
        "https://i.ibb.co/X53Gznv/boat.png",
        "https://i.ibb.co/xG3R3p1/cat.png",
        "https://i.ibb.co/dMs8Kvx/fruits.png",
        "https://i.ibb.co/M2QzSFg/frymire.png",
        "https://i.ibb.co/dMZ2N3N/girl.png",
        "https://i.ibb.co/XCrkhc3/goldhill.png",
        "https://i.ibb.co/cv0Jm1n/lena.png",
        "https://i.ibb.co/DVM6Cvq/monarch.png",
        "https://i.ibb.co/PtWRX6x/mountain.png",
        "https://i.ibb.co/ph6V8bd/peppers.png",
        "https://i.ibb.co/RDhmNMh/pool.png",
        "https://i.ibb.co/z6ZD7Ny/sails.png",
        "https://i.ibb.co/gyMyzcW/serrano.png",
        "https://i.ibb.co/5Y1ZFYj/tulips.png",
        "https://i.ibb.co/x2khkdG/watch.png",
        "https://i.ibb.co/pnGQk92/zelda.png"
    };

}