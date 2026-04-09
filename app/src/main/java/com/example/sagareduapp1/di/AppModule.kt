package com.example.sagareduapp1.di

import androidx.room.Room
import com.example.sagareduapp1.database.AppDatabase
import com.example.sagareduapp1.database.AppRepository
import com.example.sagareduapp1.viewmodel.AppViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "edu_app_database"
        ).fallbackToDestructiveMigration().build()
    }

    single { get<AppDatabase>().appDao() }

    single { AppRepository(get()) }

    viewModel { AppViewModel(get()) }
}
