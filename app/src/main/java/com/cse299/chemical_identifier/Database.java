package com.cse299.chemical_identifier;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class Database extends SQLiteOpenHelper {
    private int id ;

    SQLiteDatabase sqLiteDatabase;
    private static final String DATABASE_NAME = "CHEMICAL.db";
    private static final String TABLE_NAME = "CHEMICAL_DETAILS";
    private static final String ID = "_ID";
    private static final String NAME = "CHEMICAL_NAME";
    private static final String CONCENTRATION1 = "CONCENTRATION";
    private static final String TYPE1 = "TYPE";
    private static final String LOCATION = "SHELF_LOCATION";
    private static final String DANGER = "DANGEROUS_SHELF";
    private static final String DLEVEL = "DANGER_LEVEL";
    private static final int VERSION_NUMBERR = 1 ;
    private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+NAME+" VARCHAR(100),"+CONCENTRATION1+" DOUBLE(1,1),"+TYPE1+" VARCHAR(10),"+LOCATION+" VARCHAR(5),"+DANGER+" VARCHAR(5),"+DLEVEL+" INTEGER(2));";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " +TABLE_NAME;
    //private static final String SELECT_ALL = "SELECT * FROM " + TABLE_NAME;
    private Context context;

    public Database(Context context) {
        super(context,DATABASE_NAME,null, VERSION_NUMBERR);
        this.context = context;
        sqLiteDatabase = this.getWritableDatabase() ;
        id = 0 ;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            Toast.makeText(context,"ON CREATE IS CALLED ",Toast.LENGTH_SHORT).show();
            sqLiteDatabase.execSQL(CREATE_TABLE);
        }catch (Exception e)
        {
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        try {
            Toast.makeText(context,"ON UPGRADE IS CALLED ",Toast.LENGTH_SHORT).show();
            sqLiteDatabase.execSQL(DROP_TABLE);
            onCreate(sqLiteDatabase);
        }catch (Exception e)
        {
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
        }


    }
    public long insertData(String CHEMICAL_NAME,String  CONCENTRATION,String TYPE,String SHELF_LOCATION,String DANGEROUS_LOCATION,String DANGER_LEVEL)
    {
        ContentValues contentValues =new ContentValues();
        contentValues.put(NAME,CHEMICAL_NAME);
        contentValues.put(CONCENTRATION1,CONCENTRATION);
        contentValues.put(TYPE1,TYPE);
        contentValues.put(LOCATION,SHELF_LOCATION);
        contentValues.put(DANGER,DANGEROUS_LOCATION);
        contentValues.put(DLEVEL,DANGER_LEVEL);
        return sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
    }

    public Cursor DISPLAY_ALL_DATA(){
        return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME,null);
    }

    public Cursor searchItem(String name){
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME + " WHERE CHEMICAL_NAME=\""+name+"\"",null);
        return c ;
    }

    public boolean delete(String c) {
            sqLiteDatabase.execSQL("DELETE FROM "+TABLE_NAME + " WHERE CHEMICAL_NAME=\""+c+"\"");
            return true ;
    }
}

