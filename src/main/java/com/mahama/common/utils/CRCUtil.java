package com.mahama.common.utils;

/**
 * CRC校验码
 */
public class CRCUtil {

    /**
     * 获取CRC校验码，地位在前，高位在后
     *
     * @param arr_buff 原数组
     * @return hexString
     */
    public static String getCRCHexStr(byte[] arr_buff) {
        return ByteUtil.bytesToHexString(getCrc16(arr_buff));
    }

    /**
     * 获取验证码byte数组，基于Modbus CRC16的校验算法
     */
    public static byte[] getCrc16(byte[] arr_buff) {
        int len = arr_buff.length;

        // 预置 1 个 16 位的寄存器为十六进制FFFF, 称此寄存器为 CRC寄存器。
        int crc = 0xFFFF;
        int i, j;
        for (i = 0; i < len; i++) {
            // 把第一个 8 位二进制数据 与 16 位的 CRC寄存器的低 8 位相异或, 把结果放于 CRC寄存器
            crc = ((crc & 0xFF00) | (crc & 0x00FF) ^ (arr_buff[i] & 0xFF));
            for (j = 0; j < 8; j++) {
                // 把 CRC 寄存器的内容右移一位( 朝低位)用 0 填补最高位, 并检查右移后的移出位
                if ((crc & 0x0001) > 0) {
                    // 如果移出位为 1, CRC寄存器与多项式A001进行异或
                    crc = crc >> 1;
                    crc = crc ^ 0xA001;
                } else
                    // 如果移出位为 0,再次右移一位
                    crc = crc >> 1;
            }
        }
        return intToBytes(crc);
    }

    /**
     * 将int转换成byte数组，低位在前，高位在后
     * 改变高低位顺序只需调换数组序号
     */
    private static byte[] intToBytes(int value) {
        byte[] src = new byte[2];
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * 和校验
     */
    public static byte[] getSumCheck(byte[] arr_buff) {
        int sum = 0;
        for (byte b : arr_buff) {
            sum += (b & 0xFF);
        }
        if (sum > 0xFF) {
            sum = ~sum + 1;
        }
        return new byte[]{(byte) (sum & 0xFF)};
    }

    public static void main(String[] args) {
        byte[] bytes = new byte[]{0x55, 0x01, 0x61, 0x00, 0x00, 0x00, 0x03};
        byte[] aaa = getSumCheck(bytes);
        System.out.println(ByteUtil.bytesToHexString(aaa));
    }
}
