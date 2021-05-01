package com.g1t2.asgn3.model;

import android.graphics.Bitmap;

/**
 * ListItem models the data required to hydrate a single item in the main ListView. This allows us
 * to easily pass the model between storage levels while maintaining a stable interface.
 */
public class ListItem implements ByteCountable {
    // Caption
    private String name;
    // Bitmap
    private Bitmap image;

    public ListItem(String name, Bitmap image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public Bitmap getImage() {
        return image;
    }

    @Override
    public long getByteCount() {
        return image == null ? 0 : image.getByteCount();
    }

    @Override
    public String toString() {
        return name;
    }
}
