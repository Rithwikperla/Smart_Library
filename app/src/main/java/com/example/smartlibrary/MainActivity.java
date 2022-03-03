package com.example.smartlibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    Button search;
    EditText BookName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search = findViewById(R.id.searchbutton);
        BookName = findViewById(R.id.edittextbook);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailsIntent = new Intent( MainActivity.this,Details.class);
                String book = BookName.getText().toString();
                book = book.replace(" ","+");
                if (BookName.getText().toString().isEmpty()) {
                    BookName.setError("Please enter search query");
                    return;
                }
                detailsIntent.putExtra("key",book );
                startActivity(detailsIntent);
                finish();

            }
        });

    }
}