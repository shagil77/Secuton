package com.shagil.secuton.di

import com.shagil.secuton.data.repository.LeisureTaskRepoImpl
import com.shagil.secuton.data.repository.RoutineTaskRepoImpl
import com.shagil.secuton.data.repository.TODOTaskRepoImpl
import com.shagil.secuton.domain.repository.LeisureTasksRepository
import com.shagil.secuton.domain.repository.RoutineTaskRepository
import com.shagil.secuton.domain.repository.TODOTaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindRoutineTaskRepository(
        routineTaskRepoImpl: RoutineTaskRepoImpl
    ):RoutineTaskRepository

    @Binds
    @Singleton
    abstract fun bindTODOTaskRepository(
        todoTaskRepoImpl: TODOTaskRepoImpl
    ):TODOTaskRepository

    @Binds
    @Singleton
    abstract fun bindLeisureTaskRepository(
        leisureTaskRepoImpl: LeisureTaskRepoImpl
    ):LeisureTasksRepository
}