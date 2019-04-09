package com.codeayit.devicelib;

import android.os.SystemClock;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.SerialPort;


public class AndroidSerialportApi {

    private static final String TAG = "android_serialport_api";

    //默认读取超时
    private final long def_read_time_out = 3*1000;
    //默认串口地址
    private final String def_serial_port = "/dev/ttyS1";
    //默认波特率
    private final int def_baudrate = 9600;
    //默认数据位
    private final int def_data_bits = 8;
    //默认数据校验  无校验
    private final char def_parity = 'N';
    //默认停止位
    private final int def_stop_bit = 1;




    private SerialPort mSerialPort;

    //串口地址
    private String serialPort;
    //波特率
    private int baudrate;
    //校验类型 取值
    // N  无
    // E  偶
    // O  奇
    private char parity;
    // 数据位
    private int dataBits;
    // 停止位
    private int stopBit;
    // 是否打开
    private boolean isOpened;

    private long readTimeOut;

    public void setReadTimeOut(long readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    private OutputStream mOutputStream;
    private InputStream mInputStream;

    public AndroidSerialportApi() {
        this.serialPort = def_serial_port;
        this.baudrate = def_baudrate;
        this.parity = def_parity;
        this.dataBits = def_data_bits;
        this.stopBit = def_stop_bit;
        this.readTimeOut = def_read_time_out;
    }

    public AndroidSerialportApi(String serialPort, int baudrate) {
        this.serialPort = serialPort;
        this.baudrate = baudrate;
        this.parity = def_parity;
        this.dataBits = def_data_bits;
        this.stopBit = def_stop_bit;
        this.readTimeOut = def_read_time_out;
    }

    public AndroidSerialportApi(String serialPort, int baudrate, char parity, int dataBits, int stopBit) {
        this.serialPort = serialPort;
        this.baudrate = baudrate;
        this.parity = parity;
        this.dataBits = dataBits;
        this.stopBit = stopBit;
    }

    public int open() {
        if (isOpened) {
            return 1;
        }
        try {
            mSerialPort = new SerialPort(new File(serialPort), baudrate, dataBits, stopBit, parity);
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();
            isOpened = true;
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    public byte[] read() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[128];
        long startTime = System.currentTimeMillis();

        int repCount = 5;

        while (true) {
            if (mInputStream.available() == 0) {
                repCount--;
                if (repCount <= 0 && System.currentTimeMillis() - startTime > readTimeOut) {
                    break;
                }
                SystemClock.sleep(10);
            } else {
                int size = mInputStream.read(buffer);
                out.write(buffer, 0, size);
                continue;
            }
            if (out.size() != 0 && mInputStream.available() == 0) {
                break;
            }
        }
        byte[] bytes = out.toByteArray();
        out.close();
        if (bytes.length == 0) {
            Log.d(TAG, "read_time_out:" + readTimeOut);
        } else {
            Log.d(TAG, "read：" + ByteArrToHex(bytes));
        }
        return bytes;

    }

    public byte[] read(int lenght) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[512];
        long startTime = System.currentTimeMillis();
        while (true) {
            if (mInputStream.available() == 0) {
                if (System.currentTimeMillis() - startTime > readTimeOut) {
                    break;
                }
                SystemClock.sleep(10);
            } else {
                int size = mInputStream.read(buffer);
                out.write(buffer, 0, size);
                continue;
            }
            if (out.size() == lenght) {
                break;
            }

        }
        byte[] bytes = out.toByteArray();
        out.close();
        Log.d(TAG, "read：" +ByteArrToHex(bytes));
        return bytes;

    }

    public void write(byte[] data) throws IOException {
        String writeStr = ByteArrToHex(data);
        Log.d(TAG, "write：" + writeStr);
        mOutputStream.write(data);
    }


    public void close() {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
        isOpened = false;
    }


    public boolean isOpened() {
        return isOpened;
    }


    //-------------------------------------------------------
    // 判断奇数或偶数，位运算，最后一位是1则为奇数，为0是偶数
    static public int isOdd(int num)
    {
        return num & 0x1;
    }
    //-------------------------------------------------------
    static public int HexToInt(String inHex)//Hex字符串转int
    {
        return Integer.parseInt(inHex, 16);
    }
    //-------------------------------------------------------
    static public byte HexToByte(String inHex)//Hex字符串转byte
    {
        return (byte)Integer.parseInt(inHex,16);
    }
    //-------------------------------------------------------
    static public String Byte2Hex(Byte inByte)//1字节转2个Hex字符
    {
        return String.format("%02x", inByte).toUpperCase();
    }
    //-------------------------------------------------------
    static public String ByteArrToHex(byte[] inBytArr)//字节数组转转hex字符串
    {
        StringBuilder strBuilder=new StringBuilder();
        int j=inBytArr.length;
        for (int i = 0; i < j; i++)
        {
            strBuilder.append(Byte2Hex(inBytArr[i]));
            strBuilder.append(" ");
        }
        return strBuilder.toString();
    }
    //-------------------------------------------------------
    static public String ByteArrToHex(byte[] inBytArr, int offset, int byteCount)//字节数组转转hex字符串，可选长度
    {
        StringBuilder strBuilder=new StringBuilder();
        int j=byteCount;
        for (int i = offset; i < j; i++)
        {
            strBuilder.append(Byte2Hex(inBytArr[i]));
        }
        return strBuilder.toString();
    }
    //-------------------------------------------------------
    //转hex字符串转字节数组
    static public byte[] HexToByteArr(String inHex)//hex字符串转字节数组
    {
        int hexlen = inHex.length();
        byte[] result;
        if (isOdd(hexlen)==1)
        {//奇数
            hexlen++;
            result = new byte[(hexlen/2)];
            inHex="0"+inHex;
        }else {//偶数
            result = new byte[(hexlen/2)];
        }
        int j=0;
        for (int i = 0; i < hexlen; i+=2)
        {
            result[j]=HexToByte(inHex.substring(i,i+2));
            j++;
        }
        return result;
    }

}
