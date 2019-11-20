package com.example.ex11

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class DBHandler(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "UserData"
        val TABLE_USERS = "MovieTable"
        val KEY_ID = "id"
        val KEY_NAME = "username"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //creating table with fields
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT"
                + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS)
        onCreate(db)
    }

    fun getKeyUsername():String{
        Log.d("DEBUG", KEY_NAME)
        return KEY_NAME
    }


    //method to insert data
    fun addUser(id: Int, username: String):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, id)
        contentValues.put(KEY_NAME, username)
        // Inserting Row
        val success = db.insert(TABLE_USERS, null, contentValues)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }

    //method to delete data
    fun deleteUser(id: Int) {
        val db = this.writableDatabase
        //
        db.delete(TABLE_USERS, KEY_ID + "=?", arrayOf(id.toString()))
    }

    fun viewAll():List<String>{
        val userList:ArrayList<String> = ArrayList<String>()
        val selectQuery = "SELECT  * FROM $TABLE_USERS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var userId: Int
        var userName: String
        var userPassword: String

        if (cursor.moveToFirst()) {
            do {
                userId = cursor.getInt(cursor.getColumnIndex("id"))
                userName = cursor.getString(cursor.getColumnIndex("username"))
                userPassword = cursor.getString(cursor.getColumnIndex("password"))
                val user = userId.toString() + userName + userPassword
                userList.add(user)
            } while (cursor.moveToNext())
        }
        return userList
    }

    fun viewAllNames():List<String>{
        val userList:ArrayList<String> = ArrayList<String>()
        val selectQuery = "SELECT  * FROM $TABLE_USERS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var userId: Int
        var userName: String

        if (cursor.moveToFirst()) {
            do {
                userId = cursor.getInt(cursor.getColumnIndex("id"))
                userName = cursor.getString(cursor.getColumnIndex("username"))
                val user = userId.toString() + userName //+ userPassword
                userList.add(user)
            } while (cursor.moveToNext())
        }
        return userList
    }

}

