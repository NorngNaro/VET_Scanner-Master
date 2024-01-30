package com.udaya.virak_buntham.vetpickup.bluetoothprinter.km_mode;


public class KmCreate {

    public int connectType = 0;//0:未连接 1:经典 2:BLE

    public String connectName = "";//当前连接名称

    private KmCreate(){
    }

    public static KmCreate getInstance(){
        return SingletonHolder.instance;
    }

    //静态内部类确保了在首次调用getInstance()的时候才会初始化SingletonHolder，从而导致实例被创建。
    //并且由JVM保证了线程的安全。
    private static class SingletonHolder{
        private static final KmCreate instance = new KmCreate();
    }
}
