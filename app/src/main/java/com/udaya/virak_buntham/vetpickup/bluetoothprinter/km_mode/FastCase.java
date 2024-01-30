/**
 * 此文件为开发者使用方便提供参考
 * 此文件不在sdk范围内，是为了给开发者更灵活的使用sdk提供方便
 * 起点x，y，宽度width，height单位未标明处均为像素（dot）
 */

package com.udaya.virak_buntham.vetpickup.bluetoothprinter.km_mode;

import android.graphics.Bitmap;

import com.hztytech.printer.sdk.api.AbsCpclCreater;
import com.hztytech.printer.sdk.api.AbsEscCreater;
import com.hztytech.printer.sdk.api.AbsTsplCreater;
import com.hztytech.printer.sdk.api.PrintCommandCreater;
import com.hztytech.printer.sdk.impl.CpclCreater;
import com.hztytech.printer.sdk.impl.EscCreater;
import com.hztytech.printer.sdk.impl.KmpclCreater;
import com.hztytech.printer.sdk.impl.TsplCreater;
import com.hztytech.printer.sdk.utils.CpclBitmaptoCmd;
import com.hztytech.printer.sdk.utils.EscBitmaptoCmd;
import com.hztytech.printer.sdk.utils.HexUtils;
import com.hztytech.printer.sdk.utils.TscBitmaptoCmd;


public class FastCase {

    /**
     * 打印时使用
     */
    public byte[] printCmd = new byte[0];



//-------------------------------------------------TSPL部分指令-更多详情请查看sdk-------------------------------------------------

    /**
     *  打印机使用TSPL指令请参考
     */
    AbsTsplCreater tsplCmd = new TsplCreater();

    /**
     * 清空打印机画板缓存
     * @return
     */
    public FastCase addCls(){
        byte[] addCmd = tsplCmd.crtiClear();
        //此方法为字节数组拼接
        printCmd = HexUtils.byteMerger(printCmd,addCmd);
        return this;
    }

    /**
     * 设置打印区域
     * @param width 打印宽度单位 mm
     * @param height 打印高度 mm
     * @return
     */
    public FastCase addSize(int width, int height){
        byte[] addCmd = tsplCmd.crtiSize(width,height);
        printCmd = HexUtils.byteMerger(printCmd,addCmd);
        return this;
    }

    /**
     * 生成TEXT指令
     * @param x 起始x坐标
     * @param y 起始y坐标
     * @param font 字体名称
     * @param rotate 旋转角度
     * @param xMultiple x倍数
     * @param yMultiple y倍数
     * @param alignment 对齐
     * @param content 打印内容
     * @return
     */

    public FastCase addText(int x, int y, String font, int rotate, int xMultiple, int yMultiple, int alignment, String content)
    {
        byte[] addCmd = tsplCmd.crtiText(x,y,font,rotate,xMultiple,yMultiple,alignment,content);
        printCmd = HexUtils.byteMerger(printCmd,addCmd);
        return this;
    }

    public FastCase addReverse( //生成REVERSE指令
                             int x, //起始x坐标
                             int y, //起始y 坐标
                             int width, //宽度，单位是点
                             int height //高度，单位是点
    ) {
        byte[] addCmd = tsplCmd.crtiReverse(x,y,width,height);
        printCmd = HexUtils.byteMerger(printCmd,addCmd);
        return this;
    }

    public FastCase addBox(//生成BOX
                           int x,//x坐标
                           int y,//y坐标
                           int x_end,//x结束点
                           int y_end,//y结束点
                           int thickness,//线条宽度
                           int radius//弧度半径
    )    {
        byte[] addCmd = tsplCmd.crtiBox(x,y,x_end,y_end,thickness,radius);
        printCmd = HexUtils.byteMerger(printCmd,addCmd);
        return this;
    }

    public FastCase addCircle( //生成CIRCLE指令
                               int x, //起始x坐标
                               int y, //起始y 坐标
                               int diameter, //直径 单位是点
                               int thickness //线条宽度
    )  {
        byte[] addCmd = tsplCmd.crtiCircle(x,y,diameter,thickness);
        printCmd = HexUtils.byteMerger(printCmd,addCmd);
        return this;
    }

    public FastCase addEllipse( //生成ELLIPSE指令
                                int x, //起始x坐标
                                int y, //起始y 坐标
                                int width, //宽度，单位是点
                                int height, //高度，单位是点
                                int thickness //线条宽度
    ){
        byte[] addCmd = tsplCmd.crtiEllipse(x,y,width,height,thickness);
        printCmd = HexUtils.byteMerger(printCmd,addCmd);
        return this;
    }

    public FastCase addWaterMark( //水印WATERMARK N
                                      String density //水印浓度[0,100],其中0代表取消水印
    ){
        byte[] addCmd = tsplCmd.crtiWaterMark(density);
        printCmd = HexUtils.byteMerger(printCmd,addCmd);
        return this;
    }



    /**
     * 生成BAR指令
     * @param x 起始x坐标
     * @param y 起始y坐标
     * @param width 宽度
     * @param height 高度
     * @return
     */
    public FastCase addBar(int x, int y, int width, int height)
    {
        byte[] addCmd = tsplCmd.crtiBar(x,y,width,height);
        printCmd = HexUtils.byteMerger(printCmd,addCmd);
        return this;
    }

    /**
     * 生成BARCODE指令
     * @param x x坐标
     * @param y y坐标
     * @param codeType 条码类型
     * @param height 条码高度
     * @param style 文字样式
     *                  0：不可见
     *                  1：可见居左
     *                  2：可见居中
     *                  3：可见居右
     * @param rotation 旋转角度
     * @param narrow 窄条宽度
     * @param wide 宽条宽度
     * @param alignment 对齐方式
     * @param content 条码内容
     * @return
     */
    public FastCase addBarcode(int x, int y, String codeType, int height, int style, int rotation, int narrow, int wide, int alignment, String content){

        byte[] addCmd = tsplCmd.crtiBarcode(x, y, codeType, height, style, rotation, narrow, wide, alignment, content);
        printCmd = HexUtils.byteMerger(printCmd,addCmd);
        return this;
    }

    /**
     * 生成QRCODE指令
     * @param x 起始x坐标
     * @param y 起始y坐标
     * @param eccLevel  纠错等级 {7,15,25,30}
     * @param cellWidth 条码大小级别
     * @param mode  Auto / manual encode
     * @param rotate 旋转角度
     * @param model  条码版本
     * M1:(默认)，原始版本
     * M2:增强版
     * @param mask 掩膜版的种类，控制二维码的样式 S[0-8] 默认为7
     * @param content 二维码内容
     * @return
     */
    public FastCase addQRCode(int x, int y, String eccLevel, int cellWidth, String mode, int rotate, String model, String mask, String content){

        byte[] addCmd = tsplCmd.crtiQRCode(x, y, eccLevel, cellWidth, mode, rotate, model, mask, content);
        printCmd = HexUtils.byteMerger(printCmd,addCmd);
        return this;
    }

    /**
     * 打印模版
     * @param m 指定要打印多少套标签。
     * @param n 指定每个特定标签集应该打印多少份副本
     * @return
     */
    public FastCase addPrint(int m, int n){
        byte[] addCmd = tsplCmd.crtiPrint(m, n);
        printCmd = HexUtils.byteMerger(printCmd,addCmd);
        return this;
    }




    /**
     * 打印图片
     * @param x 图片x坐标
     * @param y 图片y坐标
     * @param mode 图片打印类型 0默认，正常打印  3，zlib压缩  4，zlib反白压缩
     * @param bitmap 图片
     * @return
     */
    public FastCase addBitmap(int x, int y, int mode, Bitmap bitmap){
        try {
            byte[] addCmd = TscBitmaptoCmd.addImage(x,y,mode,bitmap);
            printCmd = HexUtils.byteMerger(printCmd,addCmd);
        }catch (Exception e){
            e.getStackTrace();
        }
        return this;
    }

    /**
     * 打印图片
     * @param x 图片x坐标
     * @param y 图片y坐标
     * @param mode 图片打印类型 0默认，正常打印  3，zlib压缩  4，zlib反白压缩
     * @param bitmap 图片
     * @param a 默认传2 水印效果
     * @return
     */
    public FastCase addBitmap(int x, int y, int mode, Bitmap bitmap,int a){
        try {
            byte[] addCmd = TscBitmaptoCmd.addImage(x,y,mode,bitmap,a);
            printCmd = HexUtils.byteMerger(printCmd,addCmd);
        }catch (Exception e){
            e.getStackTrace();
        }
        return this;
    }





//-------------------------------------------------CPCL部分指令-更多详情请查看sdk-------------------------------------------------

    /**
     *  打印机使用cpcl指令请参考
     */
    AbsCpclCreater cpclCmd = new CpclCreater();

    /**
     * 新建打印模版
     * @param height 打印模版高度
     * @param copys  打印份数
     * @return
     */
    public FastCase cpcl_addInit(int height, int copys){

        byte[] addCmd = cpclCmd.crtiPageInit(0,200,200,height,copys);
        printCmd = HexUtils.byteMerger(printCmd,addCmd);
        return this;
    }

    /**
     * 设置单位
     * @param unit IN-INCHES 度量单位英寸
     *  IN-CENTIMETERS 度量单位厘米
     *  IN-MILLIMETERS 度量单位毫米
     *  IN-DOTS 度量单位为点
     * @return
     */
    public FastCase cpcl_addUnit(String unit){

        byte[] addCmd = cpclCmd.critUnit(unit);
        printCmd = HexUtils.byteMerger(printCmd,addCmd);
        return this;
    }

    /**
     * 设置打印模版宽度
     * @param width
     * @return
     */
    public FastCase cpcl_addWidth(int width){
        byte[] addCmd = cpclCmd.crtiPW(width);
        printCmd = HexUtils.byteMerger(printCmd,addCmd);
        return this;
    }


    /**
     * 设置字体宽高倍数
     * @param w 宽度 1-3 1到3倍
     * @param h 高度 1-3 1到3倍
     * @return
     */
    public FastCase cpcl_setMag(int w, int h){
        byte[] addCmd = cpclCmd.critSetMag(w,h);
        printCmd = HexUtils.byteMerger(printCmd,addCmd);
        return this;
    }
    /**
     * 文本指令
     * @param cmd 指令类型 (逆时针旋转角度) 默认不旋转
     * TEXT（或T）
     * TEXT90（或 T90）
     * TEXT180（或 T180）
     * TEXT270（或 T270）
     * @param font 0-6(根据打印机文档设置)
     * @param size 0-6(根据打印机文档设置)
     * @param x x坐标
     * @param y y坐标
     * @param data 打印数据
     * @return
     */
    public FastCase cpcl_addText(String cmd, String font, String size, int x, int y, String data){
        byte[] addCmd = cpclCmd.crtiText(cmd,font,size,x,y,data);
        printCmd = HexUtils.byteMerger(printCmd,addCmd);
        return this;
    }

    /**
     * 打印线条
     * @param startx 起点x坐标
     * @param starty 起点y坐标
     * @param endx  终点x坐标
     * @param endy 终点y坐标
     * @param width 线条宽度
     * @return
     */
    public FastCase cpcl_addLine(int startx, int starty, int endx, int endy, int width){
        byte[] addCmd = cpclCmd.crtiLine(startx,starty,endx,endy,width);
        printCmd = HexUtils.byteMerger(printCmd,addCmd);
        return this;
    }

    /**
     *
     * @param isvb 0,1 默认0
     * BARCODE（B）：横向打印条形码。
     * VBARCODE(VB) ：纵向打印条形码
     * @param codetype 条码类型 默认 128
     * UPC-A： UPCA、UPCA2、UPCA5
     * UPC-E： UPCE、UPCE2、UPCE5
     * EAN/JAN-13： EAN13、EAN132、EAN135
     * EAN/JAN-8： EAN8、EAN82、EAN 85
     * Code 39： 39、39C、F39、F39C
     * Code 93/Ext.93： 93
     * Interleaved 2 of 5： I2OF5
     * Interleaved 2 of 5（带checksum）：I2OF5C
     * German Post Code： I2OF5G
     * Code 128（自动）： 128
     * UCC EAN 128： UCCEAN128
     * Codabar： CODABAR、CODABAR16
     * MSI/Plessy： MSI、MSI10、MSI1010、MSI1110
     * Postnet： POSTNET
     * FIM： FIM
     * @param width 窄条单位宽度 默认1
     * @param radio 宽条与窄条单位比例 默认1
     * @param height 条码单位高度
     * @param x x坐标
     * @param y y坐标
     * @param data 条码数据
     * @return
     */
    public FastCase cpcl_addBarcode(boolean isvb, String codetype, int width, String radio, int height, int x, int y, String data){
        byte[] addCmd = cpclCmd.crtiBarcode(isvb,codetype,width,radio,height,x,y,data);
        printCmd = HexUtils.byteMerger(printCmd,addCmd);
        return this;
    }


    /**
     *  打印二维码类型
     * @param isVQ isVQ 是否纵向
     * @param codetype 默认QR
     * @param x x坐标
     * @param y y坐标
     * @param m  选项是 1 或 2。QR Code Model 1 是原始ྟ规范，2增强版 默认1
     * @param n 模块单位宽度 1-32 默认6
     * @param data1 纠错等级 H/极高可高级别 Q/高可靠级别 M/标准级别 L/高密度级别 默认M
     * @param data2 字符模式 N数字 A字母数字 默认 A
     * @param data3 二维码内容 二维码内容
     * @return
     */
    public FastCase cpcl_addQrcode(boolean isVQ, String codetype, int x, int y, int m, int n, String data1, String data2, String data3){
        byte[] addCmd = cpclCmd.crtiQrcode(isVQ,codetype,x,y,m,n,data1,data2,data3);
        printCmd = HexUtils.byteMerger(printCmd,addCmd);
        return this;
    }

    public FastCase cpcl_form(){
        byte[] addCmd = cpclCmd.crtiForm();
        printCmd = HexUtils.byteMerger(printCmd,addCmd);

        return this;
    }
    public FastCase cpcl_print(){
        byte[] addCmd = cpclCmd.crtiPrint();
        printCmd = HexUtils.byteMerger(printCmd,addCmd);
        return this;
    }

    public FastCase cpcl_addBitmap(int x, int y, Bitmap bitmap){

        try {
            byte[] addCmd = CpclBitmaptoCmd.cpcl_imgToInstruct(x,y,bitmap);
            printCmd = HexUtils.byteMerger(printCmd,addCmd);
        }catch (Exception e){
            e.getStackTrace();
        }
        return this;
    }




//-------------------------------------------------ESC部分指令-更多详情请查看sdk-------------------------------------------------
    /**
     *  打印机使用esc指令请参考
     */
    AbsEscCreater escCmd = new EscCreater();

    PrintCommandCreater kmpclCreater = new KmpclCreater();

    /**
     * 打印机初始化
     * @return
     */
    public FastCase esc_addInit(){
        byte[] addCmd = escCmd.crtinit();
        printCmd = HexUtils.byteMerger(printCmd,addCmd);
        return this;
    }

    /**
     * 添加文字
     * @param text
     * @return
     */
    public FastCase esc_addText(String text){


        try {
            printCmd = HexUtils.byteMerger(printCmd,text.getBytes("gbk"));
        }catch (Exception e){

        }

        return this;
    }

    public FastCase esc_addLocation(String text1,String text2){




        try {
            byte[] l1 = escCmd.crtiLocation(10);
            printCmd = HexUtils.byteMerger(printCmd,l1);
            printCmd = HexUtils.byteMerger(printCmd,text1.getBytes("gbk"));
            byte[] l2 = escCmd.crtiLocation(257);
            printCmd = HexUtils.byteMerger(printCmd,l2);
            printCmd = HexUtils.byteMerger(printCmd,text2.getBytes("gbk"));
        }catch (Exception e){

        }

        return this;
    }

    /**
     * 对齐方式
     * @param align 0左对齐 1居中 2右对齐
     * @return
     */
    public FastCase esc_addAlign(int align){
        byte[] addCmd = escCmd.crtiAlign((byte)align);
        printCmd = HexUtils.byteMerger(printCmd,addCmd);
        return this;
    }

    /**
     * 设置字符倍宽倍高
     * @param width 0-7 分别是1-8倍 默认0正常
     * @param height 0-7 分别是1-8倍 默认0正常
     * @return
     */
    public FastCase esc_setCharSize(int width, int height){
        int size = width *16 +height;
        byte[] addCmd = escCmd.crtiSetCharSize((byte)size);
        printCmd = HexUtils.byteMerger(printCmd,addCmd);
        return this;
    }

    /**
     * 打印条码
     * @param width 条码宽度 2 ≤ n ≤ 6
     * @param height 条码高度 1 ≤ height ≤ 255
     * @param font 条码字体 0,48:标准 ASCII 码字符 (12 × 24) 1,49:压缩 ASCII 码字符 (9 × 17)
     * @param loc 条码位置显示0, 48:不打印 1, 49：条码上方 2, 50：条码下方 3, 51：条码上、下方都打印
     * @param m 条码类型 0 ≤ m ≤ 6 65 ≤ m ≤ 73 具体用法参考文档
     * @param data 条码数据
     * @return
     */
    public FastCase esc_barCode(int width, int height, int font, int loc, int m, String data){
        byte[] addCmd = escCmd.crtiBarcode((byte)width,(byte)height,(byte)font,(byte)loc,(byte)m,data.getBytes());
        printCmd = HexUtils.byteMerger(printCmd,addCmd);
        return this;
    }



    /**
     * 打印二维码
     * @param size 二维码大小 1-15
     * @param data 二维码数据
     * @return
     */
    public FastCase esc_qrCode(int size, String data){
        byte[] addCmd = escCmd.crtiQrcode(size,data);
        printCmd = HexUtils.byteMerger(printCmd,addCmd);
        return this;
    }

    /**
     * 打印并走纸
     * @param lines 1-255 走纸的行数
     * @return
     */
    public FastCase esc_feed(int lines){
        byte[] addCmd = escCmd.crtiPrintAndfeed((byte) lines);
        printCmd = HexUtils.byteMerger(printCmd,addCmd);
        return this;
    }

    /**
     * 打印一行
     * @return
     */
    public FastCase esc_print(){
        byte[] addCmd = "\r\n".getBytes();
        printCmd = HexUtils.byteMerger(printCmd,addCmd);
        return this;
    }

    /**
     * 打印一行
     * @return
     */
    public FastCase esc_print1(){
        byte[] addCmd = "\r".getBytes();
        printCmd = HexUtils.byteMerger(printCmd,addCmd);
        return this;
    }

    public FastCase esc_addBitmap(int mode, Bitmap bitmap){
        try {
            byte[] addCmd = EscBitmaptoCmd.esc_imgToInstruct(0,bitmap);
            printCmd = HexUtils.byteMerger(printCmd,addCmd);
        }catch (Exception e){

        }
        return this;
    }


    /**
     * 设置指令模式
     * @param type 0TSPL  1ESC
     * @return
     */
    public FastCase esc_Cmdtype(int type){
        byte[] addCmd = escCmd.crtiCmdtype(type);
        printCmd = HexUtils.byteMerger(printCmd,addCmd);
        return this;
    }

    /**
     * 设置纸张模式
     * @param type 33连续纸 34标签纸
     * @return
     */
    public FastCase esc_Papertype(int type){
        byte[] addCmd = escCmd.crtiPapertype(type);
        printCmd = HexUtils.byteMerger(printCmd,addCmd);
        return this;
    }

    /**
     * 设置模版宽度
     * @param width 单位mm
     * @return
     */
    public FastCase esc_Sizewidth(int width){
        byte[] addCmd = escCmd.crtiSizeWidth(width);
        printCmd = HexUtils.byteMerger(printCmd,addCmd);
        return this;
    }

    /**
     * 读取数据打印机设备号
     * @return
     */
    public FastCase kmcpl_00(){
        byte[] addCmd = kmpclCreater.crtiGetProfile();
        printCmd = HexUtils.byteMerger(printCmd,addCmd);
        return this;
    }

    /**
     * 读取数据设置信息
     * @return
     */
    public FastCase kmcpl_01(){
        byte[] addCmd = kmpclCreater.crtiGetPrintConf();
        printCmd = HexUtils.byteMerger(printCmd,addCmd);
        return this;
    }






}
