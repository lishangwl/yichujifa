package com.xieqing.codeutils.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.IOException;

public class SQLUtils {
    private  static SQLiteDatabase db;

    public static SQLiteDatabase getDb() {
        return db;
    }
    public static void addColumn(String table,String columName,String type){
        db.execSQL("alter table "+table+" add column "+columName+" "+type+";");
    }
    public static  boolean columnExists(String tableName, String columnName) {
        boolean result = false ;
        Cursor cursor = null ;
        try{
            //查询一行
            cursor = db.rawQuery( "SELECT * FROM " + tableName + " LIMIT 0"
                    , null );
            result = cursor != null && cursor.getColumnIndex(columnName) != -1 ;
        }catch (Exception e){
           e.printStackTrace();
        }finally{
            if(null != cursor && !cursor.isClosed()){
                cursor.close() ;
            }
        }

        return result ;
    }
    public static boolean createDBFile(String DBName) {
        boolean result = false;
        File f = new File(DBName);
        if (f.exists()) {
            return false;
        }
        try {
            return f.createNewFile();
        } catch (IOException e) {
            return result;
        }
    }

    public static void openDB(Context context,String DBName) {
        db = context.openOrCreateDatabase(DBName, 0, null);
    }

    public static void delete(String BM, String hz) {
        try {
            db.execSQL("DELETE FROM " + BM + " WHERE " + hz);
        } catch (Exception e) {
        }
    }

    public static void clearTable(String bm){
        try {
            db.execSQL("delete from "+bm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createTable(String bm, String lx) {
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + bm + " (" + lx + ")");
        } catch (Exception e) {
        }
    }

    public static boolean tableIsExits(String TBName) {
        int count = 0;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='" + TBName + "'", null);
            cursor.moveToFirst();
            count = cursor.getInt(0);
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        }
        if (count > 0) {
            return true;
        }
        return false;
    }

    public static void insert(String BM,String nr){
            try {
                db.execSQL("INSERT INTO " + BM + " VALUES (" + nr + ")");
            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    public static void update(String BM, String xz, String yn) {
        try {
            db.execSQL("UPDATE " + BM + " SET " + xz + " WHERE " + yn);
        } catch (Exception e) {
        }
    }
    public static void exce(String sql){
        db.execSQL(sql);
    }
    public static String query(String SQLSen, String SeperatorItem, String SeperatorLine) {
        String tmpvalue = "";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(SQLSen, null);
            tmpvalue = "";
            while (cursor.moveToNext()) {
                int columnCount = cursor.getColumnCount();
                for (int i = 0; i < columnCount; i++) {
                    tmpvalue = tmpvalue + cursor.getString(i) + SeperatorItem;
                }
                tmpvalue = tmpvalue + SeperatorLine;
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        }
        return tmpvalue;
    }

}
