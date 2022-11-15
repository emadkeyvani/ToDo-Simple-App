package com.keyvani.todoroomdatabase.di

import android.content.Context
import androidx.room.Room
import com.keyvani.todoroomdatabase.db.TodoDatabase
import com.keyvani.todoroomdatabase.db.TodoEntity
import com.keyvani.todoroomdatabase.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =Room.databaseBuilder(
        context,TodoDatabase::class.java,Constants.TODO_DATABASE)
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideDao(db :TodoDatabase)=db.dao()

    @Provides
    @Singleton
    fun provideEntity() = TodoEntity(0,"",false)


}