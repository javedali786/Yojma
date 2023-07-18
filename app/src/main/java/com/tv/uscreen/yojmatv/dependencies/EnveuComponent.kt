package com.tv.uscreen.yojmatv.dependencies

import com.tv.uscreen.yojmatv.OttApplication
import com.tv.uscreen.yojmatv.activities.homeactivity.ui.HomeActivity
import com.tv.uscreen.yojmatv.activities.splash.ui.ActivitySplash
import com.tv.uscreen.yojmatv.dependencies.modules.UserPreferencesModule
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [UserPreferencesModule::class])
interface EnveuComponent {
    fun inject (ottApplication: OttApplication)
    fun inject(splash: ActivitySplash)
    fun inject(homeActivity: HomeActivity)

}