package com.example.bangkit_2024_bpaai.AdvancedDatabase.StudentUniversity.database

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface StudentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStudent(student: List<Student>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUniversity(university: List<University>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCourse(course: List<Course>)

    //    @Query("SELECT * from student")
//    fun getAllStudent(): LiveData<List<Student>>
//    @RawQuery(observedEntities = [Student::class]) // @RawQuery digunakan untuk menandai bahwa fungsi tersebut menggunakan fitur RawQuery
//    fun getAllStudent(query: SupportSQLiteQuery): LiveData<List<Student>>
    @RawQuery(observedEntities = [Student::class])
    fun getAllStudent(query: SupportSQLiteQuery): DataSource.Factory<Int, Student>

    // Many to One
    @Transaction // dibutuhkan jika Anda menjalankan lebih dari satu query secara bersamaan karena melakukan query pada 2 tabel secara bersamaan
    @Query("SELECT * from student")
    fun getAllStudentAndUniversity(): LiveData<List<StudentAndUniversity>>

    // One to Many
    @Transaction
    @Query("SELECT * from university")
    fun getAllUniversityAndStudent(): LiveData<List<UniversityAndStudent>>

    // Many to Many
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCourseStudentCrossRef(courseStudentCrossRef: List<CourseStudentCrossRef>)

    @Transaction
    @Query("SELECT * from student")
    fun getAllStudentWithCourse(): LiveData<List<StudentWithCourse>>

    @Transaction
    @Query("SELECT * from student")
    fun getAllStudentWithCourseUniversity(): LiveData<List<StudentWithCourseUniversity>>
}