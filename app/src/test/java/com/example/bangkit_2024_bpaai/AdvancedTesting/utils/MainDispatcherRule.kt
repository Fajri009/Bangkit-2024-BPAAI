package com.example.bangkit_2024_bpaai.AdvancedTesting.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    /*
        UnconfinedTestDispatcher : Untuk test yang tidak membutuhkan pause/resume alias otomatis langsung eksekusi.
        StandardTestDispatcher : Untuk test yang membutuhkan pause/resume.
     */
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}