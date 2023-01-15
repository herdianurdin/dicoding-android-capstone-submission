package com.capstone.gometry.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object FirebaseModule {
    @Provides
    @Singleton
    fun provideFirebaseAuthInstance(): FirebaseAuth =
        FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseDatabaseInstance(): FirebaseDatabase =
        FirebaseDatabase.getInstance()
}