package com.example.apichaya.addrealmsudent.customs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apichaya.addrealmsudent.R;

public abstract class AbstractToolbarActivity extends Activity {

    protected Activity activity;
    protected ProgressDialog progressDialog;
    private LinearLayout mToolbar;
    private ImageView imgIcon;
    private ImageView imgIconLeft;
    private TextView txtTitleToolbar;

    protected abstract int setContentView();

    protected abstract void bindActionbar(ImageView imgIcon, ImageView menuLeft, ImageView imgIconRight, TextView txtTitleToolbar);

    protected abstract void bindUI(Bundle savedInstanceState);

    protected abstract void setupUI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setContentView());
        activity = this;
        setProgressDialog();
        initActionbar();
        bindUI(savedInstanceState);
        setupUI();

    }

    private void setProgressDialog() {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Now Loading...");
        progressDialog.setCancelable(false);
    }

    private void initActionbar() {
        mToolbar = (LinearLayout) findViewById(R.id.mToolBar);
        txtTitleToolbar = (TextView) findViewById(R.id.textviewTitleToolbar);
        imgIconLeft = (ImageView) findViewById(R.id.imageviewIconLeft);
        imgIcon = (ImageView) findViewById(R.id.imageviewIconToolbar);
        ImageView imgIconRight = (ImageView)  findViewById(R.id.imageviewIconRight);
        bindActionbar(imgIcon, imgIconLeft, imgIconRight, txtTitleToolbar);
    }

    private View.OnClickListener onMenuClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    public void onBackPressedButtonLeft() {
        imgIconLeft.setVisibility(View.VISIBLE);
//        changeStatusBarColor(R.color.color_white);
        imgIconLeft.setOnClickListener(onMenuClicked);
    }

    //change color of toolbar and status bar
    public void changeStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(color));
        }
    }

    protected void setToolbarColor(int color) {
        mToolbar.setBackgroundColor(getResources().getColor(color));
        setTitleColor(R.color.color_white);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void setTitleColor(int color) {
        txtTitleToolbar.setTextColor(getResources().getColor(color));
    }

    public void setTitle(int title) {
        imgIcon.setVisibility(View.INVISIBLE);
        txtTitleToolbar.setText(getResources().getString(title));
    }

    public void setTitle(String title) {
        imgIcon.setVisibility(View.INVISIBLE);
        txtTitleToolbar.setText(title);
    }

    public void setTitle(int title, int icon) {
        setTitle(title);
        if (icon != 0) {
            txtTitleToolbar.setCompoundDrawablesWithIntrinsicBounds(0, 0, icon, 0);
            txtTitleToolbar.setCompoundDrawablePadding(20);
        }
    }
}