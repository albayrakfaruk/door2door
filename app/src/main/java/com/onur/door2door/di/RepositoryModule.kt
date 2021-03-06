package com.onur.door2door.di

import com.onur.door2door.data.remote.repository.Door2DoorRepository
import com.onur.door2door.data.remote.repository.Door2DoorService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(door2DoorService: Door2DoorService) = Door2DoorRepository(door2DoorService)
}