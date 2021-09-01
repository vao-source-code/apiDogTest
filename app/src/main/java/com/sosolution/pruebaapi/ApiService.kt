package com.sosolution.pruebaapi

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {

    //dame los perros por raza
    @GET
    suspend fun getDogsByBreeds(@Url url:String ):Response<DogsResponse>

}