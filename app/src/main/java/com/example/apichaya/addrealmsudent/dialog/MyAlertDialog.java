package com.example.apichaya.addrealmsudent.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.apichaya.addrealmsudent.R;

/**
 * Created by apple on 7/25/2017 AD.
 */

public class MyAlertDialog extends AlertDialog implements View.OnClickListener {

    private Context context;

    private TextView txtMessage;
    private TextView txtPositiveButton;
    private TextView txtNegativeButton;
    private EditText edtName;
    private EditText edtPPM;

    private OnClickPositiveAddSubstanceListener onClickPositiveAddSubstanceListener;
    private OnClickPositiveListener onClickPositiveListener;
    private OnSelectListener onSelectListener;

    public MyAlertDialog(Context context) {
        super(context);
        this.context = context;
        dialogAddChemical();
    }

    public MyAlertDialog(Context context, OnClickPositiveAddSubstanceListener onClickPositiveListener) {
        super(context);
        this.context = context;
        this.onClickPositiveAddSubstanceListener = onClickPositiveListener;
        dialogAddChemical();
    }

    public MyAlertDialog(Context context, OnClickPositiveListener onClickPositiveListener) {
        super(context);
        this.context = context;
        this.onClickPositiveListener = onClickPositiveListener;
        dialogAlert();
    }

    public MyAlertDialog(Context context, OnSelectListener onSelectListener) {
        super(context);
        this.context = context;
        this.onSelectListener = onSelectListener;
        dialogSelectPicture();
    }

    public static void dialogAlert(Context context, OnClickPositiveListener onClickPositiveListener) {
        MyAlertDialog myAlertDialog = new MyAlertDialog(context, onClickPositiveListener);
        myAlertDialog.show();
    }

    public static void dialogAddChemical(Context context, OnClickPositiveAddSubstanceListener onClickPositiveAddSubstanceListener) {
        MyAlertDialog myAlertDialog = new MyAlertDialog(context, onClickPositiveAddSubstanceListener);
        myAlertDialog.show();
    }

    public static void dialogEditChemical(Context context, String name, int ppm,
                                          OnClickPositiveAddSubstanceListener onClickPositiveAddSubstanceListener) {
        MyAlertDialog myAlertDialog = new MyAlertDialog(context, onClickPositiveAddSubstanceListener);
        myAlertDialog.setName(name);
        myAlertDialog.setPPM(ppm);
        myAlertDialog.show();
    }

    public static void dialogSelect(Context context, OnSelectListener onSelectListener) {
        MyAlertDialog myAlertDialog = new MyAlertDialog(context, onSelectListener);
        myAlertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void dialogAddChemical() {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_alert_add_substance, null);
        setView(view);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setCanceledOnTouchOutside(false);
        txtMessage = (TextView) view.findViewById(R.id.textviewMessage);
        txtPositiveButton = (TextView) view.findViewById(R.id.textviewOk);
        txtNegativeButton = (TextView) view.findViewById(R.id.textviewCancel);
        edtName = (EditText) view.findViewById(R.id.edittextName);
        edtPPM = (EditText) view.findViewById(R.id.edittextPPM);

        txtPositiveButton.setOnClickListener(this);
        txtNegativeButton.setOnClickListener(this);

    }

    private void dialogAlert() {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_alert, null);
        setView(view);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setCanceledOnTouchOutside(false);
        txtMessage = (TextView) view.findViewById(R.id.textviewMessage);
        txtPositiveButton = (TextView) view.findViewById(R.id.textviewOk);
        txtNegativeButton = (TextView) view.findViewById(R.id.textviewCancel);

        txtPositiveButton.setOnClickListener(this);
        txtNegativeButton.setOnClickListener(this);

    }

    private void dialogSelectPicture() {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_select_picture, null);
        setView(view);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setCanceledOnTouchOutside(false);
        txtNegativeButton = (TextView) view.findViewById(R.id.textviewCancel);
        TextView txtGallery = (TextView) view.findViewById(R.id.textviewGallery);
        TextView txtTakePhoto = (TextView) view.findViewById(R.id.textviewCamera);

        txtNegativeButton.setOnClickListener(this);
        txtGallery.setOnClickListener(this);
        txtTakePhoto.setOnClickListener(this);

    }

    private void setName(String name) {
        edtName.setText(name);
    }

    private void setPPM(int ppm) {
        edtPPM.setText(String.valueOf(ppm));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textviewOk:
                //for add Substance
                if (onClickPositiveAddSubstanceListener != null) {
                    String name = edtName.getText().toString().trim();
                    String ppm = edtPPM.getText().toString().trim();
                    if (isValidate(name, ppm)) {
                        onClickPositiveAddSubstanceListener.onClicked(name, ppm);
                        dismiss();
                    }
                }
                //for alert
                if (onClickPositiveListener != null) {
                    onClickPositiveListener.onClicked();
                    dismiss();
                }
                break;
            case R.id.textviewCancel:
                if (onSelectListener != null) {
                    onSelectListener.cancel();
                }
                dismiss();
                break;
            case R.id.textviewGallery:
                if (onSelectListener == null) {
                    return;
                }
                dismiss();
                onSelectListener.selectGallery();
                break;
            case R.id.textviewCamera:
                if (onSelectListener == null) {
                    return;
                }
                dismiss();
                onSelectListener.selectTakePhoto();
                break;
            default:
                break;
        }
    }

    private boolean isValidate(String name, String ppm) {
        boolean valid = false;
        if (name.isEmpty()) {
            edtName.setError("กรุณากรอก");
            edtName.requestFocus();
        } else if (ppm.isEmpty()) {
            edtPPM.setError("กรุณากรอก");
            edtPPM.requestFocus();
        } else {
            valid = true;
        }
        return valid;
    }

    public interface OnClickPositiveAddSubstanceListener {
        void onClicked(String name, String ppm);
    }

    public interface OnClickPositiveListener {
        void onClicked();
    }

    public interface OnSelectListener {
        void selectGallery();

        void selectTakePhoto();

        void cancel();
    }
}
