package com.example.bangkit_2024_bpaai.AdvancedDatabase.StudentUniversity

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.bangkit_2024_bpaai.AdvancedDatabase.StudentUniversity.database.Student
import com.example.bangkit_2024_bpaai.AdvancedDatabase.StudentUniversity.database.StudentAndUniversity
import com.example.bangkit_2024_bpaai.AdvancedDatabase.StudentUniversity.database.StudentDao
import com.example.bangkit_2024_bpaai.AdvancedDatabase.StudentUniversity.database.StudentWithCourse
import com.example.bangkit_2024_bpaai.AdvancedDatabase.StudentUniversity.database.StudentWithCourseUniversity
import com.example.bangkit_2024_bpaai.AdvancedDatabase.StudentUniversity.database.UniversityAndStudent
import com.example.bangkit_2024_bpaai.AdvancedDatabase.StudentUniversity.helper.SortType
import com.example.bangkit_2024_bpaai.AdvancedDatabase.StudentUniversity.helper.SortUtils

class StudentRepository(private val studentDao: StudentDao) {
    //    fun getAllStudent(): LiveData<List<Student>> = studentDao.getAllStudent()
//    fun getAllStudent(sortType: SortType): LiveData<List<Student>> {
//        val query = SortUtils.getSortedQuery(sortType)
//        return studentDao.getAllStudent(query)
//    }
    fun getAllStudent(sortType: SortType): LiveData<PagedList<Student>> {
        val query = SortUtils.getSortedQuery(sortType)
        val student = studentDao.getAllStudent(query)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true) // menentukan apakah menggunakan placeholder atau tidak
            .setInitialLoadSizeHint(30) // mengatur jumlah data yang diambil pertama kali. Default-nya adalah tiga kali pageSize
            .setPageSize(10) // mengatur jumlah data yang diambil per halamannya
            // prefetchDistance : Menentukan jarak sisa item untuk mengambil data kembali. Default-nya sama dengan pageSize
            // maxSize : Menentukan jumlah maksimum item yang dapat dimuat di PagedList. Default-nya adalah Int.MAX_VALUE
            // jumpThreshold : Menentukan batas jumlah item yang sedang dimuat. Default-nya adalah Int.MIN_VALUE
            .build()

        return LivePagedListBuilder(student, config).build() // LivePagedListBuilder digunakan untuk merubah DataSource menjadi PagedList dalam bentuk LiveData
    }

    fun getAllStudentAndUniversity(): LiveData<List<StudentAndUniversity>> =
        studentDao.getAllStudentAndUniversity()

    fun getAllUniversityAndStudent(): LiveData<List<UniversityAndStudent>> =
        studentDao.getAllUniversityAndStudent()

    fun getAllStudentWithCourse(): LiveData<List<StudentWithCourse>> =
        studentDao.getAllStudentWithCourse()

    fun getAllStudentWithCourseUniversity(): LiveData<List<StudentWithCourseUniversity>> =
        studentDao.getAllStudentWithCourseUniversity()

//    suspend fun insertAllData() {
//        studentDao.insertStudent(InitialDataSource.getStudents())
//        studentDao.insertUniversity(InitialDataSource.getUniversities())
//        studentDao.insertCourse(InitialDataSource.getCourses())
//
//        // untuk memasukkan dan mengambil data pada StudentRepository
//        studentDao.insertCourseStudentCrossRef(InitialDataSource.getCourseStudentRelation())
//    }
}