package com.example.apichaya.addrealmsudent.customs;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;

/**
 * Created by apple on 5/17/2017 AD.
 */

public class MyIntent {

    public static final int PERMISSION_READ_EXTERNAL_STORAGE = 33;

    public static boolean isRequestPermissionReadExternalStorage(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ_EXTERNAL_STORAGE);
            }
            return false;
        }
        return true;
    }

    public static void startIntentGallery(Activity activity, int requestCode) {
        Intent intent = new Intent(android.content.Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        activity.startActivityForResult(Intent.createChooser(intent, "Select your image"), requestCode);
    }
}
