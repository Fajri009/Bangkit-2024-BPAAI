package com.example.bangkit_2024_bpaai.AdvancedDatabase.StudentUniversity

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.example.bangkit_2024_bpaai.AdvancedDatabase.StudentUniversity.database.Student
import com.example.bangkit_2024_bpaai.AdvancedDatabase.StudentUniversity.database.StudentAndUniversity
import com.example.bangkit_2024_bpaai.AdvancedDatabase.StudentUniversity.database.StudentWithCourse
import com.example.bangkit_2024_bpaai.AdvancedDatabase.StudentUniversity.database.StudentWithCourseUniversity
import com.example.bangkit_2024_bpaai.AdvancedDatabase.StudentUniversity.database.UniversityAndStudent
import com.example.bangkit_2024_bpaai.AdvancedDatabase.StudentUniversity.helper.SortType

class MainAdvancedDatabaseViewModel(private val studentRepository: StudentRepository) : ViewModel() {
//    init {
//        insertAllData()
//    }

    private val _sort = MutableLiveData<SortType>()

    init {
        _sort.value = SortType.ASCENDING
    }

    fun changeSortType(sortType: SortType) {
        _sort.value = sortType
    }

    //    fun getAllStudent(): LiveData<List<Student>> = studentRepository.getAllStudent()
//    fun getAllStudent(): LiveData<List<Student>> = _sort.switchMap {
//        studentRepository.getAllStudent(it)
//    }
    fun getAllStudent(): LiveData<PagedList<Student>> = _sort.switchMap {
        studentRepository.getAllStudent(it)
    }

    fun getAllStudentAndUniversity(): LiveData<List<StudentAndUniversity>> =
        studentRepository.getAllStudentAndUniversity()

    fun getAllUniversityAndStudent(): LiveData<List<UniversityAndStudent>> =
        studentRepository.getAllUniversityAndStudent()

    fun getAllStudentWithCourse(): LiveData<List<StudentWithCourse>> =
        studentRepository.getAllStudentWithCourse()

    fun getAllStudentWithCourseUniversity(): LiveData<List<StudentWithCourseUniversity>> =
        studentRepository.getAllStudentWithCourseUniversity()

//    private fun insertAllData() = viewModelScope.launch {
//        studentRepository.insertAllData()
//    }
}

class ViewModelFactory(private val repository: StudentRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainAdvancedDatabaseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainAdvancedDatabaseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}