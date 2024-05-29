package com.example.bangkit_2024_bpaai.AdvancedDatabase.StudentUniversity.database

import androidx.room.*

@Entity
data class Student(
    @PrimaryKey
    val studentId: Int,
    val name: String,
    val univId: Int,

    @ColumnInfo(defaultValue = "false")
    val isGraduate: Boolean? = false
)

@Entity
data class University(
    @PrimaryKey
    val universityId: Int,
    val name: String,
)

@Entity
data class Course(
    @PrimaryKey
    val courseId: Int,
    val name: String,
)

// Many to One (student to university)
// untuk menambahkan relasi Many to One pada tabel Student dan University
data class StudentAndUniversity(
    // @Embedded untuk menambahkan data utama yang ingin ditampilkan
    @Embedded // digunakan pada kelas induk
    val student: Student,

    @Relation( // digunakan pada kelas yang dihubungkan
        parentColumn = "univId",
        entityColumn = "universityId"
    )
    val university: University? = null
)

// One to Many (university to student)
data class UniversityAndStudent(
    @Embedded
    val university: University,

    @Relation(
        parentColumn = "universityId", // Primary Key dari tabel University
        entityColumn = "univId" // Foreign Key dari tabel Student
    )
    val student: List<Student>
)

// Many to Many (student to course)
@Entity(primaryKeys = ["sId", "cId"])
data class CourseStudentCrossRef(
    val sId: Int, // sId merupakan id baru yang mereferensikan studentId
    @ColumnInfo(index = true)
    val cId: Int, // cId adalah id baru yang mereferensikan untuk courseId
)

data class StudentWithCourse(
    @Embedded
    val student: Student,

    @Relation(
        parentColumn = "studentId",
        entity = Course::class,
        entityColumn = "courseId",
        associateBy = Junction( // associateBy untuk menambahkan referensi hubungan antara kedua kelas
            value = CourseStudentCrossRef::class,
            parentColumn = "sId",
            entityColumn = "cId"
        )
    )
    val course: List<Course>
)

// jika Anda ingin menampilkan data universitas juga
data class StudentWithCourseUniversity(
    @Embedded
    val studentAndUniversity: StudentAndUniversity,

    @Relation(
        parentColumn = "studentId",
        entity = Course::class,
        entityColumn = "courseId",
        associateBy = Junction(
            value = CourseStudentCrossRef::class,
            parentColumn = "sId",
            entityColumn = "cId"
        )
    )
    val course: List<Course>
)