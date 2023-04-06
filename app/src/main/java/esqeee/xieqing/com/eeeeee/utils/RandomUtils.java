package esqeee.xieqing.com.eeeeee.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {
    public static final String BASE_CHAR = "abcdefghijklmnopqrstuvwxyz";
    public static final String BASE_CHAR_NUMBER = "abcdefghijklmnopqrstuvwxyz0123456789";
    public static final String BASE_NUMBER = "0123456789";

    public static ThreadLocalRandom getRandom() {
        return ThreadLocalRandom.current();
    }

    public static SecureRandom getSecureRandom() {
        try {
            return SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException unused) {
            return null;
        }
    }

    public static Random getRandom(boolean z) {
        return z ? getSecureRandom() : getRandom();
    }

    public static int randomInt(int i, int i2) {
        return getRandom().nextInt(i, i2);
    }

    public static int randomInt() {
        return getRandom().nextInt();
    }

    public static int randomInt(int i) {
        return getRandom().nextInt(i);
    }

    public static long randomLong(long j, long j2) {
        return getRandom().nextLong(j, j2);
    }

    public static long randomLong() {
        return getRandom().nextLong();
    }

    public static long randomLong(long j) {
        return getRandom().nextLong(j);
    }

    public static double randomDouble(double d, double d2) {
        return getRandom().nextDouble(d, d2);
    }

    public static double randomDouble() {
        return getRandom().nextDouble();
    }

    public static double randomDouble(double d) {
        return getRandom().nextDouble(d);
    }

    public static byte[] randomBytes(int i) {
        byte[] bArr = new byte[i];
        getRandom().nextBytes(bArr);
        return bArr;
    }

    public static <T> T randomEle(List<T> list) {
        return randomEle(list, list.size());
    }

    public static <T> T randomEle(List<T> list, int i) {
        return list.get(randomInt(i));
    }

    public static <T> T randomEle(T[] tArr) {
        return randomEle(tArr, tArr.length);
    }

    public static <T> T randomEle(T[] tArr, int i) {
        return tArr[randomInt(i)];
    }

    public static <T> List<T> randomEles(List<T> list, int i) {
        ArrayList arrayList = new ArrayList(i);
        int size = list.size();
        while (arrayList.size() < i) {
            arrayList.add(randomEle(list, size));
        }
        return arrayList;
    }

    public static <T> Set<T> randomEleSet(Collection<T> collection, int i) {
        ArrayList arrayList = new ArrayList(new HashSet(collection));
        if (i <= arrayList.size()) {
            HashSet hashSet = new HashSet(i);
            int size = collection.size();
            while (hashSet.size() < i) {
                hashSet.add(randomEle(arrayList, size));
            }
            return hashSet;
        }
        throw new IllegalArgumentException("Count is larger than collection distinct size !");
    }

    public static String randomString(int i) {
        return randomString("abcdefghijklmnopqrstuvwxyz0123456789", i);
    }

    public static String randomStringUpper(int i) {
        return randomString("abcdefghijklmnopqrstuvwxyz0123456789", i).toUpperCase();
    }

    public static String randomNumbers(int i) {
        return randomString("0123456789", i);
    }

    public static String randomFourNumbers() {
        return (new Random().nextInt(9000) + 1000) + "";
    }

    public static String randomString(String str, int i) {
        StringBuilder sb = new StringBuilder();
        if (i < 1) {
            i = 1;
        }
        int length = str.length();
        for (int i2 = 0; i2 < i; i2++) {
            sb.append(str.charAt(getRandom().nextInt(length)));
        }
        return sb.toString();
    }

    public static int randomNumber() {
        return randomChar("0123456789");
    }

    public static char randomChar() {
        return randomChar("abcdefghijklmnopqrstuvwxyz0123456789");
    }

    public static char randomChar(String str) {
        return str.charAt(getRandom().nextInt(str.length()));
    }

    @Deprecated
    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }

    public static List<Integer> randomUniqueNumbers(int i, int i2, int i3) {
        ArrayList arrayList = new ArrayList();
        while (i <= i2) {
            arrayList.add(new Integer(i));
            i++;
        }
        Collections.shuffle(arrayList);
        ArrayList arrayList2 = new ArrayList();
        for (int i4 = 0; i4 < i3; i4++) {
            arrayList2.add((Integer) arrayList.get(i4));
        }
        return arrayList2;
    }

    public static int getRandom(int start,int end){
        int random = (int) ((Math.random() * ((double) ((end - start) + 1))) + ((double) start));
        return random;
    }
}