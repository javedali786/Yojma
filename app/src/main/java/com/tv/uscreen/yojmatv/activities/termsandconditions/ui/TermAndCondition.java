package com.tv.uscreen.yojmatv.activities.termsandconditions.ui;

import static com.tv.uscreen.yojmatv.BuildConstants.TERM_AND_CONDITION;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;


import com.tv.uscreen.yojmatv.R;
import com.tv.uscreen.yojmatv.baseModels.BaseBindingActivity;
import com.tv.uscreen.yojmatv.databinding.ActivityTermsConditionBinding;
;

public class TermAndCondition extends BaseBindingActivity<ActivityTermsConditionBinding> {

    @Override
    public ActivityTermsConditionBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityTermsConditionBinding.inflate(inflater);

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getwebView();
    }

    private void getwebView() {
        getBinding().toolbar.searchIcon.setVisibility(View.GONE);
        getBinding().toolbar.homeIcon.setVisibility(View.GONE);
        getBinding().toolbar.titleText.setVisibility(View.VISIBLE);
        getBinding().toolbar.backLayout.setVisibility(View.VISIBLE);
        getBinding().toolbar.screenText.setText(TermAndCondition.this.getResources().getString(R.string.terms_and_conditions));
        getBinding().webView.getSettings().setJavaScriptEnabled(true);
        getBinding().webView.getSettings().setBuiltInZoomControls(true);
        getBinding().webView.getSettings().setUseWideViewPort(true);
        getBinding().webView.getSettings().setLoadWithOverviewMode(true);
        getBinding().webView.loadUrl(TERM_AND_CONDITION);
        getBinding().webView.setWebViewClient(new InsideWebViewClient());
        getBinding().toolbar.backLayout.setOnClickListener(view -> onBackPressed());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private class InsideWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}

