package com.tv.dependencies

import com.tv.OttApplication
import com.tv.activities.homeactivity.ui.HomeActivity
import com.tv.activities.splash.ui.ActivitySplash
import com.tv.dependencies.modules.UserPreferencesModule
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [UserPreferencesModule::class])
interface EnveuComponent {
    fun inject (ottApplication: OttApplication)
    fun inject(splash: ActivitySplash)
    fun inject(homeActivity: HomeActivity)

}