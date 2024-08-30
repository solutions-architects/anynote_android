package com.luckhost.data.localStorage.keys.tokens

import com.luckhost.domain.models.network.AuthToken
import org.junit.jupiter.api.Assertions.*

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class SharedPrefTokensStorageTest {
    @Test
    fun `should save simple tokens`() {
        val context = RuntimeEnvironment.getApplication().applicationContext
        val storage = SharedPrefTokensStorage(context)

        val expected = AuthToken(
            accessToken = "123",
            refreshToken = "456"
        )

        storage.saveTokens(expected)

        val actual = storage.getTokensOrThrow()

        assertEquals(actual, expected)
    }

    @Test(expected = NoSuchElementException::class)
    fun `should return exception because of empty tokens`() {
        val context = RuntimeEnvironment.getApplication().applicationContext
        val storage = SharedPrefTokensStorage(context)

        storage.getTokensOrThrow()
    }
}