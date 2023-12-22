package kso.android.todoapp.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kso.android.todoapp.api.RestDataSource
import kso.android.todoapp.db.TodoListDatabase
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Singleton
    @Provides
    @Named("baseUrl")
    fun baseUrl() = "https://dummyjson.com"

    @Singleton
    @Provides
    fun provideRetrofit(@Named("baseUrl") baseUrl: String, okHttpClient: OkHttpClient): Retrofit{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .build()

    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient{
        return OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRestDataSource(retrofit: Retrofit): RestDataSource {
        return retrofit.create(RestDataSource::class.java)
    }

    @Singleton
    @Provides
    fun provideDatabaseDataSource(app: Application) =
        Room.databaseBuilder(app, TodoListDatabase::class.java, "Todo_DB")
            //.addMigrations()
            /*If you don’t want to provide migrations and you specifically want your database to be cleared when you upgrade the version, call fallbackToDestructiveMigration in the database builder
                    .build()
            */
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideTodoDao(database: TodoListDatabase) =    database.todoDao()

}