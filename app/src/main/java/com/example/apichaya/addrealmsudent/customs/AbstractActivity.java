package com.example.apichaya.addrealmsudent.customs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public abstract class AbstractActivity extends Activity {

    protected Activity activity;
    protected ProgressDialog progressDialog;

    protected abstract int setContentView();

    protected abstract void bindUI(Bundle savedInstanceState);

    protected abstract void setupUI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setContentView());

        activity = this;
        setProgressDialog();
        bindUI(savedInstanceState);
        setupUI();
    }

    private void setProgressDialog() {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Now Loading...");
        progressDialog.setCancelable(false);
    }

    //change color of toolbar and status bar
    public void changeStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(color));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}