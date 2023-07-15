package com.breadgangtvnetwork.dependencies

import com.breadgangtvnetwork.OttApplication
import com.breadgangtvnetwork.activities.homeactivity.ui.HomeActivity
import com.breadgangtvnetwork.activities.splash.ui.ActivitySplash
import com.breadgangtvnetwork.dependencies.modules.UserPreferencesModule
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [UserPreferencesModule::class])
interface EnveuComponent {
    fun inject (ottApplication: OttApplication)
    fun inject(splash: ActivitySplash)
    fun inject(homeActivity: HomeActivity)

}