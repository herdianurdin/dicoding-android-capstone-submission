package com.capstone.gometry.di

import com.capstone.gometry.data.ExamRepository
import com.capstone.gometry.data.GeometryRepository
import com.capstone.gometry.data.QuestionRepository
import com.capstone.gometry.data.UserRepository
import com.capstone.gometry.data.repository.FirebaseExamRepository
import com.capstone.gometry.data.repository.FirebaseGeometryRepository
import com.capstone.gometry.data.repository.FirebaseQuestionRepository
import com.capstone.gometry.data.repository.FirebaseUserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    @Provides
    @Singleton
    fun provideUserRepository(): FirebaseUserRepository = UserRepository(auth, database)

    @Provides
    @Singleton
    fun provideGeometryRepository(): FirebaseGeometryRepository = GeometryRepository(auth, database)

    @Provides
    @Singleton
    fun provideExamRepository(): FirebaseExamRepository = ExamRepository(auth, database)

    @Provides
    @Singleton
    fun provideQuestionRepository(): FirebaseQuestionRepository = QuestionRepository(database)
}