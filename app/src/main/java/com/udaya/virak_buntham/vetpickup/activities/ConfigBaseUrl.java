package com.udaya.virak_buntham.vetpickup.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.preferences.UserSession;

public class ConfigBaseUrl extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_base_url);

        final EditText edtBaseUrl = findViewById(R.id.edt_base_url);
        Button btnConfirm = findViewById(R.id.btn_confirm);

        edtBaseUrl.setText(UserSession.getBaseUrl(getApplication()));
        btnConfirm.setOnClickListener(v -> {
            if (TextUtils.isEmpty(edtBaseUrl.getText())) {
                alertMessageInvalid("Please Fill The Url");
            } else {
                String text = edtBaseUrl.getText().toString();
                if (!text.endsWith("/")) {
                    alertMessageInvalid("Url must end with \"/\"");
                    return;
                }
                UserSession.setBaseUrl(getApplication(), edtBaseUrl.getText().toString());
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void alertMessageInvalid(String strMessage) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_alert_information, null);
        dialogBuilder.setView(dialogView);
        TextView message = dialogView.findViewById(R.id.tv_alert_dialog_message);
        Button buttonOK = dialogView.findViewById(R.id.alert_dialog_button_ok);
        AlertDialog alertDialog = dialogBuilder.create();
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        message.setText(strMessage);
        buttonOK.setText("Ok");
        buttonOK.setOnClickListener(onAlertDialogClickListener(alertDialog));
        alertDialog.show();
    }

    private View.OnClickListener onAlertDialogClickListener(final AlertDialog alertDialog) {
        return v -> {
            if (v.getId() == R.id.alert_dialog_button_ok) {
                alertDialog.dismiss();
            }
        };
    }
}
