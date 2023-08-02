package com.svk.productbrowser.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.svk.productbrowser.data.local.AppDatabase
import com.svk.productbrowser.data.local.ProductsDao
import com.svk.productbrowser.data.remote.ProductsApi
import com.svk.productbrowser.data.repository.ProductsRepository
import com.svk.productbrowser.ui.productList.ProductsAdapter
import com.svk.productbrowser.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.Duration
import javax.inject.Singleton

/**
 * Application DI provider
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "products_db.sqlite"
        ).build()
    }

    @Provides
    @Singleton
    fun provideProductsDao(appDatabase: AppDatabase): ProductsDao {
        return appDatabase.productsDao()
    }

    @Provides
    @Singleton
    fun provideProductsRepository(productsApi: ProductsApi, dao: ProductsDao,
                                  resourcesProvider: ResourcesProvider): ProductsRepository {
        return ProductsRepository(productsApi, dao, resourcesProvider)
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
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
    fun provideProductsApi(httpClient: OkHttpClient, moshi:Moshi):  ProductsApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(httpClient)
            .build()
            .create(ProductsApi::class.java)
    }

    @Provides
    fun provideProductList(): ProductsAdapter {
        return ProductsAdapter(arrayListOf())
    }
}