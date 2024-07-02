package com.example.product_sale.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.product_sale.models.Pet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "pet_shop.db";
    private static final int DATABASE_VERSION = 1;
    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
// Tạo bảng Shop
        db.execSQL("CREATE TABLE Shop (" +
                "shop_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "address TEXT," +
                "phone TEXT," +
                "email TEXT" +
                ")");

        // Tạo bảng PetType
        db.execSQL("CREATE TABLE PetType (" +
                "type_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "description TEXT" +
                ")");

        // Tạo bảng Pet
        db.execSQL("CREATE TABLE Pet (" +
                "pet_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "type_id INTEGER NOT NULL," +
                "breed TEXT," +
                "age INTEGER," +
                "weight REAL," +
                "price NUMERIC NOT NULL," +
                "description TEXT," +
                "image TEXT," +
                "FOREIGN KEY (type_id) REFERENCES PetType(type_id)" +
                ")");

        // Tạo bảng Orders
        db.execSQL("CREATE TABLE Orders (" +
                "order_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "customer_name TEXT NOT NULL," +
                "customer_phone TEXT NOT NULL," +
                "customer_email TEXT," +
                "order_date TEXT NOT NULL," +
                "total_price NUMERIC NOT NULL," +
                "status TEXT NOT NULL" +
                ")");

        // Tạo bảng OrderPet
        db.execSQL("CREATE TABLE OrderPet (" +
                "order_id INTEGER NOT NULL," +
                "pet_id INTEGER NOT NULL," +
                "quantity INTEGER NOT NULL DEFAULT 1," +
                "PRIMARY KEY (order_id, pet_id)," +
                "FOREIGN KEY (order_id) REFERENCES Orders(order_id)," +
                "FOREIGN KEY (pet_id) REFERENCES Pet(pet_id)" +
                ")");
    }
    public void addSamplePets() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // Add sample pets
            insertPet(db, "Max", 1, "Labrador Retriever", 3, 25.5f, BigDecimal.valueOf(500), "Friendly and energetic", "image_url");
            insertPet(db, "Bella", 2, "Golden Retriever", 2, 23.0f, BigDecimal.valueOf(600), "Playful and loving", "image_url");
            insertPet(db, "Charlie", 3, "German Shepherd", 4, 30.0f, BigDecimal.valueOf(550), "Intelligent and loyal", "image_url");

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    private long insertPet(SQLiteDatabase db, String name, int typeId, String breed, int age, float weight, BigDecimal price, String description, String image) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("type_id", typeId);
        values.put("breed", breed);
        values.put("age", age);
        values.put("weight", weight);
        values.put("price", price.doubleValue()); // Store BigDecimal as double in SQLite
        values.put("description", description);
        values.put("image", image);

        return db.insert("Pet", null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop các bảng cũ nếu tồn tại
        db.execSQL("DROP TABLE IF EXISTS Shop");
        db.execSQL("DROP TABLE IF EXISTS PetType");
        db.execSQL("DROP TABLE IF EXISTS Pet");
        db.execSQL("DROP TABLE IF EXISTS Orders");
        db.execSQL("DROP TABLE IF EXISTS OrderPet");

        // Tạo lại cấu trúc mới
        onCreate(db);
    }
    public List<Pet> getAllPets() {
        List<Pet> pets = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Pet", null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int pet_id = cursor.getInt(cursor.getColumnIndex("pet_id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") int type_id = cursor.getInt(cursor.getColumnIndex("type_id"));
                @SuppressLint("Range") String breed = cursor.getString(cursor.getColumnIndex("breed"));
                @SuppressLint("Range") int age = cursor.getInt(cursor.getColumnIndex("age"));
                @SuppressLint("Range") float weight = cursor.getFloat(cursor.getColumnIndex("weight"));
                // BigDecimal price = cursor.getBigDecimal(cursor.getColumnIndex("price"));
                // Uncomment the above line if you store price as REAL in SQLite
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex("price"));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
                @SuppressLint("Range") String image = cursor.getString(cursor.getColumnIndex("image"));

                // Create a Pet object
                Pet pet = new Pet(pet_id, name, type_id, breed, age, weight, BigDecimal.valueOf(price), description, image);
                pets.add(pet);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return pets;
    }
}
