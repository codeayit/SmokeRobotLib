package com.codeayit.devicelib.modbus;

public class ModbusFunction {
    public static final int READ_COILS = 0x01;
    public static final int READ_DISCRETE_INPUTS = 0x02;
    public static final int READ_HOLDING_REGISTERS = 0x03;
    public static final int READ_INPUT_REGISTERS = 0x04;
    public static final int WRITE_SINGLE_COIL = 0x05;
    public static final int WRITE_SINGLE_COILS = 0x0f;
    public static final int WRITE_SINGLE_REGISTER = 0x06;
    public static final int WRITE_SINGLE_REGISTERS = 0x10;
}
