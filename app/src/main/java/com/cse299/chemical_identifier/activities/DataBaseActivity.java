package com.cse299.chemical_identifier.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cse299.chemical_identifier.Database;
import com.cse299.chemical_identifier.R;


public class DataBaseActivity extends AppCompatActivity implements View.OnClickListener {
    private Database database;
    private EditText CHEMICALNAMEEditText_ID,CONCENTRATIONEditText_ID,TYPEEditText_ID,SHELF_LOCATIONEditText_ID,DANGEROUS_LOCATIONEditText_ID,DANGER_LEVELEditText_ID;    //DATABASE
    private Button UPDATE_ID,DELETE_ID,DISPLAY_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base);
        database = new Database(this);
        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();

        CHEMICALNAMEEditText_ID=(EditText) findViewById(R.id.CHEMICALNAMEtextID);
        CONCENTRATIONEditText_ID=(EditText) findViewById(R.id.CONCENTRATIONtext_ID);
        TYPEEditText_ID=(EditText) findViewById(R.id.TYPEtext_ID);
        SHELF_LOCATIONEditText_ID=(EditText) findViewById(R.id.SHELF_LOCATIONtext_ID);
        DANGEROUS_LOCATIONEditText_ID=(EditText) findViewById(R.id.DANGEROUS_LOCATIONtext_ID);
        DANGER_LEVELEditText_ID=(EditText) findViewById(R.id.DANGER_LEVELtext_ID);
        UPDATE_ID=(Button) findViewById(R.id.UPDATE_ID1);
        DELETE_ID=(Button) findViewById(R.id.DELETE_ID);
        DISPLAY_ID=(Button) findViewById(R.id.DISPLAY_ID_Button1);

        UPDATE_ID.setOnClickListener(this);
        DELETE_ID.setOnClickListener(this);
        DISPLAY_ID.setOnClickListener(this);

    }
    public void onClick(View view) {
        String CHEMICAL_NAME=CHEMICALNAMEEditText_ID.getText().toString();
        String CONCENTRATION=CONCENTRATIONEditText_ID.getText().toString();
        String TYPE=TYPEEditText_ID.getText().toString();
        String SHELF_LOCATION=SHELF_LOCATIONEditText_ID.getText().toString();
        String DANGEROUS_LOCATION=DANGEROUS_LOCATIONEditText_ID.getText().toString();
        String DANGER_LEVEL=DANGER_LEVELEditText_ID.getText().toString();

        if(view.getId()==R.id.UPDATE_ID1)
        {
            if (CHEMICAL_NAME.equals("") && CONCENTRATION.equals("") && TYPE.equals("") && SHELF_LOCATION.equals("")&& DANGEROUS_LOCATION.equals("")&& DANGER_LEVEL.equals("")){
                Toast.makeText(getApplicationContext(),"Please enter all the data",Toast.LENGTH_LONG).show();
        }else{
                long rowId = database.insertData(CHEMICAL_NAME,CONCENTRATION,TYPE,SHELF_LOCATION,DANGEROUS_LOCATION,DANGER_LEVEL);
                if(rowId==-1){
                    Toast.makeText(getApplicationContext(),"Not successful", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Row "+rowId+" is successfully inserted", Toast.LENGTH_LONG).show();
                }
        }

        }

        if(view.getId()==R.id.DELETE_ID) {
            String c = CHEMICALNAMEEditText_ID.getText().toString() ;
            if(c.equals("")){
                Toast.makeText(getApplicationContext(),"Chemical Name field empty",Toast.LENGTH_SHORT).show(); ;
            }
            else{

                Cursor s = database.searchItem(c) ;
                int count = 0 ;
                while (s.moveToNext()){
                    count++ ;
                }
                if(count!=0){
                    database.delete(c) ;
                    Toast.makeText(getApplicationContext(),"Deleted",Toast.LENGTH_SHORT).show(); ;
                }
                else{
                    Toast.makeText(getApplicationContext(),"Chemical Not found",Toast.LENGTH_SHORT).show();
                }
            }

        }
        if(view.getId()==R.id.DISPLAY_ID_Button1)
        {
            Cursor cursor = database.DISPLAY_ALL_DATA();
            if(cursor.getCount()==0)
            {
                //NO DATA
                showData("ERROR","NO DATA FOUND");
                return;
            }
            StringBuilder stringBuffer = new StringBuilder();
            while(cursor.moveToNext())
            {
                stringBuffer.append("ID : "+cursor.getString(0)+"\n");
                stringBuffer.append("CHEMICAL_NAME : "+cursor.getString(1)+"\n");
                stringBuffer.append("CONCENTRATION : "+cursor.getString(2)+"\n");
                stringBuffer.append("TYPE: "+cursor.getString(3)+"\n");
                stringBuffer.append("SHELF_LOCATION : "+cursor.getString(4)+"\n");
                stringBuffer.append("DANGEROUS_LOCATION : "+cursor.getString(5)+"\n");
                stringBuffer.append("DANGER_LEVEL : "+cursor.getString(6)+"\n\n");
            }
            showData("ResultSet",stringBuffer.toString());
        }
    }
    public void showData(String title,String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.show();

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
