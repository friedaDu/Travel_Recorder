package com.example.android.travelrecorder;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.travelrecorder.data.TravelDbHelper;

public class AddTextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_text);
        final EditText mytext = (EditText) findViewById(R.id.addText);
        Button submitText = (Button) findViewById(R.id.textSubmit);
        submitText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "submit",
                        Toast.LENGTH_SHORT).show();
                final String textString = mytext.getText().toString().trim();
                Intent intent = new Intent(AddTextActivity.this, MapsActivity.class);
                intent.putExtra("mtext",textString);
                startActivity(intent);
            }
        });
        Button cancelText = (Button) findViewById(R.id.textCancel);
        submitText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Cancel",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
