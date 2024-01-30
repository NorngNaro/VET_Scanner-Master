package com.udaya.virak_buntham.vetpickup.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AlertDialog;

import android.widget.Button;

import com.udaya.virak_buntham.vetpickup.R;
import com.udaya.virak_buntham.vetpickup.activities.HomeActivity;
import com.udaya.virak_buntham.vetpickup.activities.MoveItemToVanActivity;
import com.udaya.virak_buntham.vetpickup.activities.TransitActivity;
import com.udaya.virak_buntham.vetpickup.activities.goodsTransfer.GoodsTransferActivity;
import com.udaya.virak_buntham.vetpickup.activities.movetovan.ReceiveItemActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AlertDialogUtil {
    public static void alertMessageInput(Context context, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public static void alertMessageInternetConnection(final Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setMessage("No internet connection");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Retry",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public static void alertMessageError(Context context, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public static void alertMessageScan(Context context, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getResources().getString(R.string.close),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public static void dialogAlert(final Context context) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText(context.getResources().getString(R.string.information));
        sweetAlertDialog.setContentText(context.getResources().getString(R.string.do_you_want_exit));
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                context.startActivity(new Intent(context, HomeActivity.class));
            }
        });
        sweetAlertDialog.setCancelButton(context.getResources().getString(R.string.cancel), new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();
            }
        }).show();
        Button btn = (Button) sweetAlertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(Color.parseColor("#f48539"));
        btn.setText(context.getResources().getString(R.string.ok));
    }

    public static void dialogMoneyAlert(final Context context) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText(context.getResources().getString(R.string.moneyTargets));
        sweetAlertDialog.setContentText((GoodsTransferActivity.price) + "0 ៛");

//        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//            @Override
//            public void onClick(SweetAlertDialog sDialog) {
//                sDialog.dismissWithAnimation();
//            }
//        });
        sweetAlertDialog.setCancelButton(context.getResources().getString(R.string.cancel), new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();
            }
        }).show();
        Button btn = (Button) sweetAlertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(Color.parseColor("#f48539"));
        btn.setText(context.getResources().getString(R.string.ok));
    }

    public static void dialogCheckValue(final Context context, String title, String content) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText(title);
        sweetAlertDialog.setContentText(content);
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();
            }
        }).show();
//        sweetAlertDialog.setCancelButton(context.getResources().getString(R.string.cancel), new SweetAlertDialog.OnSweetClickListener() {
//            @Override
//            public void onClick(SweetAlertDialog sDialog) {
//                sDialog.dismissWithAnimation();
//            }
//        }).show();
        Button btn = (Button) sweetAlertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(Color.parseColor("#f48539"));
        btn.setText(context.getResources().getString(R.string.ok));
    }

    public static void dialogAlertBack(final Context context) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText(context.getResources().getString(R.string.information));
        sweetAlertDialog.setContentText(context.getResources().getString(R.string.do_you_want_back));
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                context.startActivity(new Intent(context, MoveItemToVanActivity.class));
            }
        });
        sweetAlertDialog.setCancelButton(context.getResources().getString(R.string.cancel), new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();
            }
        }).show();
        Button btn = (Button) sweetAlertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(Color.parseColor("#f48539"));
        btn.setText(context.getResources().getString(R.string.ok));
    }

    public static void dialogAlertReceiveBack(final Context context) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText(context.getResources().getString(R.string.information));
        sweetAlertDialog.setContentText(context.getResources().getString(R.string.do_you_want_back));
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                context.startActivity(new Intent(context, ReceiveItemActivity.class));
            }
        });
        sweetAlertDialog.setCancelButton(context.getResources().getString(R.string.cancel), new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();
            }
        }).show();
        Button btn = (Button) sweetAlertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(Color.parseColor("#f48539"));
        btn.setText(context.getResources().getString(R.string.ok));
    }

    public static void dialogAlertTransitBack(final Context context) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText(context.getResources().getString(R.string.information));
        sweetAlertDialog.setContentText(context.getResources().getString(R.string.do_you_want_back));
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                context.startActivity(new Intent(context, TransitActivity.class));
            }
        });
        sweetAlertDialog.setCancelButton(context.getResources().getString(R.string.cancel), new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();
            }
        }).show();
        Button btn = (Button) sweetAlertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(Color.parseColor("#f48539"));
        btn.setText(context.getResources().getString(R.string.ok));
    }

    public static void dialogAlertLEDScanBack(final Context context) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText(context.getResources().getString(R.string.information));
        sweetAlertDialog.setContentText(context.getResources().getString(R.string.do_you_want_back));
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                context.startActivity(new Intent(context, HomeActivity.class));
            }
        });
        sweetAlertDialog.setCancelButton(context.getResources().getString(R.string.cancel), new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();
            }
        }).show();
        Button btn = (Button) sweetAlertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(Color.parseColor("#f48539"));
        btn.setText(context.getResources().getString(R.string.ok));
    }

}
