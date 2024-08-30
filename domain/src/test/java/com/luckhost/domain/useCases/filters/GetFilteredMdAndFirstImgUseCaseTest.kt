package com.luckhost.domain.useCases.filters

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GetFilteredMdAndFirstImgUseCaseTest {

    @Test
    fun `should return a list with a link from a simple string`() {
        val useCase = GetFilteredMdAndFirstImgUseCase()

        val inputString = "some wired text bla bla ![image](very://important/link.png)"

        val actual = useCase.execute(inputString).second.first()

        val expected = "very://important/link.png"

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should return a string without of links`() {
        val useCase = GetFilteredMdAndFirstImgUseCase()

        val inputString = "some wired text bla bla " +
                "![image](very://important/link.png)" +
                "![image](very://important/link.png)" +
                "some text again " +
                "![image](very://important/link.png)"

        val actual = useCase.execute(inputString).first

        val expected = "some wired text bla bla some text again "

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should return a list of links`() {
        val useCase = GetFilteredMdAndFirstImgUseCase()

        val inputString = "some wired text bla bla " +
                "![image](very://important/link.png)" +
                "![image](very://important/link.png)" +
                "some text again " +
                "![image](very://important/link.png)"

        val actual = useCase.execute(inputString).second

        val expected = listOf<String>(
            "very://important/link.png",
            "very://important/link.png",
            "very://important/link.png"
        )

        Assertions.assertEquals(expected, actual)
    }
}