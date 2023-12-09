package kso.android.todoapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
private object AppModule {

    @Singleton
    @Provides
    fun context(@ApplicationContext appContext: Context): Context = appContext.applicationContext

}
