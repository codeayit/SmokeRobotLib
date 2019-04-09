package com.codeayit.devicelib.modbus.exception;

public enum ModbusErrorType {
    ModbusError,
    ModbusFunctionNotSupportedError,
    ModbusDuplicatedKeyError,
    ModbusMissingKeyError,
    ModbusInvalidBlockError,
    ModbusInvalidArgumentError,
    ModbusOverlapBlockError,
    ModbusOutOfBlockError,
    ModbusInvalidResponseError,
    ModbusInvalidRequestError,

    ModbusTimeoutError
}
