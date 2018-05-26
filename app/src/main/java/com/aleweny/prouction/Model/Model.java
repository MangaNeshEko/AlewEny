package com.aleweny.prouction.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by ${Abel_Tilahun} on ${4/9/2018}.
 */
public class Model extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recovery_database_2.dp";
    private static  final String DATABASE_TABLE = "phone_Table";
    public static final String Col_1 = "ID";
//    public static final String COL_ = "PHONE_NUMBER";
    private static final String COL_2 = "RECEIVER_NUMBER";
    private static final String COL_3 = "SECURITY_CODE";

    public Model(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DATABASE_TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, RECEIVER_NUMBER TEXT, SECURITY_CODE TEXT  )" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }


    public boolean insertData( String phoneNumberRecovery, String securityCodePassed){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_2, phoneNumberRecovery);
        cv.put(COL_3, securityCodePassed);

        System.out.println("The phone entered:- "+ phoneNumberRecovery);
        long result = db.insert(DATABASE_TABLE, null, cv);

        if(result == -1){
            return false;
        }else{
            return true;
        }

//        return true;
    }

    public Cursor showAllData(){
        //Done when all the data from the database is wanted to be showed.
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor databaseResult = db.rawQuery("SELECT * FROM "+ DATABASE_TABLE, null);
        return  databaseResult;
    }

    //Retrieve Data from SQL database;
    public Cursor retriveData(int ROWID){
        //What does the Cursor class,
        //Its an interface that have the ability to read and write data
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM "+DATABASE_TABLE + " WHERE ID ="+ ROWID, null);
        return result;
    }

    public Cursor retriveDataPhoneNumber(Context context){
        //What does the Cursor class,
        //Its an interface that have the ability to read and write data
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT "+ COL_2 + " FROM " + DATABASE_NAME , null);
        if(result == null){
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    public Cursor retriveDataSecurity(Context context){
        //What does the Cursor class,
        //Its an interface that have the ability to read and write data
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT "+ COL_3 + " FROM " + DATABASE_NAME , null);
        if(result == null){
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }
        return result;
    }
    public Cursor retriveRow(Context context){
        //What does the Cursor class,
        //Its an interface that have the ability to read and write data
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT "+ Col_1 + " FROM " + DATABASE_NAME , null);
        if(result == null){
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }
        return result;
    }


}
