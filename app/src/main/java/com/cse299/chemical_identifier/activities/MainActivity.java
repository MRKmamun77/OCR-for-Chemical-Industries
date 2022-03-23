package com.cse299.chemical_identifier.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cse299.chemical_identifier.Database;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.cse299.chemical_identifier.R;

public class MainActivity extends AppCompatActivity {
    private Database database ;
    private CompoundButton useFlash;
    private TextView statusMessage;
    private TextView textValue;
    private Button copyButton;
    private Button dataBaseButton;
    private Button searchButton1;
    private static final int RC_OCR_CAPTURE = 9003;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        database = new Database(this) ;
        statusMessage = (TextView)findViewById(R.id.status_message);
        textValue = (TextView)findViewById(R.id.text_value);
        useFlash = (CompoundButton) findViewById(R.id.use_flash);
        dataBaseButton = (Button) findViewById(R.id.dataBaseButton);
        searchButton1 = (Button) findViewById(R.id.searchButton);
        dataBaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openActivityDB();
            }
        });
        searchButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = database.searchItem(textValue.getText().toString()) ;
                if(cursor!=null){
                    StringBuilder stringBuffer = new StringBuilder();
                    while(cursor.moveToNext())
                    {
                        stringBuffer.append("CHEMICAL_NAME : <b>"+cursor.getString(1)+"</b>\n\n");
                        stringBuffer.append("SHELF_LOCATION : <b>"+cursor.getString(4)+"</b>\n");
                        stringBuffer.append("DANGEROUS_LOCATION : <b>"+cursor.getString(5)+"</b>\n");
                        stringBuffer.append("DANGER_LEVEL : <b>"+cursor.getString(6)+"</b>\n\n");
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Search Result");
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        builder.setMessage(Html.fromHtml(stringBuffer.toString(), Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        builder.setMessage(Html.fromHtml(stringBuffer.toString())) ;
                    }
                    builder.setCancelable(true);
                    builder.show();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Search Result");
                    builder.setMessage("NO results found");
                    builder.setCancelable(true);
                    builder.show();
                }
            }
        });


        Button readTextButton = (Button) findViewById(R.id.read_text_button);
        readTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // launch Ocr capture activity.
                Intent intent = new Intent(getApplicationContext(), OcrCaptureActivity.class);
                intent.putExtra(OcrCaptureActivity.AutoFocus, true);
                intent.putExtra(OcrCaptureActivity.UseFlash, useFlash.isChecked());

                startActivityForResult(intent, RC_OCR_CAPTURE);
            }
        });

        copyButton = (Button) findViewById(R.id.copy_text_button);
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard =
                            (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(textValue.getText().toString());
                } else {
                    android.content.ClipboardManager clipboard =
                            (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", textValue.getText().toString());
                    clipboard.setPrimaryClip(clip);
                }
                Toast.makeText(getApplicationContext(), R.string.clipboard_copy_successful_message, Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (textValue.getText().toString().isEmpty()) {
            copyButton.setVisibility(View.GONE);
            searchButton1.setVisibility(View.GONE);


        } else {
            copyButton.setVisibility(View.VISIBLE);
            dataBaseButton.setVisibility(View.GONE);
            searchButton1.setVisibility(View.VISIBLE);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String text = data.getStringExtra(OcrCaptureActivity.TextBlockObject);
                    statusMessage.setText(R.string.ocr_success);
                    textValue.setText(text);
                    Log.d(TAG, "Text read: " + text);
                } else {
                    statusMessage.setText(R.string.ocr_failure);
                    Log.d(TAG, "No Text captured, intent data is null");
                }
            } else {
                statusMessage.setText(String.format(getString(R.string.ocr_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    public void openActivityDB() {
        Intent intent = new Intent(this,DataBaseActivity.class);
        startActivity(intent);

    }


}
