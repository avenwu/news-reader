package com.avenwu.ereader.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

public class Toolkit {

    public static boolean isUrlValid(String url) {
        return true;
    }

    public static File createFile(Context context, String url) {
        File file = null;
        String fileName = Uri.parse(url).getLastPathSegment();
        if (isExternalStorageWritable()) {
            File cacheFolder = new File(context.getExternalFilesDir("cache"),"xml");
            if (!cacheFolder.exists())
                cacheFolder.mkdirs();
            file = new File(cacheFolder, fileName);
        } else {
            file = new File(context.getFilesDir(), fileName);
        }
        return file;
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
