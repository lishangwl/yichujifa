package com.xieqing.codeutils.util;

import java.util.ArrayList;

public class ArrayUtils{
    /*
    *  Array To List
    * */
    public static ArrayList<?> toArray(Object[] array){
        ArrayList<Object> arrayList= new ArrayList<>();
        for (int i=0;i<array.length;i++){
            arrayList.add(array[i]);
        }
        return arrayList;
    }

    /*
     *  List To Array
     * */
    public static Object[] toArray(ArrayList<Object> arrayList){
        Object[] array= new Object[arrayList.size()];
        for (int i=0;i<arrayList.size();i++){
            array[i] = arrayList.get(i);
        }
        return array;
    }
}
