package com.test.meldcx.di

import android.app.Application
import com.test.meldcx.data.local.database.AppDatabase
import com.test.meldcx.data.local.database.dao.HistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun getAppDatabase(context: Application): AppDatabase{
        return AppDatabase.getDatabase(context)
    }

    @Singleton
    @Provides
    fun appDao(appDatabase: AppDatabase): HistoryDao{
        return appDatabase.historyDao()
    }
}