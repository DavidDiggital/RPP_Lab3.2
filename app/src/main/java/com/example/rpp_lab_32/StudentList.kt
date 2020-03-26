package com.example.rpp_lab_32

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import androidx.appcompat.app.AppCompatActivity


class StudentList : AppCompatActivity() {
    private var db: SQLiteDatabase? = null
    private var database: DatabaseHelper? = null
    private var cursor: Cursor? = null
    private var listView: ListView? = null
    private val TAG = StudentList::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        database = DatabaseHelper(applicationContext)
        listView = findViewById(R.id.list)
    }

    override fun onResume() {
        super.onResume()
        db = database!!.readableDatabase
        cursor = db!!.rawQuery("select * from " + DatabaseHelper.TABLE_NAME, null)
        val data = arrayOf(
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_LAST_NAME,
            DatabaseHelper.COLUMN_FIRST_NAME,
            DatabaseHelper.COLUMN_PATRONYMIC,
            DatabaseHelper.COLUMN_TIME_TO_ADD
        )
        cursor!!.moveToFirst()
        val adapter = SimpleCursorAdapter(
            this, R.layout.list_item,
            cursor, data, intArrayOf(R.id.text1, R.id.text2, R.id.text3, R.id.text4, R.id.text5), 0
        )
        listView!!.adapter = adapter
    }

    public override fun onDestroy() {
        super.onDestroy()
        db!!.close()
        cursor!!.close()
    }
}