package com.breadgangtvnetwork.dependencies.modules

import android.content.Context
import com.breadgangtvnetwork.dependencies.providers.DTGPrefrencesProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UserPreferencesModule(context: Context) {
    val mContext = context
    @Singleton
    @Provides
     fun provideUserPrefrences(): DTGPrefrencesProvider {
        return DTGPrefrencesProvider(mContext)
    }
}