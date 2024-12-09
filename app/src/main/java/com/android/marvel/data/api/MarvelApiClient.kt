package com.android.marvel.data.api

import com.android.marvel.data.models.MarvelCharactersResponseDTO
import retrofit2.http.*

interface MarvelApiClient {

    @GET("v1/public/characters")
    suspend fun getCharacters(@Query("nameStartsWith") nameStartsWith: String?,
                              @Query("limit") limit: Int
    ): MarvelCharactersResponseDTO

    @GET("v1/public/characters/{characterId}")
    suspend fun getCharacter(@Path("characterId") characterId: Int
    ): MarvelCharactersResponseDTO
}