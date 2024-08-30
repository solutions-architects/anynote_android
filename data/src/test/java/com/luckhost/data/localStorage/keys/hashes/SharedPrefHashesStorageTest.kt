package com.luckhost.data.localStorage.keys.hashes

import org.junit.jupiter.api.Assertions.*

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class SharedPrefHashesStorageTest {

    @Test
    fun `should save simple list of hashes`() {
        val context = RuntimeEnvironment.getApplication().applicationContext
        val storage = SharedPrefHashesStorage(context)

        val expectedValues = listOf(
            "123",
            "12345",
            "123456"
        )

        storage.saveHashes(expectedValues)

        val actualValues = storage.getHashes()

        assertEquals(expectedValues, actualValues)
    }

    @Test
    fun `should return empty list`() {
        val context = RuntimeEnvironment.getApplication().applicationContext
        val storage = SharedPrefHashesStorage(context)

        val excepted = listOf<String>()
        val actual = storage.getHashes()

        assertEquals(excepted, actual)
    }

    @Test
    fun `should delete hash`() {
        val context = RuntimeEnvironment.getApplication().applicationContext
        val storage = SharedPrefHashesStorage(context)

        storage.saveHashes(listOf("123", "456"))

        storage.deleteHash("123")

        val expected = listOf("456")
        val actual = storage.getHashes()

        assertEquals(expected, actual)
    }

    @Test
    fun `should add new hash`() {
        val context = RuntimeEnvironment.getApplication().applicationContext
        val storage = SharedPrefHashesStorage(context)

        storage.saveHashes(listOf("123"))

        storage.addHash("456")

        val expected = listOf("123", "456")
        val actual = storage.getHashes()

        assertEquals(expected, actual)
    }
}