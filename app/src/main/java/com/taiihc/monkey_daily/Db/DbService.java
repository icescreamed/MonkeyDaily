package com.taiihc.monkey_daily.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import com.taiihc.monkey_daily.Utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class DbService {
    private static SQLiteOpenHelper sqLiteOpenHelper;
    public static final String TABLE_COLLECTION = "Collection";
    public static final String DB_MD = "MonKeyDaily.db";
    public static SQLiteDatabase db;

    public static void insertData(Context context, final int id, final String content, final InsertListener listener){
        initIfNull(context);
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPostExecute(Void aVoid) {
               listener.onSuccess();
            }

            @Override
            protected Void doInBackground(Void... params) {
                ContentValues values = new ContentValues();
                values.put(MySQLiteOpenHelper.COLLECTION_ID,id);
                values.put(MySQLiteOpenHelper.COLLECTION_CONTENT,content);
                db.insert(TABLE_COLLECTION,null,values);
                return null;
            }
        }.execute();

    }
    private static void initIfNull(Context context){
        if(sqLiteOpenHelper==null){
            sqLiteOpenHelper = new MySQLiteOpenHelper(context,DB_MD,null,1);
        }
        if(db==null){
            db = sqLiteOpenHelper.getWritableDatabase();
        }
    }
    public static void getAllData(Context context, final QueryListener listener){
        initIfNull(context);
        final List<String> contents = new ArrayList<>();
        new AsyncTask<Void,Void,List<String>>(){
           @Override
           protected void onPostExecute(List<String> strings) {
               listener.onSuccess(strings);
           }

           @Override
           protected void onPreExecute() {
               super.onPreExecute();
           }

           @Override
           protected List<String> doInBackground(Void... params) {
               Cursor cursor = db.query(TABLE_COLLECTION,null,null,null,null,null,null,null);
               if(cursor.moveToFirst()){
                   do{
                       String content = cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.COLLECTION_CONTENT));
                       contents.add(content);
                   }while (cursor.moveToNext());
               }
               if(!ListUtils.isEmpty(contents)){
                   return  contents;
               }else {
                   return null;
               }
           }
       }.execute();

    }

    public interface InsertListener{

        void onSuccess();

    }

    public interface QueryListener{
        void onStart();
        void onSuccess(List<String> contents);

    }

}
