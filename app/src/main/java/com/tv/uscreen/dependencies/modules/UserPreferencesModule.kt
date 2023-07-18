package com.tv.uscreen.dependencies.modules

import android.content.Context
import com.tv.uscreen.dependencies.providers.DTGPrefrencesProvider
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