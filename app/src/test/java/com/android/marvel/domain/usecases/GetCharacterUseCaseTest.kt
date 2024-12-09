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

class GetCharacterUseCaseTest {

    private lateinit var getCharacterUseCase: GetCharacterUseCase

    @RelaxedMockK
    private lateinit var characterRepository: CharacterRepository


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getCharacterUseCase = GetCharacterUseCase(characterRepository)
    }

    @Test
    fun `invoke should return character when repository succeeds`() = runTest {
        val expectedCharacter = fixture<Character>()
        val characterId = 1
        coEvery { characterRepository.getCharacter(characterId) } returns Resource.Success(expectedCharacter)

        val result = getCharacterUseCase.invoke(characterId)

        coVerify(exactly = 1) { characterRepository.getCharacter(characterId) }
        Assert.assertEquals(result, Resource.Success(expectedCharacter))
    }

    @Test
    fun `invoke should return a failure error when repository fails`() = runTest {
        val expectedFailure = FailureError.Network
        val characterId = 1

        coEvery { characterRepository.getCharacter(characterId) } returns Resource.Failure(expectedFailure)

        val result = getCharacterUseCase.invoke(characterId)

        coVerify(exactly = 1) { characterRepository.getCharacter(characterId) }
        Assert.assertEquals(result, Resource.Failure(expectedFailure))
    }
}