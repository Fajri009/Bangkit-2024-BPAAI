package com.example.bangkit_2024_bpaai.AdvancedDatabase.StudentUniversity

import android.app.Application
import com.example.bangkit_2024_bpaai.AdvancedDatabase.StudentUniversity.database.StudentDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MyApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { StudentDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { StudentRepository(database.studentDao()) }
}