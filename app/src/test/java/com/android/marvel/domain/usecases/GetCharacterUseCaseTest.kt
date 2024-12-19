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
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetCharacterUseCaseTest {

    private lateinit var sut: GetCharacterUseCase
    private lateinit var characterRepository: CharacterRepository

    @Before
    fun setUp() {
        characterRepository = mock()
        sut = GetCharacterUseCase(characterRepository)
    }

    @Test
    fun `invoke should return character when repository succeeds`() = runTest {
        val expectedCharacter = fixture<Character>()
        val characterId = 1
        whenever(characterRepository.getCharacter(characterId)).thenReturn(Resource.Success(expectedCharacter))

        val result = sut.invoke(characterId)

        verify(characterRepository, times(1)).getCharacter(characterId)
        Assert.assertEquals(result, Resource.Success(expectedCharacter))
    }

    @Test
    fun `invoke should return a failure error when repository fails`() = runTest {
        val expectedFailure = FailureError.Network
        val characterId = 1

        whenever(characterRepository.getCharacter(characterId)).thenReturn(Resource.Failure(expectedFailure))

        val result = sut.invoke(characterId)

        verify(characterRepository, times(1)).getCharacter(characterId)
        Assert.assertEquals(result, Resource.Failure(expectedFailure))
    }
}