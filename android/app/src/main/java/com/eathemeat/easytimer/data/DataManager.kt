package com.eathemeat.easytimer.data

import android.app.Application
import android.telephony.mbms.MbmsErrors.InitializationErrors
import android.util.Log
import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


data class TimeSegment(val startTime:Long,val EndTime:Long,val note:String)

const val TASK_TABLE_NAME = "task"

@Entity(tableName = "${TASK_TABLE_NAME}")
data class Task(
    @PrimaryKey val uid:Long,
    @ColumnInfo(name = "name") val name:String,
    @ColumnInfo(name = "desc") val desc:String,
    @ColumnInfo(name = "segment")
    val timeList:MutableList<TimeSegment>
    )

class TaskConverter {

    @TypeConverter
    fun fromSegmentList(segment: List<TimeSegment>): String {
        val gson = Gson()
        val type = object : TypeToken<List<TimeSegment>>(){}.type
        return gson.toJson(segment,type)
    }

    @TypeConverter
    fun toSegmentList(json: String): List<TimeSegment> {
        val gson = Gson()
        val type = object : TypeToken<List<TimeSegment>>(){}.type
        return gson.fromJson(json,type)
    }

}

@Dao
interface TaskDao {

    @Query("SELECT * FROM $TASK_TABLE_NAME")
    fun getAllTask(): List<Task>

    @Query("SELECT * FROM $TASK_TABLE_NAME WHERE name LIKE :name LIMIT 1")
    fun getTaskByName(names:String): Task

    @Insert
    fun insertTask(task:Task): Unit

    @Delete
    fun delTask(task:Task): Unit

}
@Database(entities = [Task::class],version=1)
abstract class ETDatabase:RoomDatabase() {
    abstract fun taskDao():TaskDao
} 



val TAG = DataManager::class.java.simpleName
class DataManager {

    lateinit var mApp:Application
    var mTaskList = mutableListOf<Task>()

    lateinit var db:ETDatabase


    companion object {
        val sIntance:DataManager = DataManager()

        fun init(app:Application) {
            sIntance.init(app)
        }
    }

    private constructor() {

    }

    private fun check(): Unit {
        if (mApp == null) throw ExceptionInInitializerError("$TAG is not inited")
    }

    private fun init(app:Application) {
        mApp = app
        db = Room.databaseBuilder(mApp,ETDatabase::class.java,"$TASK_TABLE_NAME").build()
        var dao = db.taskDao()
        mTaskList.addAll(dao.getAllTask())
    }

    fun del(task: Task) {
        check()
        Log.d(TAG, "del() called with: task = $task")
        var dao = db.taskDao()
        dao.delTask(task)
        mTaskList.remove(task)
    }

    fun add(task: Task): Unit {
        check()
        Log.d(TAG, "add() called with: task = $task")
        var dao = db.taskDao()
        dao.insertTask(task)
        mTaskList.add(task)
    }
    
    fun commit(task:Task) {
        check()
        Log.d(TAG, "commit() called with: task = $task")
        var dao = db.taskDao()
        dao.insertTask(task)
    }

}
