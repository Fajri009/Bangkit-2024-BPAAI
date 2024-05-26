package com.example.bangkit_2024_bpaai.AdvancedTesting.utils

import com.example.bangkit_2024_bpaai.AdvancedTesting.util.DateFormatter
import org.junit.*
import java.time.format.DateTimeParseException
import java.time.zone.ZoneRulesException

class DateFormatterTest {
    @Test
    fun given_correct_ISO_8601_format_then_should_format_correctly() {
        val currentDate = "2022-02-02T10:10:10Z"
        Assert.assertEquals("02 Feb 2022 | 17:10",
            DateFormatter.formatDate(currentDate, "Asia/Jakarta")
        )
        Assert.assertEquals("02 Feb 2022 | 18:10",
            DateFormatter.formatDate(currentDate, "Asia/Makassar")
        )
        Assert.assertEquals("02 Feb 2022 | 19:10",
            DateFormatter.formatDate(currentDate, "Asia/Jayapura")
        )
    }

    @Test
    fun given_wrong_ISO_8601_format_then_should_throw_error() {
        val wrongFormat = "2022-02-02T10:10"
        Assert.assertThrows(DateTimeParseException::class.java) {
            DateFormatter.formatDate(wrongFormat, "Asia/Jakarta")
        }
    }

    @Test
    fun given_invalid_timezone_then_should_throw_error() {
        val wrongFormat = "2022-02-02T10:10:10Z"
        Assert.assertThrows(ZoneRulesException::class.java) {
            DateFormatter.formatDate(wrongFormat, "Asia/Bandung")
        }
    }
}