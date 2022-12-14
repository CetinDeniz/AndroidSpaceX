package com.axuca.spacexfan.di

import android.content.Context
import com.axuca.spacexfan.retrofit.SpaceXApi
import com.axuca.spacexfan.retrofit.SpaceXApiService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object FirebaseAuthenticationModule {

    @Singleton
    @Provides
    fun provideFirebaseAuthenticationModule(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
}

@InstallIn(SingletonComponent::class)
@Module
object GoogleSignInModule {

    @Singleton
    @Provides
    fun provideGoogleSignInAccountModule(@ApplicationContext appContext: Context): GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(appContext)
    }
}
@InstallIn(SingletonComponent::class)
@Module
object SpaceXApiServiceModule {

    @Singleton
    @Provides
    fun provideSpaceXApiService(): SpaceXApiService {
        return SpaceXApi.retrofitService
    }
}

@InstallIn(SingletonComponent::class)
@Module
object FirebaseRealtimeDatabaseModule {

    @Singleton
    @Provides
    fun provideFirebaseRealtimeDatabase(): DatabaseReference {
        return FirebaseDatabase
            .getInstance("https://spacex-fan-c5350-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("users")
    }
}
