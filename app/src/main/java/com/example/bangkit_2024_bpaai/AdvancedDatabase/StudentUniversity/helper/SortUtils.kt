package com.example.bangkit_2024_bpaai.AdvancedDatabase.StudentUniversity.helper

import androidx.sqlite.db.SimpleSQLiteQuery

object SortUtils {
    fun getSortedQuery(sortType: SortType): SimpleSQLiteQuery {
        val simpleQuery = StringBuilder().append("SELECT * FROM student ")
        when (sortType) {
            SortType.ASCENDING -> {
                simpleQuery.append("ORDER BY name ASC")
            }
            SortType.DESCENDING -> {
                simpleQuery.append("ORDER BY name DESC")
            }
            SortType.RANDOM -> {
                simpleQuery.append("ORDER BY RANDOM()")
            }
        }
        return SimpleSQLiteQuery(simpleQuery.toString())
    }
}