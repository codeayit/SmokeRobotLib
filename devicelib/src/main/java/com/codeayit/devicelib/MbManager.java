//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.codeayit.devicelib;

import android.content.Context;
import android.os.SystemClock;

import com.codeayit.devicelib.modbus.ModbusMaster;
import com.codeayit.devicelib.modbus.exception.ModbusError;

import android_serialport_api.SerialPort;
import java.io.File;
import java.io.IOException;

public class MbManager {

    public static final int CODE_OK = 0;
    public static final int CODE_ERROR = -1;

    public static final String TAG = "MbManager";
    public static final long DEFAULT_TIMEOUT = 5*1000;

    public static MbManager instance;
    private Context mContext;
    private ModbusMaster master;
    private static boolean inited;
    private int myBusId;
    private long myTimeOut;

    public MbManager() {
    }

    public void loadLibrary(){
        System.loadLibrary("serial_port");
    }

    public static MbManager getInstance() {
        if (instance == null) {
            synchronized(MbManager.class) {
                if (instance == null) {
                    instance = new MbManager();
                }
            }
        }

        return instance;
    }

    public static boolean isInited() {
        return inited;
    }

    public boolean init(Context context,int busId,String path, int baudrate){
        return init(context,busId,DEFAULT_TIMEOUT,path,baudrate,8,1);
    }

    /**
     *  初始化
     * @param context
     * @param busId
     * @param timeOut  链接超时时间
     * @param path 串口地址
     * @param baudrate 波特率
     * @param dataBits 数据位
     * @param stopBits 停止位
     * @return
     */
    public boolean init(Context context,int busId,long timeOut,String path, int baudrate, int dataBits, int stopBits) {
        this.mContext = context;
        this.myBusId = busId;
        this.myTimeOut = timeOut;

        try {
            this.master = new ModbusMaster(new SerialPort(new File(path),baudrate,dataBits,stopBits,'N'));
            inited = true;
        } catch (Exception e) {
           e.printStackTrace();
           inited = false;
        }
        return inited;
    }

    /**
     *  读取单个寄存器
     * @param addr
     * @param timeOut 超时时间
     * @return
     */
    public int read_registers_must(int addr,long timeOut) {
        int value = -1;
        long startTime  = SystemClock.elapsedRealtime();
        while(true) {
                 value = read_registers(addr);
                 if (value!=CODE_ERROR){
                     return value;
                 }
                 if (SystemClock.elapsedRealtime()-startTime>=timeOut){
                     return value;
                 }
        }
    }

    /**
     *
     * @param addr
     * @return
     */
    public int read_registers_must(int addr) {
        return read_registers_must(addr,myTimeOut);
    }


    /**
     * 读取两个寄存器
     * @param addr
     * @param timeOut
     * @return
     */
    public int read_registers32_must(int addr,long timeOut) {
        int value = -1;
        long startTime  = SystemClock.elapsedRealtime();
        while(true) {
            value = read_registers32(addr);
            if (value!=CODE_ERROR){
                return value;
            }
            if (SystemClock.elapsedRealtime()-startTime>=timeOut){
                return value;
            }
        }
    }
    public int read_registers32_must(int addr) {
        return read_registers32_must(addr,myTimeOut);
    }



    public int read_registers(int addr) {
        try {
            int value = master.readHoldingRegister(myBusId,addr);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return CODE_ERROR;
    }

    public int read_registers32(int addr) {
        try {
            int value = this.master.readHoldingRegister32(myBusId, addr);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CODE_ERROR;
    }





    public int write_registers_must(int addr, int value,long timeOut) {
        long startTime = SystemClock.elapsedRealtime();
        while(true) {
            int code = write_registers(addr, value);
            if (code!=CODE_ERROR){
                return code;
            }
            if (SystemClock.elapsedRealtime()-startTime>timeOut){
                return CODE_ERROR;
            }
        }
    }

    public int write_registers_must(int addr, int value) {
        return write_registers_must(addr,value,myTimeOut);
    }


    public int write_registers32_must(int addr, int value,long timeOut) {
        long startTime = SystemClock.elapsedRealtime();
        while(true) {
            int code = write_registers32(addr, value);
            if (code==CODE_OK){
                return CODE_OK;
            }
            if (SystemClock.elapsedRealtime()-startTime>=timeOut){
                return CODE_ERROR;
            }
        }
    }

    public int write_registers32_must(int addr, int value) {
        return write_registers32_must(addr,value,myTimeOut);
    }

    public int write_registers(int addr, int value) {
        try {
            master.writeSingleRegister(myBusId, addr, value);
            return CODE_OK;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CODE_ERROR;
    }

    public int write_registers32(int addr, int value) {
        try {
            master.writeSingleRegister32(myBusId, addr, value);
            return CODE_OK;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CODE_ERROR;
    }
}
