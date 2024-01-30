package com.udaya.virak_buntham.vetpickup.bluetoothprinter.km_mode;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.hztytech.printer.sdk.km_blebluetooth.KmBlebluetooth;
import com.hztytech.printer.sdk.km_blebluetooth.KmBlebluetoothAdapter;
import com.hztytech.printer.sdk.km_bluetooth.Kmbluetooth;
import com.hztytech.printer.sdk.km_bluetooth.KmbluetoothAdapter;
import com.hztytech.printer.sdk.km_usb.KmUsb;
import com.hztytech.printer.sdk.km_usb.KmUsbAdapter;

import java.util.Date;

public class Send {

    public static long getSeconds(Date date1, Date date2) {
        System.out.println(date2);
        System.out.println(date1);
        long diff =date2.getTime() -date1.getTime();
        return diff / 1000;
    }


    public static void writeData(byte[] data,Activity activity){

        Date startDate = new Date(System.currentTimeMillis());

        if (KmCreate.getInstance().connectType == 1){
            Log.e("", "writeData: connection 1" );

            //此处蓝牙下发
            Kmbluetooth.getInstance().writeData(data,new KmbluetoothAdapter(){
                @Override
                public void writeSuccess() {
                    Date endDate = new Date(System.currentTimeMillis());
                    long time = getSeconds(startDate,endDate);
                 //   String s ="写入成功，数据长度："+ data.length;
                 //   Toast.makeText(activity,s,Toast.LENGTH_SHORT).show();
                }

                @Override
                public void writeFail(String reason) {
                    super.writeFail(reason);
                    System.out.println(reason);
                }
            });
        }

        if (KmCreate.getInstance().connectType == 2){
            Log.e("", "writeData: connection 2 blue" );
            System.out.println("BLE小票---");
            KmBlebluetooth.getInstance().writeData(data,new KmBlebluetoothAdapter(){
                @Override
                public void writeSuccess() {
                    super.writeSuccess();
                    System.out.println("写入成功");
                    Date endDate = new Date(System.currentTimeMillis());
                    long time = getSeconds(startDate,endDate);
                   // String s ="写入成功，数据长度："+ data.length + "耗时："+time+"s";
                  //  Toast.makeText(activity,s,Toast.LENGTH_SHORT).show();
                }
                @Override
                public void writeProgress(int progress) {
                    super.writeProgress(progress);
                    System.out.println("当前进度返回"+progress);
                }
            });
        }

        if (KmCreate.getInstance().connectType == 3){
            Log.e("", "writeData: connection 3 usb" );
            System.out.println("usb");
            KmUsb.getInstance().writeData(data,new KmUsbAdapter(){
                @Override
                public void writeSuccess() {
                    super.writeSuccess();
                    System.out.println("写入成功");
                }
            });
        }
    }

}

