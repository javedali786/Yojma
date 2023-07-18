package com.tv.uscreen.yojmatv.utils.helpers;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.core.app.ActivityOptionsCompat;

import com.enveu.client.utils.ClickHandler;
import com.tv.uscreen.yojmatv.activities.search.ui.ActivitySearch;
import com.tv.uscreen.yojmatv.databinding.ActivityHelpBinding;
import com.tv.uscreen.yojmatv.databinding.ToolbarBinding;
;
;


public final class ToolBarHandler {
    private static ToolBarHandler instance;

    private ToolBarHandler() {
    }

    public static ToolBarHandler getInstance() {
        if (instance == null) {
            instance = new ToolBarHandler();
        }
        return instance;
    }

    public void setHomeAction(ToolbarBinding toolbar, Activity context) {
        toolbar.llSearchIcon.setOnClickListener(view -> {
            if (ClickHandler.allowClick()) {
                ActivityOptionsCompat activityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(context,
                                toolbar.searchIcon, "imageMain");
                Intent in = new Intent(context, ActivitySearch.class);
                context.startActivity(in, activityOptionsCompat.toBundle());
            }
        });
        toolbar.clNotification.setOnClickListener(view -> {
            if (ClickHandler.allowClick()) {
               // ActivityLauncher.getInstance().notificationActivity(context, NotificationActivity.class);
            }
        });
    }

    public void setHelpAction(ActivityHelpBinding binding, String title, Activity context) {
        binding.toolbarFaq.backLayout.setVisibility(View.VISIBLE);
        binding.toolbarFaq.titleMid.setVisibility(View.VISIBLE);
        binding.toolbarFaq.titleMid.setText(title);
    }

}