package com.g1t2.asgn3.repo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.g1t2.asgn3.model.ListItem;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.util.NoSuchElementException;

/**
 * Repo implementation that retrieves images from secondary storage.
 */
public class DiskRepo implements Repo<String, ListItem> {
    private static final String TAG = "DiskRepo";
    private File fileDir;

    /**
     * Constructor
     *
     * @param pathname Directory where images should be stored relative to the app root directory.
     */
    public DiskRepo(String pathname) {
        this.fileDir = new File(pathname);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
    }

    /**
     * Retrieve an image and caption from disk.
     *
     * @param url The URL of the image
     * @return The image and its caption.
     */
    @Override
    public ListItem get(String url) {
        String fileHash = String.valueOf(url.hashCode());
        String fileName = null;
        for (String file : fileDir.list()) {
            if (file.contains(fileHash)) {
                fileName = file;
                break;
            }
        }
        if (fileName == null) {
            throw new NoSuchElementException(
                    String.format("Could not find file on disk for %s", url));
        }

        File file = new File(fileDir, fileName);
        try {
            InputStream is = new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            is.close();
            String name = fileName.substring(0, fileName.lastIndexOf('_'));
            return new ListItem(name, bitmap);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        throw new NoSuchElementException(
                String.format("Could not find file on disk for %s", url));
    }

    /**
     * Save an image to disk.
     *
     * @param url URL of the image
     * @param item ListItem representing caption and image data in bitmap.
     */
    @Override
    public void save(String url, ListItem item) {
        String fileHash = String.valueOf(url.hashCode());
        String fileName = item.getName();
        File file = new File(fileDir, String.format("%s_%s.txt", fileName, fileHash));

        try {
            if (file.exists()) file.delete();

            Bitmap bitmap = item.getImage();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapData = bos.toByteArray();
            bos.close();

            // write the bytes in file
            FileOutputStream fos = new FileOutputStream((file));
            fos.write(bitmapData);
            fos.flush();
            fos.close();
        } catch (Exception e){
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Delete all images from disk.
     */
    @Override
    public void deleteAll() {
        for (String fileName : fileDir.list()) {
            File file = new File(fileDir, fileName);
            if (file.exists()) file.delete();
        }
    }
}