package com.android.marvel.domain.usecases

import com.android.marvel.common.extensions.fixture
import com.android.marvel.domain.base.FailureError
import com.android.marvel.domain.base.Resource
import com.android.marvel.domain.models.Character
import com.android.marvel.domain.repositories.CharacterRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetCharactersUseCaseTest {

    private lateinit var getCharactersUseCase: GetCharactersUseCase

    @RelaxedMockK
    private lateinit var characterRepository: CharacterRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        getCharactersUseCase = GetCharactersUseCase(characterRepository)
    }

    @Test
    fun `invoke should return characters when repository succeeds`() = runTest {
        val characterNamePrefix = "a"
        val expectedResponseSuccess = Resource.Success(listOf(fixture<Character>(), fixture<Character>()))

        coEvery { characterRepository.getCharacters(characterNamePrefix) } returns expectedResponseSuccess

        val result = getCharactersUseCase.invoke(characterNamePrefix)

        coVerify(exactly = 1) { characterRepository.getCharacters(characterNamePrefix) }
        Assert.assertEquals(expectedResponseSuccess, result)
    }

    @Test
    fun `invoke should return a failure error when repository fails`() = runTest {
        val characterNamePrefix = "a"
        val expectedResponseFailure = Resource.Failure(FailureError.Network)

        coEvery { characterRepository.getCharacters(characterNamePrefix) } returns expectedResponseFailure

        val result = getCharactersUseCase.invoke(characterNamePrefix)

        coVerify(exactly = 1) { characterRepository.getCharacters(characterNamePrefix) }
        Assert.assertEquals(expectedResponseFailure, result)
    }
}