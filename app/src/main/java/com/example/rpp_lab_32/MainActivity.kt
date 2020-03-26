package com.example.rpp_lab_32

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class MainActivity : AppCompatActivity() {
    private var sqlHelper: DatabaseHelper? = null
    private var db: SQLiteDatabase? = null
    private var cursor: Cursor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sqlHelper = DatabaseHelper(applicationContext)
        db = sqlHelper!!.getWritableDatabase()
        onUpdate()
    }

    private fun onUpdate() {
        cursor = db!!.rawQuery("select " + DatabaseHelper.COLUMN_ID.toString() + " from " + DatabaseHelper.TABLE_NAME, null)
        val idList = ArrayList<String>()
        if (cursor!!.moveToFirst()) {
            do {
                val id = cursor!!.getString(0)
                idList.add(id)
            } while (cursor!!.moveToNext())
        }
        cursor!!.close()
        for (id in idList) {
            db!!.delete(DatabaseHelper.TABLE_NAME, "_id = ?", arrayOf(id))
        }
        sqlHelper!!.insertData(db!!)
    }

    fun onClickShow(view: View?) {
        val intent = Intent(this, StudentList::class.java)
        startActivity(intent)
    }

    fun onClickAdd(view: View?) {
        val values = ContentValues()
        val name = sqlHelper!!.getName(db!!)
        val parts = name.split("\\s".toRegex())

        values.put(DatabaseHelper.COLUMN_FIRST_NAME, parts[1])
        values.put(DatabaseHelper.COLUMN_LAST_NAME, parts[0])
        values.put(DatabaseHelper.COLUMN_PATRONYMIC, parts[2])

        db!!.insert(DatabaseHelper.TABLE_NAME, null, values)


    }

    fun onClickChange(view: View?) {
        val values = ContentValues()
        values.put(DatabaseHelper.COLUMN_FIRST_NAME, "Иван")
        values.put(DatabaseHelper.COLUMN_LAST_NAME, "Иванов")
        values.put(DatabaseHelper.COLUMN_PATRONYMIC, "Иванович")

        cursor = db!!.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.COLUMN_ID + " = (SELECT MAX("
                + DatabaseHelper.COLUMN_ID + ") FROM " + DatabaseHelper.TABLE_NAME + ");", null)
        cursor!!.moveToFirst()

        val id = cursor!!.getString(0)
        db!!.update(DatabaseHelper.TABLE_NAME, values, "_id = ?", arrayOf(id))
        cursor!!.close()
    }

//    fun onClickClear(view: View?) {
//        sqlHelper!!.deleteStudent(db!!, cursor!!.getString(0))
//    }
}
