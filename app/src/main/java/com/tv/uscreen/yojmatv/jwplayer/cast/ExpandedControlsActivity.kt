package com.tv.uscreen.yojmatv.jwplayer.cast

import android.os.Bundle
import android.os.PersistableBundle
import android.view.ContextThemeWrapper
import android.view.Menu
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.mediarouter.app.MediaRouteButton
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.media.uicontroller.UIMediaController
import com.google.android.gms.cast.framework.media.widget.ExpandedControllerActivity
import com.tv.uscreen.yojmatv.R


class ExpandedControlsActivity : ExpandedControllerActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.cast_expanded_controller_activity)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.expanded_controller, menu)
        val tintColor = ContextCompat.getColor(this, R.color.moe_white)
        val item = menu.findItem(R.id.media_route_menu_item)
        val button = item.actionView as MediaRouteButton
        val castContext = ContextThemeWrapper(this, androidx.mediarouter.R.style.Theme_MediaRouter)
        val attrs = castContext.obtainStyledAttributes(
            null,
            androidx.mediarouter.R.styleable.MediaRouteButton,
            androidx.mediarouter.R.attr.mediaRouteButtonStyle,
            0
        )
        val drawable =
            attrs.getDrawable(androidx.mediarouter.R.styleable.MediaRouteButton_externalRouteEnabledDrawable)
        attrs.recycle()
        DrawableCompat.setTint(drawable!!, tintColor)
        drawable.state = button?.drawableState!!
        button.setRemoteIndicatorDrawable(drawable)
        CastButtonFactory.setUpMediaRouteButton(this, menu, R.id.media_route_menu_item)
        return true
    }

}