package com.svk.productbrowser.di

import com.squareup.moshi.Moshi
import com.svk.productbrowser.data.remote.ProductsApi
import com.svk.productbrowser.data.repository.ProductsRepository
import com.svk.productbrowser.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.Duration
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideProductsRepository(productsApi: ProductsApi): ProductsRepository {
        return ProductsRepository(productsApi)
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)

        return OkHttpClient.Builder()
            .readTimeout(Duration.ofSeconds(10))
            .connectTimeout(Duration.ofSeconds(15))
            .addInterceptor(logging)
            .build()
    }


    @Provides
    @Singleton
    fun provideProductsApi(httpClient: OkHttpClient):  ProductsApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(httpClient)
            .build()
            .create(ProductsApi::class.java)
    }
}