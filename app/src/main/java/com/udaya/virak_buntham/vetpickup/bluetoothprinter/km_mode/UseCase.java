package com.udaya.virak_buntham.vetpickup.bluetoothprinter.km_mode;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.hztytech.printer.sdk.utils.HexUtils;
import com.udaya.virak_buntham.vetpickup.bluetoothprinter.CityPrintActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class UseCase {

//----------------------TSPL头部----------------------
    //常用指令
    public static byte[] tspl_case1(Bitmap bitmap){
        FastCase fastCase = new FastCase();
        fastCase
//                .esc_Cmdtype(0)
                .addCls()
              //  .addBitmap(60,100,5,bitmap)
                .addSize(75,135)
                .addText(50,50,"TSS16.BF2",0,1,1,0,"你好快麦打印机16号字体")
                .addBox(20,20,500,800,2,0)
                .addText(50,100,"TSS24.BF2",0,1,1,0,"你好快麦打印机24号字体")
                .addText(250,50,"TSS16.BF2",0,1,1,0,"你好快麦打印机16号字体")
                .addReverse(240,40,200,40)
                .addBar(50,200,300,3)
                .addBarcode(50,210,"128",100,1,0,2,2,0,"kuaimai123456")
                .addQRCode(50,350,"L",7,"M",0,"M1","S3","kuaimai123456")
                .addCircle(50,550,100,3)
                .addEllipse(200,550,200,100,3)
                .addWaterMark("70kmp")
                .addText(50,660,"TSS24.BF2",0,1,1,0,"你好快麦打印机24号字体水印")
                .addWaterMark("0")
                .addPrint(1,1);
        return fastCase.printCmd;
    }


    //常用指令
    public static byte[] tspl_text16(){
        FastCase fastCase = new FastCase();
        fastCase
//                .esc_Cmdtype(0)
                .addCls()
                .addSize(75,135)
                .addText(50,50,"TSS16.BF2",0,1,1,0,"你好快麦打印机16号字体")
                .addPrint(1,1);
        return fastCase.printCmd;
    }

    //常用指令
    public static byte[] tspl_text24(){
        FastCase fastCase = new FastCase();
        fastCase
//                .esc_Cmdtype(0)
                .addCls()
                .addSize(75,135)
                .addText(50,50,"TSS24.BF2",0,1,1,0,"你好快麦打印机16号字体")
                .addPrint(1,1);
        return fastCase.printCmd;
    }


    //常用指令
    public static byte[] tspl_box(){
        FastCase fastCase = new FastCase();
        fastCase
//                .esc_Cmdtype(0)
                .addCls()
                .addSize(75,135)
                .addBox(20,20,500,800,2,0)
                .addPrint(1,1);
        return fastCase.printCmd;
    }

    //常用指令
    public static byte[] tspl_reverse(){
        FastCase fastCase = new FastCase();
        fastCase
//                .esc_Cmdtype(0)
                .addCls()
                .addSize(75,135)
                .addText(250,50,"TSS16.BF2",0,1,1,0,"你好快麦打印机16号字体")
                .addReverse(240,40,200,40)
                .addPrint(1,1);
        return fastCase.printCmd;
    }

    //常用指令
    public static byte[] tspl_bar(){
        FastCase fastCase = new FastCase();
        fastCase
//                .esc_Cmdtype(0)
                .addCls()
                .addSize(75,135)
                .addBar(50,200,300,3)
                .addPrint(1,1);
        return fastCase.printCmd;
    }

    //常用指令
    public static byte[] tspl_barcode(){
        FastCase fastCase = new FastCase();
        fastCase
//                .esc_Cmdtype(0)
                .addCls()
                .addSize(75,135)
                .addBarcode(50,210,"128",100,1,0,2,2,0,"kuaimai123456")
                .addQRCode(50,350,"L",7,"M",0,"M1","S3","kuaimai123456")

                .addPrint(1,1);
        return fastCase.printCmd;
    }

    //常用指令
    public static byte[] tspl_qrcode(){
        FastCase fastCase = new FastCase();
        fastCase
//                .esc_Cmdtype(0)
                .addCls()
                .addSize(75,135)
                .addQRCode(50,350,"L",7,"M",0,"M1","S3","kuaimai123456")
                .addCircle(50,550,100,3)
                .addEllipse(200,550,200,100,3)
                .addWaterMark("70kmp")
                .addText(50,660,"TSS24.BF2",0,1,1,0,"你好快麦打印机24号字体水印")
                .addWaterMark("0")
                .addPrint(1,1);
        return fastCase.printCmd;
    }

    //常用指令
    public static byte[] tspl_circle(){
        FastCase fastCase = new FastCase();
        fastCase
//                .esc_Cmdtype(0)
                .addCls()
                .addSize(75,135)
                .addCircle(50,550,100,3)
                .addPrint(1,1);
        return fastCase.printCmd;
    }

    //常用指令
    public static byte[] tspl_ellipse(){
        FastCase fastCase = new FastCase();
        fastCase
//                .esc_Cmdtype(0)
                .addCls()
                .addSize(75,135)
                .addEllipse(200,550,200,100,3)
                .addPrint(1,1);
        return fastCase.printCmd;
    }

    //常用指令
    public static byte[] tspl_watermark(){
        FastCase fastCase = new FastCase();
        fastCase
//                .esc_Cmdtype(0)
                .addCls()
                .addSize(75,135)
                .addWaterMark("70kmp")
                .addText(50,660,"TSS24.BF2",0,1,1,0,"你好快麦打印机24号字体水印")
                .addWaterMark("0")
                .addPrint(1,1);
        return fastCase.printCmd;
    }




    //打印图片
    public static byte[] tspl_case2(Activity activity, Bitmap bitmap){

        FastCase fastCase = new FastCase();
        fastCase.addCls()
                .addSize(100, 100)
                .addBitmap(5,5,0, bitmap)
                .addPrint(1,1);

        return fastCase.printCmd;

    }

//----------------------TSPL尾部----------------------

//----------------------CPCL头部----------------------

    //常用指令
    public static byte[] cpcl_case1(){
        FastCase fastCase = new FastCase();
        fastCase.cpcl_addInit(600,1)
                .cpcl_addUnit("IN-DOTS")

                .cpcl_setMag(1,1)
                .cpcl_addText("T","1","0",50,150,"你好快麦打印机24")
                .cpcl_addText("T","4","0",50,200,"你好快麦打印机32")
                .cpcl_setMag(2,1)
                .cpcl_addText("T","1","0",50,250,"2-1你好快麦打印机32")
                .cpcl_setMag(3,3)
                .cpcl_addText("T","1","0",50,300,"3-3你好快麦打印机24")
                .cpcl_addLine(50,380,250,380,5)
                .cpcl_addBarcode(false,"128",1,"1",50,50,390,"kuaimai123456")

//                .cpcl_addLine(50,200,250,200,5)
//                .cpcl_addBarcode(false,"128",1,"1",50,50,350,"kuaimai123456")
                .cpcl_addQrcode(false,"QR",50,450,2,6,"M","A","kuaimai123456")
//                .cpcl_form()
                .cpcl_print();
        return fastCase.printCmd;
    }

    //cpcl图片
    public static byte[] cpcl_case2(Activity activity){

        byte [] kmdyj;
        String base64Str = "";

        try {
            kmdyj = readLocalFile(activity,"");
            base64Str = new String(kmdyj,"gbk");
            System.out.println(base64Str);
        }catch (Exception e){
            e.getStackTrace();
        }


        Bitmap bitmap = base64ToBufferedImage(base64Str);

        FastCase fastCase = new FastCase();
        fastCase.cpcl_addInit(800,1)
                .cpcl_addUnit("IN-DOTS")
                .cpcl_addBitmap(50,50,bitmap)
                .cpcl_print();
        return fastCase.printCmd;
    }
//----------------------TSPL尾部----------------------



//----------------------ESC头部----------------------

    //常用指令
    public static byte[] esc_case1(){
        FastCase fastCase = new FastCase();
        fastCase.esc_addInit()
                .esc_addAlign(1)
                .esc_setCharSize(0,0)
                .esc_addText("快麦打印机居中\r\n")
                .esc_addAlign(0)
                .esc_setCharSize(1,1)
                .esc_addText("快麦打印机放大1-1\r\n")
                .esc_setCharSize(2,2)
                .esc_addText("快麦打印机放大2-2\r\n")
//                .esc_setCharSize(3,3)
//                .esc_addText("快麦打印机放大3-3\r\n")
                .esc_setCharSize(0,0)
                .esc_addLocation("text1","text2\r\n\r\n")
                .esc_setCharSize(0,0)
                .esc_addText("width2条码宽度36mm\r\n")
                .esc_barCode(2,80,1,2,73,"width2abcd")
                .esc_addText("\r\n")

                .esc_addText("width3条码宽度54mm\r\n")
                .esc_barCode(3,80,1,2,73,"width2abcd")
                .esc_addText("\r\n")

                .esc_addText("width4条码宽度72mm\r\n")
                .esc_barCode(4,80,1,2,73,"width2abcd")
                .esc_addText("\r\n")

                .esc_addText("width5条码宽度90mm\r\n")
                .esc_barCode(5,80,1,2,73,"width2abcd")
                .esc_addText("\r\n")

                .esc_print()
                .esc_addText("SIZE-1二维码宽度3mm\r\n")
                .esc_qrCode(1,"kuaimai123456")
                .esc_addText("\r\nSIZE-2-二维码宽度4mm\r\n")
                .esc_qrCode(2,"kuaimai123456")
                .esc_addText("\r\nSIZE-3-二维码宽度7mm\r\n")
                .esc_qrCode(3,"kuaimai123456")
                .esc_addText("\r\nSIZE-4-二维码宽度10mm\r\n")
                .esc_qrCode(4,"kuaimai123456")
                .esc_addText("\r\nSIZE-5-二维码宽度13mm\r\n")
                .esc_qrCode(5,"kuaimai123456")
                .esc_addText("\r\nSIZE-6-二维码宽度16mm\r\n")
                .esc_qrCode(6,"kuaimai123456")
                .esc_addText("\r\nSIZE-7-二维码宽度18mm\r\n")
                .esc_qrCode(7,"kuaimai123456")
                .esc_addText("\r\nSIZE-8-二维码宽度21mm\r\n")
                .esc_qrCode(8,"kuaimai123456")
                .esc_addText("\r\nSIZE-9-二维码宽度24mm\r\n")
                .esc_qrCode(9,"kuaimai123456")
                .esc_addText("\r\nSIZE-10-二维码宽度26mm\r\n")
                .esc_qrCode(10,"kuaimai123456")
                .esc_addText("\r\nSIZE-11-二维码宽度29mm\r\n")
                .esc_qrCode(11,"kuaimai123456")
                .esc_addText("\r\nSIZE-12-二维码宽度31mm\r\n")
                .esc_qrCode(12,"kuaimai123456")
                .esc_addText("\r\nSIZE-13-二维码宽度34mm\r\n")
                .esc_qrCode(13,"kuaimai123456")
                .esc_addText("\r\nSIZE-14-二维码宽度37mm\r\n")
                .esc_qrCode(14,"kuaimai123456")
                .esc_addText("\r\nSIZE-15-二维码宽度39mm\r\n")
                .esc_qrCode(15,"kuaimai123456")
                .esc_print()

                .esc_feed(5);
        return fastCase.printCmd;
    }

    //打印信息
    public static byte[] esc_00(){
        FastCase fastCase = new FastCase();
        fastCase.kmcpl_00();
        return fastCase.printCmd;
    }

    //打印设备号
    public static byte[] esc_01(){
        FastCase fastCase = new FastCase();
        fastCase.kmcpl_01();
        return fastCase.printCmd;
    }

    //esc图片
    public static byte[] esc_case2(Activity activity){
        byte [] kmdyj;
        String base64Str = "";

        try {
            kmdyj = readLocalFile(activity,"");
            base64Str = new String(kmdyj,"gbk");
            System.out.println(base64Str);
        }catch (Exception e){
            e.getStackTrace();
        }


        Bitmap bitmap = base64ToBufferedImage(base64Str);

        FastCase fastCase = new FastCase();
        fastCase.esc_addInit()
                .esc_addBitmap(0,bitmap)
                .esc_feed(5);
        return fastCase.printCmd;
    }

    //---------------读取本地文件---------------
    public static byte[] readLocalFile(Activity activity,String path) throws Exception {
        String fileName;
        if (path.length() == 0){
            fileName = "快麦打印机.txt";
        }else {
            fileName = path;
        }

        InputStream inputStream = activity.getAssets().open(fileName);
        byte[] data = toByteArray(inputStream);
        inputStream.close();
        return data;
    }

    /**
     * 获取打印模版
     * @param activity
     * @param path 文件路径
     * @param dataType 文件数据类型 1：string（明文） 2：base64  3：16进制
     * @return
     */
    public static byte[] geModelocalFile(Activity activity,String path,int dataType){
        byte[] data = new  byte[0];
        try {
            data = readLocalFile(activity,path);
        }catch (Exception e){
            e.getStackTrace();
        }
        if (dataType == 1){
//            return data;
        }
        if (dataType == 2){
            data = Base64.decode(new String(data),1);
        }

        if (dataType == 3){
            data = HexUtils.hexStr2ByteArr(new String(data));
        }
        return data;
    }

    private static byte[] toByteArray(InputStream in) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    //---------------base64转图片---------------
    public  static Bitmap base64ToBufferedImage(String base64) {

        try {
            byte[] imageByte = Base64.decode(base64,1);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
            return bitmap;
        }catch (Exception e){

            return null;
        }
    }

    //常用指令
    public static byte[] mode1(){
        FastCase fastCase = new FastCase();
        fastCase.addCls()
                .addSize(75,50)
                .addText(20,50,"TSS24.BF2",0,1,1,0,"中免线下（8）：")
                .addText(20,100,"TSS16.BF2",0,1,1,0,"0820 08:58:01")
                .addText(20,140,"TSS24.BF2",0,1,1,0,"旅客：唐春霞第1/2票（1）")
                .addText(50,200,"TSS24.BF2",0,1,1,0,"单 1611/整3222  杜世健 李松 1066批")
                .addText(50,270,"TSS24.BF2",0,1,1,0,"提货单号")
                .addBarcode(250,50,"128",70,2,0,2,2,0,"1128431127429")
                .addBarcode(200,250,"128",65,2,0,2,2,0,"2145578568")
                .addPrint(1,1);
        return fastCase.printCmd;
    }

}
