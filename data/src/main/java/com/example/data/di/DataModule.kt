package com.example.data.di

import com.example.data.api.NomicsApi
import com.example.data.repository.NomicRepository
import com.example.data.util.NomicsInterceptor
import okhttp3.OkHttpClient
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class DataModule {

    //Keeping it in companion object so we can access it like DataModule.module

    companion object{

        private const val nomics_base_url = "https://api.nomics.com/v1/"
        private val nomics_retrofit_base = StringQualifier("nomics_retrofit")

        val module: Module = module {
            //Create a singleton of the interceptor we need to add the api key on all calls to nomics
            single { NomicsInterceptor() }
            //Create the okhttpclient instance
            single { OkHttpClient.Builder().addInterceptor(get()).build() }
            //Create retrofit for nomics with key so we can create more base urls later if need be
            single(nomics_retrofit_base){
                Retrofit.Builder()
                    .addConverterFactory(MoshiConverterFactory.create())
                    .client(get())
                    .baseUrl(nomics_base_url)
                    .build()
            }
            single {
                val retrofit by inject<Retrofit>(nomics_retrofit_base)
                retrofit.create(NomicsApi::class.java)
            }
            single { NomicRepository(get()) }
        }

    }

}