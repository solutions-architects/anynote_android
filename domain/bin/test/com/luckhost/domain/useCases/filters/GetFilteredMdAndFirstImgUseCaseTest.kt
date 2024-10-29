package com.luckhost.domain.useCases.filters

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GetFilteredMdAndFirstImgUseCaseTest {

    @Test
    fun `should return a text without of link`() {
        val useCase = GetFilteredMdUseCase()

        val inputString = "some wired text bla bla ![image](very://important/link.png)"

        val actual = useCase.execute(inputString)

        val expected = "some wired text bla bla "

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should return a string without of links`() {
        val useCase = GetFilteredMdUseCase()

        val inputString = "some wired text bla bla " +
                "![image](very://important/link.png)" +
                "![image](very://important/link.png)" +
                "some text again " +
                "![image](very://important/link.png)"

        val actual = useCase.execute(inputString)

        val expected = "some wired text bla bla some text again "

        Assertions.assertEquals(expected, actual)
    }
}