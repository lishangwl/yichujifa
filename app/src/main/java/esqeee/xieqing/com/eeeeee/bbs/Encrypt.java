package esqeee.xieqing.com.eeeeee.bbs;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encrypt {
    public static String encrypt(String str){
        StringBuilder sb=new StringBuilder(str);
        for(int i=0;i<sb.length();i++){
            char c=sb.charAt(i);
            sb.setCharAt(i,(char)(c+i*8));
        }
        sb.reverse();
        return sb.toString();
    }
    public static String decrypt(String str){
        StringBuilder sb=new StringBuilder(str);
        sb.reverse();
        for(int i=0;i<sb.length();i++){
            char c=sb.charAt(i);
            sb.setCharAt(i,(char)(c-i*8));
        }
        return sb.toString();
    }

    public static String md5(String str){
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
            m.update(str.getBytes("UTF8"));
            byte s[] = m.digest();

            final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
            StringBuilder ret = new StringBuilder(s.length * 2);
            for (int i=0; i<s.length; i++) {
                ret.append(HEX_DIGITS[(s[i] >> 4) & 0x0f]);
                ret.append(HEX_DIGITS[s[i] & 0x0f]);
            }
            return ret.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "abcdefghijklstuvwxyz0123456789";
    }
}
