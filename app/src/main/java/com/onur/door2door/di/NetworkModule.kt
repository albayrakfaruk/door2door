package com.onur.door2door.di

import android.content.Context
import com.onur.door2door.BuildConfig.BASE_URL
import com.onur.door2door.data.remote.repository.Door2DoorService
import com.onur.door2door.data.remote.repository.HerokuWebSocketListener
import com.readystatesoftware.chuck.BuildConfig
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 *   Created by farukalbayrak on 05.05.2021.
 */

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideRequest(): Request {
        return Request.Builder().url(BASE_URL).build()
    }

    @Provides
    fun provideDoor2DoorService(webSocket: WebSocket,herokuWebSocketListener: HerokuWebSocketListener): Door2DoorService {
        return Door2DoorService(webSocket,herokuWebSocketListener)
    }

    @Provides
    fun provideWebSocket(
        okHttpClient: OkHttpClient,
        request: Request,
        herokuWebSocketListener: HerokuWebSocketListener
    ): WebSocket {
        return okHttpClient.newWebSocket(request, herokuWebSocketListener)
    }

    @Provides
    fun provideHerokuWebSocketListener() = HerokuWebSocketListener()

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    fun provideChuckInterceptor(@ApplicationContext context: Context): ChuckInterceptor {
        return ChuckInterceptor(context)
    }

    @Provides
    fun provideOkHttpClient(
        chuckInterceptor: ChuckInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(chuckInterceptor)
            builder.addInterceptor(httpLoggingInterceptor)
        }
        return builder.build()
    }
}