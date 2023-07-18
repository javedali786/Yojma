package com.tv.uscreen.dependencies

import com.tv.uscreen.OttApplication
import com.tv.uscreen.activities.homeactivity.ui.HomeActivity
import com.tv.uscreen.activities.splash.ui.ActivitySplash
import com.tv.uscreen.dependencies.modules.UserPreferencesModule
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [UserPreferencesModule::class])
interface EnveuComponent {
    fun inject (ottApplication: OttApplication)
    fun inject(splash: ActivitySplash)
    fun inject(homeActivity: HomeActivity)

}