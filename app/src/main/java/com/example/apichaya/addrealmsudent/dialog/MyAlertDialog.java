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

    private OnClickPositiveListener onClickPositiveListener;

    public MyAlertDialog(Context context) {
        super(context);
        this.context = context;
        dialogAddChemical();
    }

    public MyAlertDialog(Context context, OnClickPositiveListener onClickPositiveListener) {
        super(context);
        this.context = context;
        this.onClickPositiveListener = onClickPositiveListener;
        dialogAddChemical();
    }

    public static void dialogAddChemical(Context context, OnClickPositiveListener onClickPositiveListener) {
        MyAlertDialog myAlertDialog = new MyAlertDialog(context, onClickPositiveListener);
        myAlertDialog.show();
    }

    public static void dialogEditChemical(Context context, String name, int ppm,
                                          OnClickPositiveListener onClickPositiveListener) {
        MyAlertDialog myAlertDialog = new MyAlertDialog(context, onClickPositiveListener);
        myAlertDialog.setName(name);
        myAlertDialog.setPPM(ppm);
        myAlertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void dialogAddChemical() {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_alert, null);
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
                dismiss();
                if (onClickPositiveListener != null) {
                    String name = edtName.getText().toString().trim();
                    String ppm = edtPPM.getText().toString().trim();
                    if (isValidate(name, ppm)) {
                        onClickPositiveListener.onClicked(name, ppm);
                    }
                }
                break;
            case R.id.textviewCancel:
                dismiss();
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

    public interface OnClickPositiveListener {
        void onClicked(String name, String ppm);
    }
}
