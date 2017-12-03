package com.example.apichaya.addrealmsudent.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apichaya.addrealmsudent.R;
import com.example.apichaya.addrealmsudent.customs.AbstractToolbarActivity;
import com.example.apichaya.addrealmsudent.customs.FileName;
import com.example.apichaya.addrealmsudent.customs.FileSize;
import com.example.apichaya.addrealmsudent.customs.MyIntent;
import com.example.apichaya.addrealmsudent.dialog.MyAlertDialog;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import static com.example.apichaya.addrealmsudent.customs.MyIntent.PERMISSION_READ_EXTERNAL_STORAGE;

/**
 * Created by apple on 11/12/2017 AD.
 */

public class CropImageActivity extends AbstractToolbarActivity {

    public static final int REQUEST_CAMERA = 1;
    public static final int REQUEST_IMAGE_GALLERY = 12;

    private int indexClicked = 0;
    private String imageFileName;
    private File fileImage;
    private CropImageView cropImageView;
    private TextView txtImageSize;

    @Override
    protected int setContentView() {
        return R.layout.activity_crop_image;
    }

    @Override
    protected void bindActionbar(ImageView imgIcon, ImageView menuLeft, final ImageView imgIconRight, TextView txtTitleToolbar) {
        txtTitleToolbar.setText("Crop image");
        imgIconRight.setImageResource(R.drawable.img_oval_icon);
        imgIconRight.setVisibility(View.VISIBLE);
        imgIconRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (indexClicked % 2 != 0) {
                    imgIconRight.setImageResource(R.drawable.img_oval_icon);
                    cropImageView.setCropShape(CropImageView.CropShape.RECTANGLE);
                } else {
                    cropImageView.setCropShape(CropImageView.CropShape.OVAL);
                    imgIconRight.setImageResource(R.drawable.img_square_icon);
                }
                indexClicked++;
            }
        });
    }

    @Override
    protected void bindUI(Bundle savedInstanceState) {
        cropImageView = (CropImageView) findViewById(R.id.cropImageView);
        txtImageSize = (TextView) findViewById(R.id.textviewImageSize);

    }

    @Override
    protected void setupUI() {
        onBackPressedButtonLeft();
        if (MyIntent.isRequestPermissionReadExternalStorage(activity)) {
            alertSelect();
        }
        cropImageView.setOnSetCropOverlayMovedListener(new CropImageView.OnSetCropOverlayMovedListener() {
            @Override
            public void onCropOverlayMoved(Rect rect) {
                txtImageSize.setText("Image size " + rect.width() + "x" + rect.height());
            }
        });

    }

    private void alertSelect() {
        MyAlertDialog.dialogSelect(activity, new MyAlertDialog.OnSelectListener() {
            @Override
            public void selectGallery() {
                selectImageGallery();
            }

            @Override
            public void selectTakePhoto() {
                cameraIntent();
            }

            @Override
            public void cancel() {
                onBackPressed();
            }
        });
    }

    public void doneButton(View view) {
        cropImageView.post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setData(Uri.fromFile(FileSize.reduceImageSize(convertBitmapToFile(cropImageView.getCroppedImage()))));
                setResult(RESULT_OK, intent);
                finishActivity(123);
                finish();
            }
        });
    }

    private void deleteImage(File fileImage) {
        File file = new File(FileName.getRealPathFromURI(activity, Uri.fromFile(fileImage)));
        if (file.exists()) {
            file.delete();
        }
    }

    private void cameraIntent() {
        imageFileName = "JPEG_" + new Date().getTime(); //make a better file name
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                activity.getResources().getString(R.string.app_name));
        fileImage = null;
        try {
            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }
            fileImage = File.createTempFile(imageFileName,
                    ".jpg",
                    storageDir
            );

        } catch (IOException e) {
            Log.e("create temp file error", e.getMessage());
            e.printStackTrace();
        }

        Uri uriImage = Uri.fromFile(fileImage);

        // Save a file: path for use with ACTION_VIEW intents
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriImage);
        startActivityForResult(takePhotoIntent, REQUEST_CAMERA);
    }

    public void selectImageGallery() {
        if (MyIntent.isRequestPermissionReadExternalStorage(activity)) {
            MyIntent.startIntentGallery(activity, REQUEST_IMAGE_GALLERY);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    alertSelect();
                } else {
                    finish();
                    // Your app will not have this permission. Turn off all functions
                    // that require this permission or it will force close like your
                    // original question
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            } else if (requestCode == REQUEST_IMAGE_GALLERY) {
                Uri imageUri = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (bitmap == null) {
                    return;
                }
                cropImageView.setImageBitmap(bitmap);
                txtImageSize.setText("Image size " + cropImageView.getWholeImageRect().width() + "x"
                        + cropImageView.getWholeImageRect().height());
            }
        } else {
            finish();
        }

    }

    private void onCaptureImageResult(Intent data) {
        File file = FileSize.reduceImageSize(rotateImage(fileImage.getPath()));
        Bitmap realImage = BitmapFactory.decodeFile(file.getPath());
        cropImageView.setImageBitmap(realImage);
        txtImageSize.setText("Image size " + cropImageView.getWholeImageRect().width() + "x"
                + cropImageView.getWholeImageRect().height());
        deleteImage(file);
    }

    private File rotateImage(String filePath) {
        Bitmap realImage = BitmapFactory.decodeFile(filePath);

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("EXIF value", exif.getAttribute(ExifInterface.TAG_ORIENTATION));
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                realImage = rotate(realImage, 90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                realImage = rotate(realImage, 180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                realImage = rotate(realImage, 270);
                break;
        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        realImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        imageFileName = "JPEG_" + new Date().getTime(); //make a better file name
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                activity.getResources().getString(R.string.app_name));
        fileImage = null;
        try {
            fileImage = File.createTempFile(imageFileName,
                    ".jpg",
                    storageDir
            );

        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream fo;
        try {
            fileImage.createNewFile();
            fo = new FileOutputStream(fileImage);
            fo.write(bytes.toByteArray());
            fo.close();
            scanFile(activity, fileImage.getPath());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileImage;
    }

    public static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        mtx.postRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    private static void scanFile(Activity activity, String file) {
        MediaScannerConnection.scanFile(activity,
                new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });


    }

    private File convertBitmapToFile(Bitmap realImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        realImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        File image = null;
        imageFileName = "JPEG_" + new Date().getTime(); //make a better file name
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                activity.getResources().getString(R.string.app_name));
        image = null;
        try {
            image = File.createTempFile(imageFileName,
                    ".jpg",
                    storageDir
            );

        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream fo;
        try {
            image.createNewFile();
            fo = new FileOutputStream(image);
            fo.write(bytes.toByteArray());
            fo.close();
            scanFile(activity, image.getPath());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

}
