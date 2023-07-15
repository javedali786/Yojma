package com.breadgangtvnetwork.utils.helpers.carousel

import android.content.Context
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.breadgangtvnetwork.R
import com.breadgangtvnetwork.beanModel.enveuCommonRailData.RailCommonData
import com.breadgangtvnetwork.callbacks.commonCallbacks.CommonRailtItemClickListner
import com.breadgangtvnetwork.utils.Logger
import com.breadgangtvnetwork.utils.colorsJson.converter.AppColors
import com.breadgangtvnetwork.utils.colorsJson.converter.ColorsHelper
import com.breadgangtvnetwork.utils.commonMethods.AppCommonMethod.Companion.convertDpToPixel
import com.breadgangtvnetwork.utils.constants.AppConstants
import com.breadgangtvnetwork.utils.helpers.carousel.adapter.SliderAdapter
import com.breadgangtvnetwork.utils.helpers.carousel.indicators.IndicatorShape
import com.breadgangtvnetwork.utils.helpers.carousel.indicators.SlideIndicatorsGroup
import com.breadgangtvnetwork.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import com.enveu.client.enums.CRIndicator
import java.security.SecureRandom

class Slider : FrameLayout, OnPageChangeListener {
    private val handler = Handler()
    private var viewPager: LooperWrapViewPager? = null
    private var itemClickListener: AdapterView.OnItemClickListener? = null
    private var selectedSlideIndicator: Drawable? = null
    private var unSelectedSlideIndicator: Drawable? = null
    private var defaultIndicator = 0
    private var indicatorSize = 0
    private var mustAnimateIndicators = false
    private var mustLoopSlides = false
    private var slideIndicatorsGroup: SlideIndicatorsGroup? = null
    private var slideShowInterval = 4000
    private var slideCount = 0
    private var currentPageNumber = 0
    private var hideIndicators = false
    private val layoutType = -1

    constructor(context: Context) : super(context) {}

    var attrset: AttributeSet? = null

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        attrset = attrs
    }

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        attrset = attrs
    }

    private fun parseCustomAttributes(attributeSet: AttributeSet?) {
        try {
            if (attributeSet != null) {
                val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.BannerSlider)
                try {
                    indicatorSize = typedArray.getDimension(R.styleable.BannerSlider_indicatorSize, resources.getDimensionPixelSize(R.dimen.slider_circle_size).toFloat()).toInt()
                    selectedSlideIndicator = typedArray.getDrawable(R.styleable.BannerSlider_selected_slideIndicator)
                    unSelectedSlideIndicator = typedArray.getDrawable(R.styleable.BannerSlider_unselected_slideIndicator)
                    defaultIndicator = typedArray.getInt(R.styleable.BannerSlider_defaultIndicators, IndicatorShape.CIRCLE)
                    mustAnimateIndicators = typedArray.getBoolean(R.styleable.BannerSlider_animateIndicators, true)
                    mustLoopSlides = typedArray.getBoolean(R.styleable.BannerSlider_loopSlides, true)
                    hideIndicators = typedArray.getBoolean(R.styleable.BannerSlider_hideIndicators, false)
                    Logger.d("carousalValue: " + KsPreferenceKeys.getInstance().autoRotation + " " + KsPreferenceKeys.getInstance().autoDuration + " " + mustLoopSlides)
                    if (KsPreferenceKeys.getInstance().autoRotation) {
                        if (KsPreferenceKeys.getInstance().autoDuration > 0) {
                            val slideShowIntervalSecond = typedArray.getInt(R.styleable.BannerSlider_intervalSecond, KsPreferenceKeys.getInstance().autoDuration)
                            Logger.d("carousalValue++ $slideShowIntervalSecond")
                            slideShowInterval = slideShowIntervalSecond * 1000
                            Logger.d("carousalValue++-- $slideShowInterval")
                        } else {
                            val slideShowIntervalSecond = typedArray.getInt(R.styleable.BannerSlider_intervalSecond, 3)
                            slideShowInterval = slideShowIntervalSecond * 1000
                        }
                    }
                } catch (e: Exception) {
                    Logger.w(e)
                } finally {
                    typedArray.recycle()
                }
            }
        } catch (e: Exception) {
            Logger.w(e)
        }
    }

    var autoRotate = true
    var autoRotateDuration = 0
    fun addSlides(
        slideList: RailCommonData?,
        listner: CommonRailtItemClickListner?,
        position: Int,
        layoutType: Int,
        indicatorPosition: String,
        auto: Boolean,
        rotateDuration: Int,
        redirectedFrom: String?
    ) {
        parseCustomAttributes(attrset)
        if (slideList == null || slideList.enveuVideoItemBeans.size == 0) return
        viewPager = LooperWrapViewPager(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            viewPager!!.id = generateViewId()
        } else {
            val id = Math.abs(SecureRandom().nextInt(5000 - 1000 + 1) + 1000)
            viewPager!!.id = id
        }
        viewPager!!.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        viewPager!!.addOnPageChangeListener(this@Slider)
        addView(viewPager)
        val adapter = SliderAdapter(context, slideList, layoutType, listner, position)
        viewPager!!.adapter = adapter
        adapter.notifyDataSetChanged()
        slideCount = slideList.enveuVideoItemBeans.size
        viewPager!!.currentItem = 0
        viewPager!!.clipToPadding = false
        //viewPager.setBackgroundColor(getResources().getColor(R.color.buy_now_pay_now_btn_text_color_theme_color));
        val tabletSize = resources.getBoolean(R.bool.isTablet)
        autoRotate = auto
        autoRotateDuration = rotateDuration
        if (tabletSize) {
            val orientation = resources.configuration.orientation
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                viewPager!!.setPadding(getViewPagerPadding(layoutType), 0, getViewPagerPadding(layoutType), 0)
            } else {
                viewPager!!.setPadding(getViewPagerTabletPadding(layoutType), 0, getViewPagerTabletPadding(layoutType), 0)

                // viewPager.setPadding(getViewPagerTabletPadding(layoutType), 10, getViewPagerTabletPadding(layoutType), 10);
            }
        } else {
            viewPager!!.setPadding(0, 0, 0, 0)
        }
        viewPager!!.pageMargin = 20
        if (indicatorPosition.equals(CRIndicator.HDN.name, ignoreCase = true)) {
            hideIndicators = true
        }
        if (!hideIndicators && slideCount > 1) {
            slideIndicatorsGroup = SlideIndicatorsGroup(context, selectedSlideIndicator, unSelectedSlideIndicator, defaultIndicator, indicatorSize, mustAnimateIndicators, layoutType, indicatorPosition, redirectedFrom)
            slideIndicatorsGroup!!.setBackgroundColor(resources.getColor(R.color.tph_hint_txt_color))
            addView(slideIndicatorsGroup)
            slideIndicatorsGroup!!.setSlides(slideCount)

            slideIndicatorsGroup!!.changeIndicator(ColorsHelper.strokeBgDrawable(AppColors.selectedIndicatorColor(), AppColors.selectedIndicatorColor() , 200f),
                ColorsHelper.strokeBgDrawable(AppColors.unselectedIndicatorColor(), AppColors.unselectedIndicatorColor() , 200f))

            slideIndicatorsGroup!!.onSlideChange(0)
            slideIndicatorsGroup!!.setBackgroundColor(context.resources.getColor(R.color.transparent))
        }
        if (slideCount > 1) {
            Logger.d("carousalValue-- $auto")
            if (autoRotate) {
                setupTimer()
            }
        }
    }

    private fun getViewPagerTabletPadding(layoutType: Int): Int {
        return when (layoutType) {
            AppConstants.CAROUSEL_LDS_LANDSCAPE -> 0
            AppConstants.CAROUSEL_PR_POTRAIT -> resources.getDimension(R.dimen.carousal_potrait_padding).toInt()
            AppConstants.CAROUSEL_SQR_SQUARE -> 200
            AppConstants.CAROUSEL_CIR_CIRCLE -> convertDpToPixel(40)
            else -> 0
        }
    }

    private fun getViewPagerPadding(layoutType: Int): Int {
        return when (layoutType) {
            AppConstants.CAROUSEL_LDS_LANDSCAPE -> resources.getDimension(R.dimen.carousal_landscape_padding).toInt()
            AppConstants.CAROUSEL_PR_POTRAIT -> resources.getDimension(R.dimen.carousal_potrait_padding).toInt()
            AppConstants.CAROUSEL_SQR_SQUARE -> resources.getDimension(R.dimen.carousal_square_padding).toInt()
            AppConstants.CAROUSEL_CIR_CIRCLE -> convertDpToPixel(40)
            else -> 0
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
    override fun onPageSelected(position: Int) {
        currentPageNumber = position
        if (slideIndicatorsGroup != null && !hideIndicators) {
            if (position == 0) {
                slideIndicatorsGroup!!.onSlideChange(0)
            } else if (position == slideCount) {
                slideIndicatorsGroup!!.onSlideChange(slideCount)
            } else {
                slideIndicatorsGroup!!.onSlideChange(position)
            }
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
        when (state) {
            ViewPager.SCROLL_STATE_DRAGGING -> handler.removeCallbacksAndMessages(null)
            ViewPager.SCROLL_STATE_IDLE -> if (autoRotate) {
                setupTimer()
            }
        }
    }

    private fun setupTimer() {
        try {
            if (mustLoopSlides) {
                handler.postDelayed(object : Runnable {
                    override fun run() {
                        try {
                            if (currentPageNumber < slideCount) currentPageNumber += 1 else currentPageNumber = 1
                            viewPager!!.setCurrentItem(currentPageNumber - 1, true)
                            handler.removeCallbacksAndMessages(null)
                            handler.postDelayed(this, slideShowInterval.toLong())
                        } catch (e: Exception) {
                            Logger.w(e)
                        }
                    }
                }, slideShowInterval.toLong())
            }
        } catch (e: Exception) {
            Logger.w(e)
        }
    }

    // setters
    fun setItemClickListener(itemClickListener: AdapterView.OnItemClickListener?) {
        this.itemClickListener = itemClickListener
    }

    fun setHideIndicators(hideIndicators: Boolean) {
        this.hideIndicators = hideIndicators
        try {
            if (hideIndicators) slideIndicatorsGroup!!.visibility = INVISIBLE else slideIndicatorsGroup!!.visibility = VISIBLE
        } catch (e: Exception) {
            Logger.w(e)
        }
    }

    companion object {
        const val TAG = "SLIDER"
    }
}