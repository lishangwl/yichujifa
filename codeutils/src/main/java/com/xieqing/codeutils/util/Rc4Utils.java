package com.xieqing.codeutils.util;

public class Rc4Utils {

    public static String encrypt(String data, String key) {
        if (data == null || key == null) {
            return "";
        }
        try {
            char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
            char[] str = new char[RC4Base(data.getBytes("GBK"), key).length * (2)];
            int k = 0;
            for (byte byte0 : RC4Base(data.getBytes("GBK"), key)) {
                int k2 = k + 1;
                str[k] = hexDigits[(byte0 >>> 4) & 15];
                k = k2 + 1;
                str[k2] = hexDigits[byte0 & 15];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String encrypt2(String var0, String var1) {
        if (var0 != null && var1 != null) {
            Exception var10000;
            label38: {
                int var4;
                byte[] var7;
                char[] var10;
                char[] var12;
                boolean var10001;
                try {
                    var7 = RC4Base(var0.getBytes("GBK"), var1);
                    var10 = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
                    var4 = var7.length;
                    var12 = new char[var4 * 2];
                } catch (Exception var9) {
                    var10000 = var9;
                    var10001 = false;
                    break label38;
                }

                int var2 = 0;

                for(int var3 = 0; var2 < var4; ++var2) {
                    byte var6 = var7[var2];
                    int var5 = var3 + 1;
                    var12[var3] = (char)var10[var6 >>> 4 & 15];
                    var3 = var5 + 1;
                    var12[var5] = (char)var10[var6 & 15];
                }

                try {
                    var0 = new String(var12);
                    return var0;
                } catch (Exception var8) {
                    var10000 = var8;
                    var10001 = false;
                }
            }

            Exception var11 = var10000;
            var11.printStackTrace();
            var0 = "";
        } else {
            var0 = "";
        }

        return var0;
    }

    public static String decrypt(String data, String key) {
        if (data == null || key == null) {
            return "";
        }
        try {
            return new String(RC4Base(HexString2Bytes(data), key), "GBK");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static byte[] encrypt(byte[] data, String key) {
        if (data == null || key == null) {
            return new byte[0];
        }
        try {
            byte[] hexDigits = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70};
            byte[] str = new byte[(RC4Base(data, key).length * 2)];
            int k = 0;
            for (byte byte0 : RC4Base(data, key)) {
                int k2 = k + 1;
                str[k] = hexDigits[(byte0 >>> 4) & 15];
                k = k2 + 1;
                str[k2] = hexDigits[byte0 & 15];
            }
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    public static byte[] decrypt(byte[] data, String key) {
        if (data == null || key == null) {
            return new byte[0];
        }
        try {
            return RC4Base(data, key);
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    private static byte[] RC4Base(byte[] input, String mKkey) {
        int x = 0;
        int y = 0;
        byte[] key = initKey(mKkey);
        byte[] result = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            x = (x + 1) & 255;
            y = ((key[x] & 255) + y) & 255;
            byte tmp = key[x];
            key[x] = key[y];
            key[y] = tmp;
            result[i] = (byte) (input[i] ^ key[((key[x] & 255) + (key[y] & 255)) & 255]);
        }
        return result;
    }

    private static byte[] initKey(String aKey) {
        try {
            byte[] b_key = aKey.getBytes("GBK");
            byte[] state = new byte[256];
            for (int i = 0; i < 256; i++) {
                state[i] = (byte) i;
            }
            int index1 = 0;
            int index2 = 0;
            if (b_key == null || b_key.length == 0) {
                return null;
            }
            for (int i2 = 0; i2 < 256; i2++) {
                index2 = ((b_key[index1] & 255) + (state[i2] & 255) + index2) & 255;
                byte tmp = state[i2];
                state[i2] = state[index2];
                state[index2] = tmp;
                index1 = (index1 + 1) % b_key.length;
            }
            return state;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] HexString2Bytes(String src) {
        try {
            int size = src.length();
            byte[] ret = new byte[(size / 2)];
            byte[] tmp = src.getBytes("GBK");
            for (int i = 0; i < size / 2; i++) {
                ret[i] = uniteBytes(tmp[i * 2], tmp[(i * 2) + 1]);
            }
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    private static byte uniteBytes(byte src0, byte src1) {
        return (byte) (((char) (((char) Byte.decode("0x" + new String(new byte[]{src0})).byteValue()) << 4)) ^ ((char) Byte.decode("0x" + new String(new byte[]{src1})).byteValue()));
    }

}
