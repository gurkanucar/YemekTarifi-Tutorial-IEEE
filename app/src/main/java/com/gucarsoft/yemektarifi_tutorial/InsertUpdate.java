package com.gucarsoft.yemektarifi_tutorial;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class InsertUpdate extends AppCompatActivity {

    EditText foodName;
    EditText foodIngredients;
    EditText foodRecipe;
    ImageView imageButton;
    Button save;
    Button delete;
    boolean imageSelected;
    Bitmap image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_update);

        foodName = findViewById(R.id.foodName);
        foodIngredients = findViewById(R.id.foodIngredients);
        foodRecipe = findViewById(R.id.foodRecipe);
        imageButton = findViewById(R.id.imageButton);
        save = findViewById(R.id.saveButton);
        delete = findViewById(R.id.deleteButton);

        if (!getIntent().getStringExtra("event").equals("insert")) {
            fill();
            imageSelected=true;
        }
        else {
            imageSelected=false;
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Pick image"), 1);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageSelected == true && foodName.getText().toString().length() > 0 && foodRecipe.getText().toString().length() > 0 && foodIngredients.getText().toString().length() > 0) {
                    insertOrUpdate();
                }
                else{
                    Toast.makeText(InsertUpdate.this, "Fill the blanks!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getIntent().getStringExtra("event").equals("insert")) {
                  delete();
                }
                else {
                    Toast.makeText(InsertUpdate.this, "You must add food first!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            try {
                InputStream stream = getContentResolver().openInputStream(data.getData());
                image = BitmapFactory.decodeStream(stream);
                imageButton.setImageBitmap(image);
                imageSelected = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void fill() {
        imageSelected = true;
        DB dataBase = new DB(InsertUpdate.this);
        SQLiteDatabase sqLiteDatabase = dataBase.getWritableDatabase();
        Food food = dataBase.findById(sqLiteDatabase, Integer.parseInt(getIntent().getStringExtra("foodId")));
        byte[] bytes = food.getImage();
        image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageButton.setImageBitmap(image);
        foodName.setText(food.getName());
        foodIngredients.setText(food.getIngredients());
        foodRecipe.setText(food.getRecipe());
    }

    public void delete() {
        DB dataBase = new DB(InsertUpdate.this);
        SQLiteDatabase sqLiteDatabase = dataBase.getWritableDatabase();
        dataBase.delete(sqLiteDatabase,Integer.parseInt(getIntent().getStringExtra("foodId")));
        Toast.makeText(InsertUpdate.this, "Deleted!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(InsertUpdate.this, MainActivity.class);
        startActivity(intent);
    }

    public void insertOrUpdate() {
        DB dataBase = new DB(InsertUpdate.this);
        SQLiteDatabase sqLiteDatabase = dataBase.getWritableDatabase();
        Food food;
        if (getIntent().getStringExtra("event").equals("insert")) {
            food = new Food();
        } else {
            food = dataBase.findById(sqLiteDatabase, Integer.parseInt(getIntent().getStringExtra("foodId")));
        }

        food.setName(foodName.getText().toString());
        food.setIngredients(foodIngredients.getText().toString());
        food.setRecipe(foodRecipe.getText().toString());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        food.setImage(stream.toByteArray());

        if (getIntent().getStringExtra("event").equals("insert")) {
            dataBase.insert(sqLiteDatabase, food);
        } else {
            dataBase.update(sqLiteDatabase, food);
        }

        Toast.makeText(InsertUpdate.this, "Saved!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(InsertUpdate.this, MainActivity.class);
        startActivity(intent);

    }

}