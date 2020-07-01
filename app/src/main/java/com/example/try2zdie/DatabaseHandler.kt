package com.example.try2zdie


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException

//creating the database logic, extending the SQLiteOpenHelper base class
class DatabaseHandler(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "LegoDatabase"
        private val TABLE_PART = "PartTable"
        private val ITEM_TYPE = "itemType"
        private val ITEM_ID = "itemId"
        private val QUANTITY_NEED = "quantityNeed"
        private val QUANTITY_HAVE = "quantityHave"
        private val COLOR_ID = "colorId"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //creating table with fields
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_PART + "("
                + ITEM_TYPE + " TEXT,"
                + ITEM_ID + " TEXT,"
                + QUANTITY_NEED + " INTEGER,"
                + QUANTITY_HAVE + " INTEGER,"
                + COLOR_ID + " INTEGER,"
                + "PRIMARY KEY(" + ITEM_ID + "," + COLOR_ID
                + "));" )
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_PART)
        onCreate(db)
    }

    fun resetTable(){
        val db = this.writableDatabase
        db!!.execSQL("DELETE FROM " + TABLE_PART + ";")
        /*val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_PART + "("
                + ITEM_TYPE + " TEXT," + ITEM_ID + " TEXT,"
                + QUANTITY_NEED + " INTEGER," + QUANTITY_HAVE + " INTEGER,"
                + COLOR_ID + " INTEGER," + "PRIMARY KEY(" + ITEM_ID + "," + COLOR_ID + "));" )
        db?.execSQL(CREATE_CONTACTS_TABLE)*/
    }

    fun addPartsProject(items: MutableList<ArrayList<String>>): Boolean {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM " + TABLE_PART)
        for (i in items)
        {
            val contentValues = ContentValues()
            contentValues.put(ITEM_TYPE,i[0])
            contentValues.put(ITEM_ID, i[1])
            contentValues.put(QUANTITY_NEED, i[2])
            contentValues.put(QUANTITY_HAVE, 0)
            contentValues.put(COLOR_ID,i[3])
            this.writableDatabase.insert(TABLE_PART, null, contentValues)
        }
        db.close()
        return true
    }

    //method to read data
    fun viewEmployee():List<PartClass>{
        val empList:ArrayList<PartClass> = ArrayList<PartClass>()
        val selectQuery = "SELECT  * FROM $TABLE_PART"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var itemType: String
        var itemId: String
        var quantNeed: Int
        var quantHave: Int
        var color: Int
        if (cursor.moveToFirst()) {
            do {
                itemType = cursor.getString(cursor.getColumnIndex("itemType"))
                itemId = cursor.getString(cursor.getColumnIndex("itemId"))
                quantNeed = cursor.getInt(cursor.getColumnIndex("quantityNeed"))
                quantHave = cursor.getInt(cursor.getColumnIndex("quantityHave"))
                color = cursor.getInt(cursor.getColumnIndex("colorId"))
                val emp= PartClass(itemType = itemType, itemId= itemId,
                    quantityNeed = quantNeed, quantityHave = quantHave, colorId = color)
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        return empList
    }
    //method to update data
    /*fun updateEmployee(emp: EmpModelClass):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.userId)
        contentValues.put(KEY_NAME, emp.userName) // EmpModelClass Name
        contentValues.put(KEY_EMAIL,emp.userEmail ) // EmpModelClass Email

        // Updating Row
        val success = db.update(TABLE_CONTACTS, contentValues,"id="+emp.userId,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    } */
    //method to delete data
    /*fun deleteEmployee(emp: EmpModelClass):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.userId) // EmpModelClass UserId
        // Deleting Row
        val success = db.delete(TABLE_CONTACTS,"id="+emp.userId,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    } */
}