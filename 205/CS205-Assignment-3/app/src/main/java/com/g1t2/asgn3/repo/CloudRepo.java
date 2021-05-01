package com.g1t2.asgn3.repo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.g1t2.asgn3.model.ListItem;

import java.io.InputStream;
import java.net.URL;
import java.util.NoSuchElementException;

/**
 * Repo implementation that retrieves images from the web.
 */
public class CloudRepo implements Repo<String, ListItem> {
    /**
     * Retrieve an image from the web and compute its caption.
     *
     * @param url The URL of the image.
     * @return The image and its caption.
     */
    @Override
    public ListItem get(String url) {
        // Get caption
        int m = url.lastIndexOf("/");
        int n = url.lastIndexOf(".png");
        String name = url.substring(m + 1, n);

        // Download image from web
        try {
            URL imageURL = new URL(url);
            InputStream input = imageURL.openConnection().getInputStream();
            Bitmap image = BitmapFactory.decodeStream(input);
            return new ListItem(name, image);
        } catch (Exception e) {
            throw new NoSuchElementException(String.format("Could not download from %s", url));
        }
    }

    /**
     * Do nothing, as images from the web cannot be "saved".
     */
    @Override
    public void save(String url, ListItem item) { }

    /**
     * Do nothing, as we cannot delete images from the web.
     */
    @Override
    public void deleteAll() {}
}