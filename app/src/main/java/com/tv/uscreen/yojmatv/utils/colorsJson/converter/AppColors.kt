package com.tv.uscreen.yojmatv.utils.colorsJson.converter

import com.tv.uscreen.yojmatv.R


object AppColors {

    fun appBgColor(): Int = ColorsHelper.colorParser(ColorsHelper.instance()?.data?.config?.app_bg_color.toString(), R.color.app_bg_color)
    fun bottomNavUnselectedTextColor(): Int = ColorsHelper.colorParser(ColorsHelper.instance()?.data?.config?.bottom_nav_unselected_text_color.toString(), R.color.bottom_nav_unselected_text_color)
    fun bottomNavSelectedTextColor(): Int = ColorsHelper.colorParser(ColorsHelper.instance()?.data?.config?.bottom_nav_selected_text_color.toString(), R.color.bottom_nav_selected_text_color)
    fun bottomNavUnselectedIconColor(): Int = ColorsHelper.colorParser(ColorsHelper.instance()?.data?.config?.bottom_nav_unselected_icon_color.toString(), R.color.bottom_nav_unselected_icon_color)
    fun bottomNavSelectedIconColor(): Int = ColorsHelper.colorParser(ColorsHelper.instance()?.data?.config?.bottom_nav_selected_icon_color.toString(), R.color.bottom_nav_selected_icon_color)
    fun tphBgColor(): Int = ColorsHelper.colorParser(ColorsHelper.instance()?.data?.config?.tph_bg_color.toString(), R.color.tph_bg_color)
    fun tphBrColor(): Int = ColorsHelper.colorParser(ColorsHelper.instance()?.data?.config?.tph_border_color.toString(), R.color.tph_border_color)
    fun pwdSelectedEyeColor(): Int = ColorsHelper.colorParser(ColorsHelper.instance()?.data?.config?.pwd_selected_eye_color.toString(), R.color.pwd_selected_eye_color)
    fun pwdUnselectedEyeColor(): Int = ColorsHelper.colorParser(ColorsHelper.instance()?.data?.config?.pwd_unselected_eye_color.toString(), R.color.pwd_unselected_eye_color)
    fun radioBtnCheckedColor(): Int = ColorsHelper.colorParser(ColorsHelper.instance()?.data?.config?.signup_radio_btn_checked_color.toString(), R.color.signup_radio_btn_checked_color)
    fun radioBtnUncheckedColor(): Int = ColorsHelper.colorParser(ColorsHelper.instance()?.data?.config?.signup_radio_btn_unchecked_color.toString(), R.color.signup_radio_btn_unchecked_color)
    fun termsAndConditionTextColor(): Int =
        ColorsHelper.colorParser(ColorsHelper.instance()?.data?.config?.signup_terms_and_condition_text_color.toString(), R.color.signup_terms_and_condition_text_color)
    fun profileImageBorderColor(): Int = ColorsHelper.colorParser(ColorsHelper.instance()?.data?.config?.profile_Image_borderColor.toString(), R.color.profile_Image_borderColor)
    fun dobIconColor(): Int = ColorsHelper.colorParser(ColorsHelper.instance()?.data?.config?.profile_dob_icon_color.toString(), R.color.profile_dob_icon_color)
    fun dropDownIconColor(): Int = ColorsHelper.colorParser(ColorsHelper.instance()?.data?.config?.profile_drop_down_color.toString(), R.color.profile_drop_down_color)
    fun itemLabelIconColor(): Int = ColorsHelper.colorParser(ColorsHelper.instance()?.data?.config?.item_label_icon_color.toString(), R.color.item_label_icon_color)
    fun detailPageTabItemUnselectedTxtColor(): Int =
        ColorsHelper.colorParser(ColorsHelper.instance()?.data?.config?.series_detail_tab_item_unselected_txt_color.toString(), R.color.series_detail_tab_item_unselected_txt_color)
    fun detailPageTabItemSelectedTxtColor(): Int =
        ColorsHelper.colorParser(ColorsHelper.instance()?.data?.config?.series_detail_tab_item_selected_txt_color.toString(), R.color.series_detail_tab_item_selected_txt_color)
    fun detailPageTabUnselectedBorderColor(): Int =
        ColorsHelper.colorParser(ColorsHelper.instance()?.data?.config?.series_detail_tab_unselected_border_color.toString(), R.color.series_detail_tab_unselected_border_color)
    fun detailPageTabSelectedBorderColor(): Int =
        ColorsHelper.colorParser(ColorsHelper.instance()?.data?.config?.series_detail_tab_selected_bg_color.toString(), R.color.series_detail_tab_selected_bg_color)
    fun detailPageHDBrColor(): Int = ColorsHelper.colorParser(ColorsHelper.instance()?.data?.config?.series_detail_hd_border_color.toString(), R.color.series_detail_hd_border_color)
    fun detailPageHDBgColor(): Int = ColorsHelper.colorParser(ColorsHelper.instance()?.data?.config?.series_detail_hd_bg_color.toString(), R.color.series_detail_hd_bg_color)
    fun detailPageLikeSelectedColor(): Int = ColorsHelper.colorParser(ColorsHelper.instance()?.data?.config?.series_detail_like_selected_color.toString(), R.color.series_detail_like_selected_color)
    fun detailPageLikeUnselectedColor(): Int =
        ColorsHelper.colorParser(ColorsHelper.instance()?.data?.config?.series_detail_like_unselected_color.toString(), R.color.series_detail_like_unselected_color)
    fun detailPageMyListSelectedColor(): Int =
        ColorsHelper.colorParser(ColorsHelper.instance()?.data?.config?.series_detail_my_list_selected_color.toString(), R.color.series_detail_my_list_selected_color)
    fun detailPageMyListUnselectedColor(): Int =
        ColorsHelper.colorParser(ColorsHelper.instance()?.data?.config?.series_detail_my_list_unselected_color.toString(), R.color.series_detail_my_list_unselected_color)
    fun itemLabelBgColor(): Int = ColorsHelper.colorParser(ColorsHelper.instance()?.data?.config?.item_label_bg_color.toString(), R.color.item_label_bg_color)
    fun selectedIndicatorColor(): Int = ColorsHelper.colorParser(ColorsHelper.instance()?.data?.config?.selected_indicator_color.toString(), R.color.selected_indicator_color)
    fun unselectedIndicatorColor(): Int = ColorsHelper.colorParser(ColorsHelper.instance()?.data?.config?.unselected_indicator_color.toString(), R.color.unselected_indicator_color)
    fun popupBgColor(): Int = ColorsHelper.colorParser(ColorsHelper.instance()?.data?.config?.popup_Bg_Color.toString(), R.color.popup_Bg_Color)
    fun popupBrColor(): Int = ColorsHelper.colorParser(ColorsHelper.instance()?.data?.config?.popup_Br_Color.toString(), R.color.popup_Br_Color)
    fun clearColor(): Int = ColorsHelper.colorParser(ColorsHelper.instance()?.data?.config?.clear_Color.toString(), R.color.clear_Color)
    fun searchKeywordTextColor(): Int = ColorsHelper.colorParser(ColorsHelper.instance()?.data?.config?.search_keyword_text_color.toString(), R.color.search_keyword_text_color)
    fun searchKeywordHintColor(): Int = ColorsHelper.colorParser(ColorsHelper.instance()?.data?.config?.search_keyword_hint_color.toString(), R.color.search_keyword_hint_color)
}
