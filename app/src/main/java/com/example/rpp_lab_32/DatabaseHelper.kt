package com.example.rpp_lab_32

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*


class DatabaseHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, SCHEMA) {
    private var studentsList: ArrayList<String>? = null

    companion object {
        private const val DATABASE_NAME = "students.db"
        const val TABLE_NAME = "student"
        private const val SCHEMA = 2
        const val COLUMN_ID = "_id"
        const val COLUMN_FULL_NAME = "name"
        const val COLUMN_TIME_TO_ADD = "time"
        const val COLUMN_FIRST_NAME = "first_name"
        const val COLUMN_LAST_NAME = "second_name"
        const val COLUMN_PATRONYMIC = "patronymic"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " ( " +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LAST_NAME + " TEXT, " +
                    COLUMN_FIRST_NAME + " TEXT, " +
                    COLUMN_PATRONYMIC + " TEXT, " +
                    COLUMN_TIME_TO_ADD + " DATETIME DEFAULT CURRENT_TIME" +
                    ");"
        )
    }

    init {
        fillArrayList()
    }

    fun insertData(db: SQLiteDatabase) {
        val random = Random()
        val randomNumbers = ArrayList<Int>()
        var pos: Int
        while (randomNumbers.size != 5) {
            pos = random.nextInt(studentsList!!.size)
            if (randomNumbers.indexOf(pos) == -1) {
                randomNumbers.add(pos)
            }
        }
        val values = ContentValues()
        for (i in randomNumbers) {
            val words = studentsList!![i].split("\\s".toRegex()).toTypedArray()
            values.put(COLUMN_FIRST_NAME, words[1])
            values.put(COLUMN_LAST_NAME, words[0])
            values.put(COLUMN_PATRONYMIC, words[2])
            db.insert(TABLE_NAME, null, values)
        }
    }

    fun getName(db: SQLiteDatabase): String {
        val cursor = db.rawQuery(
            "SELECT " + COLUMN_LAST_NAME + ", " +
                    COLUMN_FIRST_NAME + ", " + COLUMN_PATRONYMIC + " FROM " + TABLE_NAME + ";",
            null
        )
        val names = ArrayList<String>()
        if (cursor.moveToFirst()) {
            do {
                val lastName = cursor.getString(0)
                val patronymic = cursor.getString(2)
                val firstName = cursor.getString(1)
                val name = "$lastName $firstName $patronymic"
                names.add(name)
            } while (cursor.moveToNext())
        }
        cursor.close()
        for (name in studentsList!!) {
            if (names.indexOf(name) == -1) {
                return name
            }
        }
        return studentsList!![0]
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (newVersion > oldVersion) {
            db.execSQL(
                "CREATE TABLE IF NOT EXISTS new_table" +
                        " ( " +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_TIME_TO_ADD + " DATETIME DEFAULT CURRENT_TIME" +
                        ");"
            )
            db.execSQL("insert into new_table (_id, time) select _id, time from student;")
            db.execSQL("ALTER TABLE new_table ADD COLUMN $COLUMN_LAST_NAME TEXT")
            db.execSQL("ALTER TABLE new_table ADD COLUMN $COLUMN_FIRST_NAME TEXT")
            db.execSQL("ALTER TABLE new_table ADD COLUMN $COLUMN_PATRONYMIC TEXT")
            val cursor = db.rawQuery(
                "SELECT " + COLUMN_FULL_NAME + ", " + COLUMN_ID
                        + " FROM " + TABLE_NAME + ";", null
            )
            val names = ArrayList<String>()
            val idList = ArrayList<String>()
            if (cursor.moveToFirst()) {
                do {
                    val name = cursor.getString(0)
                    val id = cursor.getString(1)
                    names.add(name)
                    idList.add(id)
                } while (cursor.moveToNext())
            }
            cursor.close()
            val values = ContentValues()
            for (i in names.indices) {
                val words = names[i].split("\\s").toTypedArray()
                values.put(COLUMN_FIRST_NAME, words[1])
                values.put(COLUMN_LAST_NAME, words[0])
                values.put(COLUMN_PATRONYMIC, words[2])
                db.update("new_table", values, "_id = ?", arrayOf(idList[i]))
            }
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            db.execSQL("ALTER TABLE new_table RENAME TO student")
        }
    }

    private fun fillArrayList() {
        studentsList = ArrayList()
        studentsList!!.add("Алексеев Никита Евгеньевич")
        studentsList!!.add("Баранников Вадим Сергеевич")
        studentsList!!.add("Булыгин Андрей Генадьевич")
        studentsList!!.add("Геденидзе Давид Темуриевич")
        studentsList!!.add("Горак Никита Сергеевич")
        studentsList!!.add("Грачев Александр Альбертович")
        studentsList!!.add("Гусейнов Илья Алексеевич")
        studentsList!!.add("Жарикова Екатерина Сергеевна")
        studentsList!!.add("Журавлев Владимир Евгеньевич")
        studentsList!!.add("Загребельный Александр Русланович")
        studentsList!!.add("Иванов Алексей Дмитриевич")
        studentsList!!.add("Карипова Лейсан Вильевна")
        studentsList!!.add("Копотов Михаил Алексеевич")
        studentsList!!.add("Копташкина Татьяна Алексеевна")
        studentsList!!.add("Косогоров Кирилл Станиславович")
        studentsList!!.add("Кошкин Артём Сергеевич")
        studentsList!!.add("Легецкая Светлана Александровна")
        studentsList!!.add("Магомедов Мурад Магамедович")
        studentsList!!.add("Миночкин Антон Андреевич")
        studentsList!!.add("Опарин Иван Алексеевич")
        studentsList!!.add("Паршаков Никита Алексеевич")
        studentsList!!.add("Самохин Олег Романович")
        studentsList!!.add("Сахаров Владислав Игоревич")
        studentsList!!.add("Смирнов Сергей Юрьевич")
        studentsList!!.add("Трошин Дмитрий Вадимович")
        studentsList!!.add("Чехуров Денис Александрович")
        studentsList!!.add("Эльшейх Самья Ахмед")
        studentsList!!.add("Юров Илья Игоревич")
    }
}