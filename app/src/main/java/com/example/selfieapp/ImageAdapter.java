package com.example.selfieapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import androidx.exifinterface.media.ExifInterface;

import android.net.Uri;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImageAdapter extends ArrayAdapter<File> {
    public ImageAdapter(Context context, ArrayList<File> files) {
        super(context, 0, files);
    }

    private ViewGroup createInflatedLayout(@NonNull ViewGroup parent) {
        return (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.image_row, parent, false);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        File imageFile = getItem(position);

        if (convertView == null) {
            convertView = createInflatedLayout(parent);
        }

        String uri = String.valueOf(Uri.fromFile(imageFile));
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .considerExifParams(true)
                .build();

        ImageView imageView = convertView.findViewById(R.id.image_thumbnail);
        ImageLoader.getInstance().displayImage(uri, imageView, displayImageOptions);

        TextView textView = convertView.findViewById(R.id.image_name);
        textView.setText(imageFile.getName());

        return convertView;
    }
}
