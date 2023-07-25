package com.tv.uscreen.yojmatv.activities.search.ui

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.moe.pushlibrary.MoEHelper
import com.moengage.core.Properties
import com.tv.uscreen.yojmatv.R


import com.tv.uscreen.yojmatv.SDKConfig
import com.tv.uscreen.yojmatv.activities.detail.ui.DetailActivity
import com.tv.uscreen.yojmatv.activities.search.adapter.CategoriedSearchAdapter
import com.tv.uscreen.yojmatv.activities.search.adapter.CommonSearchAdapter
import com.tv.uscreen.yojmatv.activities.search.adapter.RecentListAdapter
import com.tv.uscreen.yojmatv.activities.search.adapter.RecentListAdapter.KeywordItemHolderListener
import com.tv.uscreen.yojmatv.activities.series.ui.SeriesDetailActivity
import com.tv.uscreen.yojmatv.adapters.CommonShimmerAdapter
import com.tv.uscreen.yojmatv.baseModels.BaseBindingActivity
import com.tv.uscreen.yojmatv.beanModel.KeywordList
import com.tv.uscreen.yojmatv.beanModel.enveuCommonRailData.RailCommonData
import com.tv.uscreen.yojmatv.beanModel.popularSearch.ItemsItem
import com.tv.uscreen.yojmatv.beanModel.search.SearchRequestModel
import com.tv.uscreen.yojmatv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import com.tv.uscreen.yojmatv.callbacks.commonCallbacks.SearchClickCallbacks
import com.tv.uscreen.yojmatv.databinding.ActivitySearchBinding
import com.tv.uscreen.yojmatv.fragments.dialog.CommonDialogFragment
import com.tv.uscreen.yojmatv.fragments.dialog.CommonDialogFragment.Companion.newInstance
import com.tv.uscreen.yojmatv.utils.BindingUtils.FontUtil
import com.tv.uscreen.yojmatv.utils.Logger
import com.tv.uscreen.yojmatv.utils.MediaTypeConstants
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.AppColors
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod
import com.tv.uscreen.yojmatv.utils.constants.AppConstants
import com.tv.uscreen.yojmatv.utils.cropImage.helpers.NetworkConnectivity
import com.tv.uscreen.yojmatv.utils.helpers.AppPreference
import com.tv.uscreen.yojmatv.utils.helpers.RailInjectionHelper
import com.tv.uscreen.yojmatv.utils.helpers.RecyclerAnimator
import com.tv.uscreen.yojmatv.utils.helpers.intentlaunchers.ActivityLauncher
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper
import java.util.Collections
import java.util.Objects

class ActivitySearch : BaseBindingActivity<ActivitySearchBinding?>(), SearchClickCallbacks, KeywordItemHolderListener, SearchView.OnQueryTextListener, CommonDialogFragment.EditDialogListener {
    private var searchAdapter: CategoriedSearchAdapter? = null
    private var model: MutableList<RailCommonData>? = null
    private var mLastClickTime: Long = 0
    private var isShimmer = false
    private var railInjectionHelper: RailInjectionHelper? = null
    private val FILTER_REQUEST_CODE = 2000
    private var searchText: String? = ""
    private var applyFilter = false
    private var requestModel: SearchRequestModel? = null
    private val stringsHelper by lazy { StringsHelper }
    private val colorsHelper by lazy { ColorsHelper }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseColor()
        val font = FontUtil.getNormal(this@ActivitySearch)
        val searchText = binding!!.toolbar.searchView.findViewById<TextView>(R.id.search_src_text)
        val imageView = binding!!.toolbar.searchView.findViewById<ImageView>(R.id.search_close_btn)
            try {
                searchText.setTextColor(AppColors.searchKeywordTextColor())
                searchText.setHintTextColor(AppColors.searchKeywordHintColor())
                searchText.isCursorVisible = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if (searchText.textCursorDrawable is InsetDrawable) {
                        val insetDrawable = searchText.textCursorDrawable as InsetDrawable?
                        insetDrawable!!.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                        searchText.textCursorDrawable = insetDrawable
                    }
                    if (searchText.textSelectHandle is BitmapDrawable) {
                        val insetDrawable = searchText.textSelectHandle as BitmapDrawable?
                        insetDrawable!!.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                        searchText.setTextSelectHandle(insetDrawable)
                    }
                    if (searchText.textSelectHandleRight is BitmapDrawable) {
                        val insetDrawable = searchText.textSelectHandleRight as BitmapDrawable?
                        insetDrawable!!.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                        searchText.setTextSelectHandleRight(insetDrawable)
                    }
                    if (searchText.textSelectHandleLeft is BitmapDrawable) {
                        val insetDrawable = searchText.textSelectHandleLeft as BitmapDrawable?
                        insetDrawable!!.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                        searchText.setTextSelectHandleLeft(insetDrawable)
                    }
                }
                imageView.setColorFilter(ContextCompat.getColor(this, R.color.series_detail_now_playing_title_color), PorterDuff.Mode.MULTIPLY)
            } catch (ex: Exception) {
                Logger.w(ex)
            }
//        }
        searchText.typeface = font
        clickListner()
        connectionObserver()
        binding!!.toolbar.searchView.setOnQueryTextListener(this)
    }

    private fun parseColor() {
        binding!!.stringData = stringsHelper
        binding!!.colorsData = colorsHelper
        binding?.connection?.colorsData = colorsHelper
        binding?.connection?.stringData = stringsHelper
        binding!!.toolbar.colorsData = colorsHelper
        binding!!.toolbar.stringData = stringsHelper
    }

    private var searchResult = true
    private fun clickListner() {
        binding!!.noResult.visibility = View.GONE
        hitApiPopularSearch()
        setRecyclerProperties(binding!!.recentSearchRecycler)
        setRecentSearchAdapter()
        binding!!.toolbar.backButton.setOnClickListener { onBackPressed() }
        binding!!.toolbar.clearText.setOnClickListener { onBackPressed() }
        /*getBinding().toolbar.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivitySearch.this, FilterIconActivity.class);
                startActivityForResult(intent, FILTER_REQUEST_CODE);
            }
        });*/
    }

    private fun setRecentSearchAdapter() {
        val gson = Gson()
        val json = AppPreference.getInstance(this).recentSearchList
        if (json.isEmpty()) {
            binding!!.llRecentSearchLayout.visibility = View.GONE
        } else {
            binding!!.llRecentSearchLayout.visibility = View.VISIBLE
            val type = object : TypeToken<List<KeywordList?>?>() {}.type
            val arrPackageData = gson.fromJson<List<KeywordList?>>(json, type)
            Collections.reverse(arrPackageData)
            if (arrPackageData.isNotEmpty()) {
                val recentListAdapter = RecentListAdapter(this@ActivitySearch, arrPackageData, this@ActivitySearch)
                binding!!.recentSearchRecycler.adapter = recentListAdapter
            }
        }
        binding!!.deleteKeywords.setOnClickListener {
            commonDialog(
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.recent_searches.toString(),
                    getString(R.string.popup_recent_search)
                ),
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_delete_search_history.toString(),
                    getString(R.string.popup_delete_search_history)
                ),
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_search_yes.toString(),
                    getString(R.string.popup_search_yes)
                )
            )
        }
    }

    private fun confirmDeletion() {
        val builder = AlertDialog.Builder(this@ActivitySearch, R.style.AlertDialogStyle)
        builder.setMessage(
            stringsHelper.stringParse(
                stringsHelper.instance()?.data?.config?.popup_delete_search_history.toString(),
                getString(R.string.popup_delete_search_history)
            )
        ).setCancelable(true).setPositiveButton(this.resources.getString(R.string.delete)) { dialog: DialogInterface, _: Int ->
            AppPreference.getInstance(this@ActivitySearch).recentSearchList = ""
            binding!!.llRecentSearchLayout.visibility = View.GONE
            dialog.cancel()
        }
            .setNegativeButton(this.resources.getString(R.string.cancel)) { dialog: DialogInterface, _: Int -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
        val bn = alert.getButton(DialogInterface.BUTTON_NEGATIVE)
        bn.setTextColor(ContextCompat.getColor(this@ActivitySearch, R.color.series_detail_now_playing_title_color))
        val bp = alert.getButton(DialogInterface.BUTTON_POSITIVE)
        bp.setTextColor(ContextCompat.getColor(this@ActivitySearch, R.color.series_detail_episode_unselected_btn_txt_color))
    }

    private fun hitApiSearchKeyword(searchKeyword: String, applyFilter: Boolean, requestModel: SearchRequestModel?) {
        model = ArrayList()
        callShimmer(binding!!.searchResultRecycler)
        setRecyclerProperties(binding!!.searchResultRecycler)
        binding!!.rootView.visibility = View.GONE
        binding!!.noResult.visibility = View.GONE
        binding!!.llSearchResultLayout.visibility = View.VISIBLE
        railInjectionHelper!!.getSearch(searchKeyword, 4, 0, applyFilter, requestModel).observe(this@ActivitySearch) { data: List<RailCommonData> ->
            searchResult = true
            if (data.isNotEmpty()) {
                try {
                    val properties = Properties()
                    properties.addAttribute(AppConstants.SEARCH_TEXT, searchKeyword)
                    MoEHelper.getInstance(applicationContext).trackEvent(AppConstants.TAB_SCREEN_VIEWED, properties)
                    binding!!.noResult.visibility = View.GONE
                    binding!!.rootView.visibility = View.GONE
                    binding!!.toolbar.filter.visibility = View.GONE
                    for (i in data.indices) {
                        val railCommonData = data[i]
                        if (railCommonData.pageTotal > 0) {
                            if (railCommonData.status) {
                                val temp = RailCommonData()
                                temp.enveuVideoItemBeans = railCommonData.enveuVideoItemBeans
                                if (railCommonData.enveuVideoItemBeans.size > 0) {
                                    val enveuVideoItemBean = railCommonData.enveuVideoItemBeans[0]
                                    val gson = Gson()
                                    val tmp = gson.toJson(enveuVideoItemBean)
                                    Log.d("Javed", "main: $tmp")
                                    temp.assetType = enveuVideoItemBean.assetType
                                    temp.status = true
                                    if (data[i].enveuVideoItemBeans[0].assetType.equals(AppConstants.VIDEO, ignoreCase = true)) {
                                        if (data[i].enveuVideoItemBeans[0].videoDetails.videoType.equals(MediaTypeConstants.getInstance().movie, ignoreCase = true)) {
                                            temp.layoutType = 0
                                        }
                                        if (data[i].enveuVideoItemBeans[0].videoDetails.videoType.equals(AppConstants.Movie, ignoreCase = true) ||
                                            data[i].enveuVideoItemBeans[0].videoDetails.videoType.equals(AppConstants.episode, ignoreCase = true)
                                        ) {
                                            temp.layoutType = 1
                                        }
                                    }
                                    temp.searchKey = searchKeyword
                                    temp.totalCount = railCommonData.pageTotal
                                    (model as ArrayList<RailCommonData>).add(temp)
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    Logger.w(e)
                }
                if ((model as ArrayList<RailCommonData>).size > 0) {
                    RecyclerAnimator(this).animate(binding!!.searchResultRecycler)
                    searchAdapter = CategoriedSearchAdapter(this@ActivitySearch, model, this@ActivitySearch)
                    binding!!.searchResultRecycler.adapter = searchAdapter
                } else {
                    binding!!.noResult.visibility = View.VISIBLE
                    val filterGenreSavedListKeyForApi = KsPreferenceKeys.getInstance().dataGenreListKeyValue
                    val filterSortSavedListKeyForApi = KsPreferenceKeys.getInstance().dataSortListKeyValue
                    if (filterGenreSavedListKeyForApi != null && filterGenreSavedListKeyForApi.size > 0 || filterSortSavedListKeyForApi != null && filterSortSavedListKeyForApi.size > 0) {
                        binding!!.toolbar.filter.visibility = View.VISIBLE
                    } else {
                        binding!!.toolbar.filter.visibility = View.GONE
                    }
                    binding!!.rootView.visibility = View.VISIBLE
                    binding!!.llSearchResultLayout.visibility = View.GONE
                }
            } else {
                binding!!.noResult.visibility = View.VISIBLE
                val filterGenreSavedListKeyForApi = KsPreferenceKeys.getInstance().dataGenreListKeyValue
                val filterSortSavedListKeyForApi = KsPreferenceKeys.getInstance().dataSortListKeyValue
                if (filterGenreSavedListKeyForApi != null && filterGenreSavedListKeyForApi.size > 0 || filterSortSavedListKeyForApi != null && filterSortSavedListKeyForApi.size > 0) {
                    binding!!.toolbar.filter.visibility = View.VISIBLE
                } else {
                    binding!!.toolbar.filter.visibility = View.GONE
                }
                binding!!.rootView.visibility = View.VISIBLE
                binding!!.llSearchResultLayout.visibility = View.GONE
            }
            createRecentSearch(searchKeyword)
            binding!!.progressBar.visibility = View.GONE
        }
    }

    private fun createRecentSearch(searchKeyword: String) {
        if (searchKeyword.equals("", ignoreCase = true)) {
            return
        }
        val keywordList = KeywordList()
        keywordList.keywords = searchKeyword
        keywordList.timeStamp = AppCommonMethod.currentTimeStamp
        if (AppPreference.getInstance(this).recentSearchList.equals("", ignoreCase = true)) {
            val list: MutableList<KeywordList> = ArrayList()
            list.add(keywordList)
            val gson = Gson()
            val json = gson.toJson(list)
            AppPreference.getInstance(this).recentSearchList = json
            setRecentSearchAdapter()
        } else {
            val gson = Gson()
            val json = AppPreference.getInstance(this).recentSearchList
            if (json.isEmpty()) {
            } else {
                val type = object : TypeToken<List<KeywordList?>?>() {}.type
                val arrPackageData = gson.fromJson<List<KeywordList?>>(json, type)
                if (json.contains(searchKeyword)) {
                    return
                }
                val newL: MutableList<KeywordList?> = ArrayList(arrPackageData)
                if (newL.size < 5) {
                    newL.add(keywordList)
                } else {
                    newL.removeAt(0)
                    newL.add(keywordList)
                }
                val jsons = gson.toJson(newL)
                AppPreference.getInstance(this).recentSearchList = jsons
                Collections.reverse(newL)
                if (newL.size > 0) {
                    val recentListAdapter = RecentListAdapter(this@ActivitySearch, newL, this@ActivitySearch)
                    binding!!.recentSearchRecycler.adapter = recentListAdapter
                }
            }
        }
    }

    private fun hitApiPopularSearch() {
        railInjectionHelper = ViewModelProvider(this)[RailInjectionHelper::class.java]
        if (!SDKConfig.getInstance().popularSearchId.equals("", ignoreCase = true)) {
            railInjectionHelper!!.getPlayListDetailsWithPagination(SDKConfig.getInstance().popularSearchId, 0, 5, null).observe(this) { playlistRailData: RailCommonData ->
                if (Objects.requireNonNull(playlistRailData) != null) {
                    setUiComponents(playlistRailData)
                }
            }
        }
    }

    private fun setUiComponents(jsonObject: RailCommonData) {
        isShimmer = false
        if (jsonObject.status) {
            binding!!.tvPopularSearch.visibility = View.VISIBLE
        } else {
            binding!!.popularSearchRecycler.visibility = View.GONE
        }
        binding!!.popularSearchRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding!!.popularSearchRecycler.adapter = CommonSearchAdapter(this@ActivitySearch, jsonObject, this)
    }

    private fun setRecyclerProperties(recyclerView: RecyclerView) {
        recyclerView.hasFixedSize()
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
    }

    private fun connectionObserver() {
        connectionValidation(NetworkConnectivity.isOnline(this))
    }

    private fun connectionValidation(aBoolean: Boolean) {
        if (aBoolean) {
            uiInitialisation()
        } else {
            noConnectionLayout()
        }
    }

    private fun uiInitialisation() {
        binding!!.searchLayout.visibility = View.VISIBLE
        binding!!.noConnectionLayout.visibility = View.GONE
        binding!!.tvPopularSearch.visibility = View.GONE
        binding!!.toolbar.searchView.onActionViewExpanded()
        binding!!.toolbar.searchView.requestFocus()
        callShimmer(binding!!.popularSearchRecycler)
        clickListner()
    }

    private fun callShimmer(recyclerView: RecyclerView) {
        isShimmer = true
        val adapterPurchase = CommonShimmerAdapter(true)
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapterPurchase
    }

    private fun noConnectionLayout() {
        binding!!.searchLayout.visibility = View.GONE
        binding!!.progressBar.visibility = View.GONE
        binding!!.noConnectionLayout.visibility = View.VISIBLE
        binding!!.connection.retryTxt.setOnClickListener { connectionObserver() }
    }

    override fun inflateBindingLayout(inflater: LayoutInflater): ActivitySearchBinding {
        return ActivitySearchBinding.inflate(inflater)
    }

    override fun onEnveuItemClicked(itemValue: EnveuVideoItemBean) {
        val isPremium: Boolean = itemValue.isPremium
        var isHosted = false
        var externalUrl: String? = ""
        var isParentContentNull = false
        var skuId: String? = ""
        if (itemValue.sku != null) {
            skuId = itemValue.sku
        }
        if (itemValue.parentContent == null) {
            isParentContentNull = true
        }
        var tittle: String? = ""
        if (itemValue.title != null) {
            tittle = itemValue.title
        }
        var trailerReferenceId: String? = ""
        if (itemValue.trailerReferenceId != null) {
            trailerReferenceId = itemValue.trailerReferenceId
        }
        var externalRefId: String? = ""
        if (itemValue.externalRefId != null) {
            externalRefId = itemValue.externalRefId
        }
        var customContentType: String? = ""
        val assetType = itemValue.assetType
        var videoType: String? = ""
        if (itemValue.assetType.equals(AppConstants.VIDEO, ignoreCase = true)) {
            videoType = itemValue.videoDetails.videoType
        } else if (itemValue.assetType.equals(AppConstants.LIVE, ignoreCase = true)) {
            if (java.lang.Boolean.TRUE == itemValue.liveContent.isHosted) {
                isHosted = true
            } else {
                if (itemValue.liveContent.externalUrl != null) {
                    externalUrl = itemValue.liveContent.externalUrl
                }
            }
        }
        AppCommonMethod.launchDetailScreenFromSearch(
            this,
            assetType,
            itemValue.id,
            customContentType!!,
            videoType,
            trailerReferenceId,
            externalRefId!!,
            isPremium,
            skuId,
            isParentContentNull,
            tittle,
            isHosted,
            externalUrl!!,
            itemValue.posterURL
        )
    }

    override fun onShowAllItemClicked(itemValue: RailCommonData, header: String) {
        var customContentType: String? = ""
        var videoType: String? = ""
        val assetType = itemValue.assetType

        if (assetType.equals(AppConstants.VIDEO, ignoreCase = true)) {
            videoType = itemValue.enveuVideoItemBeans[0].videoDetails.videoType
        }
        if (itemValue != null && itemValue.status) {
            applyFilter = java.lang.Boolean.parseBoolean(KsPreferenceKeys.getInstance().filterApply)
            ActivityLauncher.getInstance().resultActivityBundle(
                this@ActivitySearch, ActivityResults::class.java,
                assetType, itemValue.searchKey, itemValue.totalCount, applyFilter, customContentType, videoType, header
            )
        }
    }

    override fun onShowAllProgramClicked(itemValue: RailCommonData) {
        if (itemValue != null && itemValue.status) {
            applyFilter = java.lang.Boolean.parseBoolean(KsPreferenceKeys.getInstance().filterApply)
        }
    }

    override fun onPopularSearchItemClicked(itemValue: ItemsItem) {
        try {
            AppCommonMethod.trackFcmEvent(itemValue.name, itemValue.type, this@ActivitySearch, 0)
        } catch (e: Exception) {
        }
        if (itemValue.type.equals("SERIES", ignoreCase = true)) {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                return
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            if (NetworkConnectivity.isOnline(this@ActivitySearch)) {
                ActivityLauncher.getInstance().seriesDetailScreen(this@ActivitySearch, SeriesDetailActivity::class.java, itemValue.id)
            } else {
                commonDialog(
                    "",
                    stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.popup_no_internet_connection_found.toString(),
                        getString(R.string.popup_no_internet_connection_found)
                    ),
                    stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                        getString(R.string.popup_continue)
                    )
                )
            }
        } else if (itemValue.type.equals("SEASON", ignoreCase = true)) {
        } else {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                return
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            if (NetworkConnectivity.isOnline(this@ActivitySearch)) {
                ActivityLauncher.getInstance().detailScreen(this@ActivitySearch, DetailActivity::class.java, itemValue.id, "0", false)
            } else commonDialog(
                "",
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_no_internet_connection_found.toString(),
                    getString(R.string.popup_no_internet_connection_found)
                ), stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                    getString(R.string.popup_continue)
                )
            )
        }
    }

    override fun onItemClicked(itemValue: KeywordList) {
        if (NetworkConnectivity.isOnline(this@ActivitySearch)) {
            hideSoftKeyboard(binding!!.toolbar.searchText)
            try {
                AppCommonMethod.trackSearchFcmEvent(this@ActivitySearch, itemValue.keywords.trim { it <= ' ' })
            } catch (e: Exception) {
            }
            if (searchResult) {
                searchResult = false
                binding!!.toolbar.searchView.setQuery(itemValue.keywords.trim { it <= ' ' }, true)
            }
        } else {
            commonDialog(
                "",
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_no_internet_connection_found.toString(),
                    getString(R.string.popup_no_internet_connection_found)
                ),
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                    getString(R.string.popup_continue)
                )
            )
        }
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        if (NetworkConnectivity.isOnline(this@ActivitySearch)) {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return true
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            hideSoftKeyboard(binding!!.toolbar.searchText)
            try {
                AppCommonMethod.trackSearchFcmEvent(this@ActivitySearch, query.trim { it <= ' ' })
            } catch (e: Exception) {
            }
            if (query != null && query.trim { it <= ' ' }.length > 2) {
                applyFilter = java.lang.Boolean.parseBoolean(KsPreferenceKeys.getInstance().filterApply)
                searchText = query.trim { it <= ' ' }
                Logger.d("SEARCH TEXT $searchText")
                hitApiSearchKeyword(query.trim { it <= ' ' }, applyFilter, requestModel)
            }
        } else {
            commonDialog(
                "",
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_no_internet_connection_found.toString(),
                    getString(R.string.popup_no_internet_connection_found)
                ), stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                    getString(R.string.popup_continue)
                )
            )
        }
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILTER_REQUEST_CODE) {
            if (data != null && data.hasExtra("selected_filter")) {
                requestModel = data.getParcelableExtra("selected_filter")
            }
            if (resultCode == RESULT_OK) {
                Logger.d("RETURN WITH DATA")
                if (searchText != null && searchText!!.trim { it <= ' ' }.length > 2) {
                    applyFilter = java.lang.Boolean.parseBoolean(KsPreferenceKeys.getInstance().filterApply)
                    hitApiSearchKeyword(searchText!!.trim { it <= ' ' }, applyFilter, requestModel)
                }
            } else {
                applyFilter = java.lang.Boolean.parseBoolean(KsPreferenceKeys.getInstance().filterApply)
                hitApiSearchKeyword(searchText!!.trim { it <= ' ' }, applyFilter, requestModel)
            }
        }
    }

    private fun commonDialog(title: String, description: String, actionBtn: String) {
        val fm = supportFragmentManager
        val commonDialogFragment = newInstance(title, description, actionBtn)
        commonDialogFragment.setEditDialogCallBack(this)
        commonDialogFragment.show(fm, AppConstants.MESSAGE)
    }

    override fun onActionBtnClicked() {
        AppPreference.getInstance(this@ActivitySearch).recentSearchList = ""
        binding!!.llRecentSearchLayout.visibility = View.GONE
    }
}