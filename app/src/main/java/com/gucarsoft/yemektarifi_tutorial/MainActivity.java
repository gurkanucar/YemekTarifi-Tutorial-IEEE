package com.gucarsoft.yemektarifi_tutorial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    RecyclerView rcView;
    ImageButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.newFood);
        rcView = findViewById(R.id.recyclerView);

        DB dataBase = new DB(MainActivity.this);
        SQLiteDatabase sqLiteDatabase = dataBase.getReadableDatabase();

        RecyclerViewAdapter rcAdapter = new RecyclerViewAdapter(dataBase.listAll(sqLiteDatabase));
        rcView.setAdapter(rcAdapter);
        rcView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,InsertUpdate.class);
                intent.putExtra("event","insert");
                startActivity(intent);
            }
        });

    }
}