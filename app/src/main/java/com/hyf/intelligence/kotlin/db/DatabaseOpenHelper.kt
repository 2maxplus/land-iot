package com.hyf.intelligence.kotlin.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.hyf.intelligence.kotlin.common.Constant.DBProvider.DATABASE_NAME
import com.hyf.intelligence.kotlin.common.Constant.DBProvider.DATABASE_VERSION

/**
 * 数据库
 * Created by L on 2017/7/17.
 */
class DatabaseOpenHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    //单例
    companion object {
        private var instance: DatabaseOpenHelper? = null

        fun getInstance(c: Context): DatabaseOpenHelper {
            if (instance != null) return instance as DatabaseOpenHelper
            return synchronized(this) {
                if (instance != null) {
                    instance as DatabaseOpenHelper
                } else {
                    instance = DatabaseOpenHelper(c)
                    instance as DatabaseOpenHelper
                }
            }
        }
    }

    //表字段管理器
    private val mDatabaseColumnsHelper = DatabaseColumnsHelper()

    override fun onCreate(db: SQLiteDatabase) {
        createTables(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    private fun createTables(db: SQLiteDatabase) {
        mDatabaseColumnsHelper.getTableNames().map { createTable(db, it) }
    }

    private fun createTable(db: SQLiteDatabase, tableName: String) {
        val columns = mDatabaseColumnsHelper.getCreateTableSql(tableName)
        db.execSQL("create table if not exists $tableName$columns")
    }

}