package com.eathemeat.easytimer.data

import android.app.Application
import android.os.SystemClock
import android.provider.ContactsContract.CommonDataKinds.Nickname
import android.text.TextUtils
import android.util.Log
import androidx.room.*
import com.eathemeat.easytimer.net.HttpApiManager
import com.eathemeat.easytimer.net.user.User
import com.eathemeat.easytimer.util.OtherThread
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*


data class TimeSegment(var startTime:Long,var endTime:Long,var note:String) {
    fun start(): Unit {
        startTime = Calendar.getInstance().timeInMillis
        Log.d(TAG, "start() called  $startTime")
    }

    fun end(): Unit {
        endTime = Calendar.getInstance().timeInMillis
        Log.d(TAG, "end() called ${endTime}")
    }

    fun isEnd() :Boolean {
        return endTime > 0
    }
}

const val TASK_TABLE_NAME = "task"

@Entity(tableName = "${TASK_TABLE_NAME}")
data class Task(
    @PrimaryKey val uid:Long = generateUid(),
    @ColumnInfo(name = "name") val name:String,
    @ColumnInfo(name = "desc") val desc:String,
    @ColumnInfo(name = "segment")
    val timeList:MutableList<TimeSegment>
    ) {
    companion object {
        fun generateUid(): Long {
            var time = Calendar.getInstance().timeInMillis
            return time
        }
    }

}


class SegmentConverter {

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
    fun getTaskByName(name:String): Task

    @Insert
    fun insertTask(task:Task): Unit

    @Delete
    fun delTask(task:Task): Unit

    @Update
    fun updateTask(task:Task):Unit

}
@Database(entities = [Task::class],version=1, exportSchema = false)
@TypeConverters(SegmentConverter::class)
abstract class ETDatabase:RoomDatabase() {
    abstract fun taskDao():TaskDao
} 



val TAG = DataManager::class.java.simpleName
class DataManager {

    lateinit var mApp:Application
    var mTaskList = mutableListOf<Task>()

    private val user:User = User(-1,"","")

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
        OtherThread.sInstance.post{
            var dao = db.taskDao()
            mTaskList.addAll(dao.getAllTask())
        }
    }

    fun del(task: Task) {
        check()
        Log.d(TAG, "del() called with: task = $task")
        mTaskList.remove(task)
        OtherThread.sInstance.post{
            var dao = db.taskDao()
            dao.delTask(task)
        }

    }

    fun add(task: Task): Unit {
        check()
        Log.d(TAG, "add() called with: task = $task")
        mTaskList.add(task)
        OtherThread.sInstance.post {
            var dao = db.taskDao()
            dao.insertTask(task)

        }
    }
    
    fun commit(task:Task) {
        check()
        Log.d(TAG, "commit() called with: task = $task")
        OtherThread.sInstance.post {
            var dao = db.taskDao()
            dao.insertTask(task)
        }
    }

    fun notifyDataChanged(task: Task) {
        check()
        OtherThread.sInstance.post {
            var dao = db.taskDao()
            dao.updateTask(task)
        }
    }

    fun registerUser(passPort:String, passWord:String, phone:String , nickname: String): User {
        if (user.Id >0) return user
        if (TextUtils.isEmpty(passWord) || TextUtils.isEmpty(passPort) || TextUtils.isEmpty(nickname)) throw NullPointerException("something wrpng")
        HttpApiManager.userApi.register(passPort,passWord,phone,nickname)
    }

}
