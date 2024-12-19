package com.android.marvel.domain.usecases

import com.android.marvel.common.extensions.fixture
import com.android.marvel.domain.base.FailureError
import com.android.marvel.domain.base.Resource
import com.android.marvel.domain.models.Character
import com.android.marvel.domain.repositories.CharacterRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever

class GetCharactersUseCaseTest {

    private lateinit var sut: GetCharactersUseCase
    private lateinit var characterRepository: CharacterRepository

    @Before
    fun setup() {
        characterRepository = mock()
        sut = GetCharactersUseCase(characterRepository)
    }

    @Test
    fun `invoke should return characters when repository succeeds`() = runTest {
        val characterNamePrefix = "a"
        val expectedResponseSuccess = Resource.Success(listOf(fixture<Character>(), fixture<Character>()))

        whenever(characterRepository.getCharacters(characterNamePrefix)).thenReturn(expectedResponseSuccess)

        val result = sut.invoke(characterNamePrefix)

        verify(characterRepository, times(1)).getCharacters(characterNamePrefix)
        Assert.assertEquals(expectedResponseSuccess, result)
    }

    @Test
    fun `invoke should return a failure error when repository fails`() = runTest {
        val characterNamePrefix = "a"
        val expectedResponseFailure = Resource.Failure(FailureError.Network)

        whenever(characterRepository.getCharacters(characterNamePrefix)).thenReturn(expectedResponseFailure)

        val result = sut.invoke(characterNamePrefix)

        verify(characterRepository, times(1)).getCharacters(characterNamePrefix)
        Assert.assertEquals(expectedResponseFailure, result)
    }
}