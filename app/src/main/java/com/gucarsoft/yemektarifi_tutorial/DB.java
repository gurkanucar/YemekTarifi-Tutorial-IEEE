package com.gucarsoft.yemektarifi_tutorial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DB extends SQLiteOpenHelper {

    private static final String DB_NAME = "database";
    private static final int VERSION =1;
    private static String[] columns = {"id","name","ingredients","recipe","image"};

   public DB(Context context){
       super(context,DB_NAME,null,VERSION);
   }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE food(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,ingredients TEXT,recipe TEXT, image BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<Food> listAll(SQLiteDatabase db) throws SQLException{
       List<Food> foodList = new ArrayList<>();
       Cursor cursor = db.query("food",null,null,null,null,null,null);
       while (cursor.moveToNext()){
           Food food = new Food();
           food.setId(cursor.getInt((cursor.getColumnIndex(columns[0]))));
           food.setName(cursor.getString(cursor.getColumnIndex(columns[1])));
           food.setIngredients(cursor.getString(cursor.getColumnIndex(columns[2])));
           food.setRecipe(cursor.getString(cursor.getColumnIndex(columns[3])));
           food.setImage(cursor.getBlob(cursor.getColumnIndex(columns[4])));
           foodList.add(food);
       }
       return foodList;
    }

    public Food findById(SQLiteDatabase db,int id) throws SQLException{
        List<Food> foodList = new ArrayList<>();
        Cursor cursor = db.query("food",null,null,null,null,null,null);
        Food food = new Food();
        while (cursor.moveToNext()){
            if(cursor.getInt((cursor.getColumnIndex(columns[0])))==id) {
                food.setId(cursor.getInt((cursor.getColumnIndex(columns[0]))));
                food.setName(cursor.getString(cursor.getColumnIndex(columns[1])));
                food.setIngredients(cursor.getString(cursor.getColumnIndex(columns[2])));
                food.setRecipe(cursor.getString(cursor.getColumnIndex(columns[3])));
                food.setImage(cursor.getBlob(cursor.getColumnIndex(columns[4])));
                return food;
            }
        }
        return null;
    }

    public void insert(SQLiteDatabase db,Food food) throws SQLException{
        ContentValues contentValues=new ContentValues();
        contentValues.put(columns[1],food.getName());
        contentValues.put(columns[2],food.getIngredients());
        contentValues.put(columns[3],food.getRecipe());
        contentValues.put(columns[4],food.getImage());
        db.insertOrThrow("food",null,contentValues);
    }

    public void update(SQLiteDatabase db,Food food) throws SQLException{
        ContentValues contentValues=new ContentValues();
        contentValues.put(columns[1],food.getName());
        contentValues.put(columns[2],food.getIngredients());
        contentValues.put(columns[3],food.getRecipe());
        contentValues.put(columns[4],food.getImage());
        db.update("food",contentValues,"id="+food.getId(),null);
    }

    public void delete(SQLiteDatabase db,int id) throws SQLException{
       db.delete("food","id="+id,null);
    }


}
