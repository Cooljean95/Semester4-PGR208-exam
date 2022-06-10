package com.example.prg208_android_programming_exam

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.prg208_android_programming_exam.fragment_3.FragmentThree

class FeedReaderDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    //This creates the table that is used to store the images and its information.
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table Images (imageId Integer PRIMARY KEY,  imageUri text, imageLink text ,image blob)")
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("drop table if exists Images")
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "ImageManager.db"
    }

}