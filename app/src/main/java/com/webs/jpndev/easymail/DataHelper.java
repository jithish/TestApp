package com.webs.jpndev.easymail;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ceino on 26/2/15.
 */
public class DataHelper extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "leapup.db";
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
    private static final String TEXT_TYPE = " TEXT";
    private static final String BLOB_TYPE = " BLOB";
    private static final String LEFT_BRACKET = " (";
    private static final String RIGHT_BRACKET = " )";
    private static final String INTEGER_TYPE = " INTEGER DEFAULT 0";
    private static final String COMMA_SEP = ",";
    private static final String ADD_COLUMN = " ADD COLUMN ";
    private static final String INTEGER_PRIMARY_KEY = " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL";
 
    private static final String FOREIGN_KEY = " FOREIGN KEY";
    private static final String REFERENCES = " REFERENCES";
    private static final String DATE = " DATETIME DEFAULT CURRENT_TIMESTAMP ";
    private static final String TABLE_NAME = "EmailEntry";
    private static final String EmailEntry_ID = "_ID";
    private static final String EmailEntry_EMAIL = "_EMAIL";
    Context context;

    private static final String CREATE_TABLE_LEAPUP_EMAIL= CREATE_TABLE + TABLE_NAME + LEFT_BRACKET +
          EmailEntry_ID+ INTEGER_PRIMARY_KEY + COMMA_SEP +
            EmailEntry_EMAIL+ TEXT_TYPE +
            RIGHT_BRACKET;


    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static DataHelper newInstance(Context context) {
        DataHelper leapUpDataHelper = new DataHelper(context);

        return leapUpDataHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
       
        sqLiteDatabase.execSQL(CREATE_TABLE_LEAPUP_EMAIL);
    }

    public void addEmail(String email) {
        if(isMailExist(email)) {

        } else {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put( EmailEntry_EMAIL, email);
            long id = db.insert(TABLE_NAME, null, values);
          //  LogUtils.LOGD("db", "Added user mailo DB with Id:  " + id);
        }

    }
    public void addEmailListDb(ArrayList<String> maillist) {
        for (String mail : maillist)
            addEmail(mail);

    }
   
   
    public Boolean isMailExist(String mail) {

        Boolean ans = false;
        ArrayList<String>maillist=getAllSavedMails();
        for(String s :maillist)
        {
            if(mail.equalsIgnoreCase(s))
            {
                ans=true;break;
            }
        }
        return ans;
    }
    public ArrayList<String> getAllSavedMails()
    {
        ArrayList<String>maillist=new ArrayList<String>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME , null);
        if(cursor != null) {
            if(cursor.moveToFirst()) {
                do {
                    maillist.add( cursor.getString(cursor.getColumnIndex( EmailEntry_EMAIL)));

                }
                while (cursor.moveToNext());
            }
        }
        return maillist;

    }

  
    /*// Getting users Count
    public int getUsersCount() {
        String countQuery = "SELECT * FROM " + UserEntry.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }*/

    private String getDateTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return (date == null) ? "" : dateFormat.format(date);
    }


















/*    boolean isFirstTime = true;

    public Cursor getAllIssuesCursor() {
        //   +" ORDER BY CASE WHEN ("+IssuesEntry.C_TITLE +" LIKE '%Sample%' )"+"THEN "+ 1+" ELSE "+ 2+" END,"+IssuesEntry.C_PUBLISHED_DATE+" DESC ",null);
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + IssuesEntry.TABLE_NAME + " ORDER BY CASE WHEN (" + IssuesEntry.C_TITLE + " LIKE '%Sample%' )" + "THEN " + 1 + " ELSE " + 2 + " END," + IssuesEntry.C_PUBLISHED_DATE + " DESC ", null);
        //Cursor cursor=getReadableDatabase().rawQuery("SELECT * FROM "+IssuesEntry.TABLE_NAME+" ORDER BY "+IssuesEntry.C_PUBLISHED_DATE+" DESC",null);

        if(isFirstTime) {
            if(cursor.moveToFirst()) {
                do {


                    int idIndex = cursor.getColumnIndex(IssuesEntry.C_OBJECT_ID);
                    int downloadStatusIndex = cursor.getColumnIndex(IssuesEntry.C_DOWNLOAD_STATUS);


                    int downloadStatus = cursor.getInt(downloadStatusIndex);
                    if(downloadStatus == 2 || downloadStatus == 3) {
                        setDownloadStatus(cursor.getString(idIndex), 0);
                    }

                } while (cursor.moveToNext());
            }
            cursor = getReadableDatabase().rawQuery("SELECT * FROM " + IssuesEntry.TABLE_NAME + " ORDER BY " + IssuesEntry.C_PUBLISHED_DATE + " DESC", null);
            isFirstTime = false;
        }
        return cursor;
    }*/

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
