package com.mahama.common.utils;

public class ModBusUtil {
    private static final int meterAddress = 1;

    public static byte[] getWriteTop(int code, int address, int count) {
        return ByteUtil.concatAll(new byte[]{
                        (byte) meterAddress,
                        (byte) code
                },
                ByteUtil.intToShortBytes(address),
                new byte[]{
                        (byte) (count >> 8),
                        (byte) count,
                        (byte) (count * 2)
                }
        );
    }

    public static byte[] getWriteHex(int code, int address, byte[] data) {
        byte[] top = getWriteTop(code, address, data.length / 2);
        byte[] crcData = ByteUtil.concatAll(top, data);
        return ByteUtil.concatAll(top, data, CRCUtil.getCrc16(crcData));
    }

    public static byte[] getReadTop(int code, int address, int count) {
        return ByteUtil.concatAll(new byte[]{
                        (byte) meterAddress,
                        (byte) code
                },
                ByteUtil.intToShortBytes(address),
                new byte[]{
                        (byte) (count >> 8),
                        (byte) count
                }
        );
    }

    public static byte[] getReadHex(int code, int address, int count) {
        byte[] top = getReadTop(code, address, count);
        byte[] crc = CRCUtil.getCrc16(top);
        return ByteUtil.concatAll(top, crc);
    }

    public static byte[] getReadResult2Hex(byte[] data) {
        var crc = CRCUtil.getCrc16(ByteUtil.subBytes(data, 0, data.length - 2));
        var dataCrc = ByteUtil.subBytes(data, data.length - 2, 2);
        if (ByteUtil.bytesToHexString(crc).equals(ByteUtil.bytesToHexString(dataCrc))) {
            return ByteUtil.subBytes(data, 3, data.length - 3 - 2);
        } else {
            return null;
        }
    }

    public static byte[] getReadResult2Hex(String val) {
        return getReadResult2Hex(ByteUtil.hexToBytes(val));
    }

    public static String getReadResult2Str(String val) {
        return ByteUtil.bytesToHexString(getReadResult2Hex(val));
    }
}
