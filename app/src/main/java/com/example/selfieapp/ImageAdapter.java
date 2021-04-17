package com.example.selfieapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import androidx.exifinterface.media.ExifInterface;

import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

    private static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);

            if (orientation != -1) {
                // We only recognise a subset of orientation tag values.
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }

            }
        }

        return degree;
    }

    public Bitmap createRotateBitmap(Bitmap bitmap, String filepath) {
        int angle = getExifOrientation(filepath);
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                matrix, true);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        File imageFile = getItem(position);

        if (convertView == null) {
            convertView = createInflatedLayout(parent);
        }

        String imagePath = imageFile.getAbsolutePath();

        Bitmap originalBitmap = BitmapFactory.decodeFile(imagePath);
        Bitmap rotatedBitmap = createRotateBitmap(originalBitmap, imagePath);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(rotatedBitmap, 150, 150, true);

        ImageView imageThumbnailView = convertView.findViewById(R.id.image_thumbnail);
        imageThumbnailView.setImageBitmap(scaledBitmap);

        TextView imageNameView = convertView.findViewById(R.id.image_name);
        imageNameView.setText(imageFile.getName());

        return convertView;
    }
}
