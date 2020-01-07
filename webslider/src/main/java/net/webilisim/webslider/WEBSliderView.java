package net.webilisim.webslider;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import net.webilisim.webslider.ViewIndicator.PageIndicatorView;
import net.webilisim.webslider.ViewIndicator.animation.type.AnimationType;
import net.webilisim.webslider.ViewIndicator.animation.type.BaseAnimation;
import net.webilisim.webslider.ViewIndicator.animation.type.ColorAnimation;
import net.webilisim.webslider.ViewIndicator.draw.controller.DrawController;
import net.webilisim.webslider.ViewIndicator.draw.data.Orientation;
import net.webilisim.webslider.ViewIndicator.draw.data.RtlMode;
import net.webilisim.webslider.ViewIndicator.utils.DensityUtils;

import com.smarteist.autoimageslider.R;

import net.webilisim.webslider.Transactions.AntiClockSpinTransformation;
import net.webilisim.webslider.Transactions.Clock_SpinTransformation;
import net.webilisim.webslider.Transactions.CubeInDepthTransformation;
import net.webilisim.webslider.Transactions.CubeInRotationTransformation;
import net.webilisim.webslider.Transactions.CubeInScalingTransformation;
import net.webilisim.webslider.Transactions.CubeOutDepthTransformation;
import net.webilisim.webslider.Transactions.CubeOutRotationTransformation;
import net.webilisim.webslider.Transactions.CubeOutScalingTransformation;
import net.webilisim.webslider.Transactions.DepthTransformation;
import net.webilisim.webslider.Transactions.FadeTransformation;
import net.webilisim.webslider.Transactions.FanTransformation;
import net.webilisim.webslider.Transactions.FidgetSpinTransformation;
import net.webilisim.webslider.Transactions.GateTransformation;
import net.webilisim.webslider.Transactions.HingeTransformation;
import net.webilisim.webslider.Transactions.HorizontalFlipTransformation;
import net.webilisim.webslider.Transactions.PopTransformation;
import net.webilisim.webslider.Transactions.SimpleTransformation;
import net.webilisim.webslider.Transactions.SpinnerTransformation;
import net.webilisim.webslider.Transactions.TossTransformation;
import net.webilisim.webslider.Transactions.VerticalFlipTransformation;
import net.webilisim.webslider.Transactions.VerticalShutTransformation;
import net.webilisim.webslider.Transactions.ZoomOutTransformation;

import net.webilisim.webslider.ViewIndicator.draw.controller.AttributeController;

public class WEBSliderView extends FrameLayout {

    public static final int AUTO_CYCLE_DIRECTION_RIGHT = 0;
    public static final int AUTO_CYCLE_DIRECTION_LEFT = 1;
    public static final int AUTO_CYCLE_DIRECTION_BACK_AND_FORTH = 2;

    private final Handler mHandler = new Handler();
    private boolean mFlagBackAndForth;
    private boolean mIsAutoCycle;
    private int mAutoCycleDirection;
    private int mScrollTimeInSec;
    private CircularSliderHandle mCircularSliderHandle;
    private PageIndicatorView mPagerIndicator;
    private DataSetObserver mDataSetObserver;
    private PagerAdapter mPagerAdapter;
    private Runnable mSliderRunnable;
    private WEBSliderPager mWEBSliderPager;

    public WEBSliderView(Context context) {
        super(context);
        setupSlideView(context);
    }

    public WEBSliderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupSlideView(context);
        setUpAttributes(context, attrs);
    }

    public WEBSliderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupSlideView(context);
        setUpAttributes(context, attrs);
    }

    private void setUpAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WEBSliderView, 0, 0);

        int indicatorOrientation = typedArray.getInt(R.styleable.WEBSliderView_sliderIndicatorOrientation, Orientation.HORIZONTAL.ordinal());
        Orientation orientation;
        if (indicatorOrientation == 0) {
            orientation = Orientation.HORIZONTAL;
        } else {
            orientation = Orientation.VERTICAL;
        }
        int indicatorRadius = (int) typedArray.getDimension(R.styleable.WEBSliderView_sliderIndicatorRadius, DensityUtils.dpToPx(2));
        int indicatorPadding = (int) typedArray.getDimension(R.styleable.WEBSliderView_sliderIndicatorPadding, DensityUtils.dpToPx(3));
        int indicatorMargin = (int) typedArray.getDimension(R.styleable.WEBSliderView_sliderIndicatorMargin, DensityUtils.dpToPx(12));
        int indicatorMarginLeft = (int) typedArray.getDimension(R.styleable.WEBSliderView_sliderIndicatorMarginLeft, DensityUtils.dpToPx(12));
        int indicatorMarginTop = (int) typedArray.getDimension(R.styleable.WEBSliderView_sliderIndicatorMarginTop, DensityUtils.dpToPx(12));
        int indicatorMarginRight = (int) typedArray.getDimension(R.styleable.WEBSliderView_sliderIndicatorMarginRight, DensityUtils.dpToPx(12));
        int indicatorMarginBottom = (int) typedArray.getDimension(R.styleable.WEBSliderView_sliderIndicatorMarginBottom, DensityUtils.dpToPx(12));
        int indicatorGravity = typedArray.getInt(R.styleable.WEBSliderView_sliderIndicatorGravity, Gravity.CENTER | Gravity.BOTTOM);
        int indicatorUnselectedColor = typedArray.getColor(R.styleable.WEBSliderView_sliderIndicatorUnselectedColor, Color.parseColor(ColorAnimation.DEFAULT_UNSELECTED_COLOR));
        int indicatorSelectedColor = typedArray.getColor(R.styleable.WEBSliderView_sliderIndicatorSelectedColor, Color.parseColor(ColorAnimation.DEFAULT_SELECTED_COLOR));
        int indicatorAnimationDuration = typedArray.getInt(R.styleable.WEBSliderView_sliderIndicatorAnimationDuration, BaseAnimation.DEFAULT_ANIMATION_TIME);
        int indicatorRtlMode = typedArray.getInt(R.styleable.WEBSliderView_sliderIndicatorRtlMode, RtlMode.Off.ordinal());
        RtlMode rtlMode = AttributeController.getRtlMode(indicatorRtlMode);
        int sliderAnimationDuration = typedArray.getInt(R.styleable.WEBSliderView_sliderAnimationDuration, WEBSliderPager.DEFAULT_SCROLL_DURATION);
        int sliderScrollTimeInSec = typedArray.getInt(R.styleable.WEBSliderView_sliderScrollTimeInSec, 2);
        boolean sliderCircularHandlerEnabled = typedArray.getBoolean(R.styleable.WEBSliderView_sliderCircularHandlerEnabled, true);
        boolean sliderAutoCycleEnabled = typedArray.getBoolean(R.styleable.WEBSliderView_sliderAutoCycleEnabled, true);
        boolean sliderStartAutoCycle = typedArray.getBoolean(R.styleable.WEBSliderView_sliderStartAutoCycle, false);
        int sliderAutoCycleDirection = typedArray.getInt(R.styleable.WEBSliderView_sliderAutoCycleDirection, AUTO_CYCLE_DIRECTION_RIGHT);

        setIndicatorOrientation(orientation);
        setIndicatorRadius(indicatorRadius);
        setIndicatorPadding(indicatorPadding);
        setIndicatorMargin(indicatorMargin);
        if(R.styleable.WEBSliderView_sliderIndicatorMargin == 0){
            setIndicatorMarginCustom(indicatorMarginLeft,indicatorMarginTop,indicatorMarginRight,indicatorMarginBottom);
        }
        setIndicatorGravity(indicatorGravity);
        setIndicatorUnselectedColor(indicatorUnselectedColor);
        setIndicatorSelectedColor(indicatorSelectedColor);
        setIndicatorAnimationDuration(indicatorAnimationDuration);
        setIndicatorRtlMode(rtlMode);
        setSliderAnimationDuration(sliderAnimationDuration);
        setScrollTimeInSec(sliderScrollTimeInSec);
        setCircularHandlerEnabled(sliderCircularHandlerEnabled);
        setAutoCycle(sliderAutoCycleEnabled);
        setAutoCycleDirection(sliderAutoCycleDirection);
        if (sliderStartAutoCycle) {
            startAutoCycle();
        }

        typedArray.recycle();
    }

    private void setupSlideView(Context context) {

        View wrapperView = LayoutInflater
                .from(context)
                .inflate(R.layout.slider_view, this, true);

        mWEBSliderPager = wrapperView.findViewById(R.id.vp_slider_layout);
        mCircularSliderHandle = new CircularSliderHandle(mWEBSliderPager);
        mWEBSliderPager.addOnPageChangeListener(mCircularSliderHandle);
        mWEBSliderPager.setOffscreenPageLimit(4);

        mPagerIndicator = wrapperView.findViewById(R.id.pager_indicator);
        mPagerIndicator.setViewPager(mWEBSliderPager);
    }

    public void setOnIndicatorClickListener(DrawController.ClickListener listener) {
        mPagerIndicator.setClickListener(listener);
    }

    public void setCurrentPageListener(CircularSliderHandle.CurrentPageListener listener) {
        mCircularSliderHandle.setCurrentPageListener(listener);
    }

    public void setSliderAdapter(final PagerAdapter pagerAdapter) {
        mPagerAdapter = pagerAdapter;
        //set slider adapter
        //registerAdapterDataObserver();
        mWEBSliderPager.setAdapter(pagerAdapter);
        //setup with indicator
        mPagerIndicator.setCount(getAdapterItemsCount());
        mPagerIndicator.setDynamicCount(true);
    }

    public PagerAdapter getSliderAdapter() {
        return mPagerAdapter;
    }

    private void registerAdapterDataObserver() {

        if (mDataSetObserver != null) {
            mPagerAdapter.unregisterDataSetObserver(mDataSetObserver);
        }

        mDataSetObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                mWEBSliderPager.setOffscreenPageLimit(getAdapterItemsCount() - 1);
            }
        };

        mPagerAdapter.registerDataSetObserver(mDataSetObserver);
    }

    public boolean isAutoCycle() {
        return mIsAutoCycle;
    }

    public void setAutoCycle(boolean autoCycle) {
        this.mIsAutoCycle = autoCycle;
        if (!mIsAutoCycle && mSliderRunnable != null) {
            mHandler.removeCallbacks(mSliderRunnable);
            mSliderRunnable = null;
        }
    }

    public void setOffscreenPageLimit(int limit) {
        mWEBSliderPager.setOffscreenPageLimit(limit);
    }

    public void setCircularHandlerEnabled(boolean enable) {
        mWEBSliderPager.clearOnPageChangeListeners();
        if (enable) {
            mWEBSliderPager.addOnPageChangeListener(mCircularSliderHandle);
        }
    }

    public int getScrollTimeInSec() {
        return mScrollTimeInSec;
    }

    public void setScrollTimeInSec(int time) {
        mScrollTimeInSec = time;
    }

    public void setSliderTransformAnimation(WEBSliderAnimations animation) {

        switch (animation) {
            case ANTICLOCKSPINTRANSFORMATION:
                mWEBSliderPager.setPageTransformer(false, new AntiClockSpinTransformation());
                break;
            case CLOCK_SPINTRANSFORMATION:
                mWEBSliderPager.setPageTransformer(false, new Clock_SpinTransformation());
                break;
            case CUBEINDEPTHTRANSFORMATION:
                mWEBSliderPager.setPageTransformer(false, new CubeInDepthTransformation());
                break;
            case CUBEINROTATIONTRANSFORMATION:
                mWEBSliderPager.setPageTransformer(false, new CubeInRotationTransformation());
                break;
            case CUBEINSCALINGTRANSFORMATION:
                mWEBSliderPager.setPageTransformer(false, new CubeInScalingTransformation());
                break;
            case CUBEOUTDEPTHTRANSFORMATION:
                mWEBSliderPager.setPageTransformer(false, new CubeOutDepthTransformation());
                break;
            case CUBEOUTROTATIONTRANSFORMATION:
                mWEBSliderPager.setPageTransformer(false, new CubeOutRotationTransformation());
                break;
            case CUBEOUTSCALINGTRANSFORMATION:
                mWEBSliderPager.setPageTransformer(false, new CubeOutScalingTransformation());
                break;
            case DEPTHTRANSFORMATION:
                mWEBSliderPager.setPageTransformer(false, new DepthTransformation());
                break;
            case FADETRANSFORMATION:
                mWEBSliderPager.setPageTransformer(false, new FadeTransformation());
                break;
            case FANTRANSFORMATION:
                mWEBSliderPager.setPageTransformer(false, new FanTransformation());
                break;
            case FIDGETSPINTRANSFORMATION:
                mWEBSliderPager.setPageTransformer(false, new FidgetSpinTransformation());
                break;
            case GATETRANSFORMATION:
                mWEBSliderPager.setPageTransformer(false, new GateTransformation());
                break;
            case HINGETRANSFORMATION:
                mWEBSliderPager.setPageTransformer(false, new HingeTransformation());
                break;
            case HORIZONTALFLIPTRANSFORMATION:
                mWEBSliderPager.setPageTransformer(false, new HorizontalFlipTransformation());
                break;
            case POPTRANSFORMATION:
                mWEBSliderPager.setPageTransformer(false, new PopTransformation());
                break;
            case SIMPLETRANSFORMATION:
                mWEBSliderPager.setPageTransformer(false, new SimpleTransformation());
                break;
            case SPINNERTRANSFORMATION:
                mWEBSliderPager.setPageTransformer(false, new SpinnerTransformation());
                break;
            case TOSSTRANSFORMATION:
                mWEBSliderPager.setPageTransformer(false, new TossTransformation());
                break;
            case VERTICALFLIPTRANSFORMATION:
                mWEBSliderPager.setPageTransformer(false, new VerticalFlipTransformation());
                break;
            case VERTICALSHUTTRANSFORMATION:
                mWEBSliderPager.setPageTransformer(false, new VerticalShutTransformation());
                break;
            case ZOOMOUTTRANSFORMATION:
                mWEBSliderPager.setPageTransformer(false, new ZoomOutTransformation());
                break;
            default:
                mWEBSliderPager.setPageTransformer(false, new SimpleTransformation());

        }

    }

    public void setCustomSliderTransformAnimation(ViewPager.PageTransformer animation) {
        mWEBSliderPager.setPageTransformer(false, animation);
    }

    public void setSliderAnimationDuration(int duration) {
        mWEBSliderPager.setScrollDuration(duration);
    }

    public void setCurrentPagePosition(int position) {

        if (getSliderAdapter() != null) {
            mWEBSliderPager.setCurrentItem(position, true);
        } else {
            throw new NullPointerException("Adapter not set");
        }
    }

    public int getCurrentPagePosition() {

        if (getSliderAdapter() != null) {
            return mWEBSliderPager.getCurrentItem();
        } else {
            throw new NullPointerException("Adapter not set");
        }
    }

    public void setIndicatorAnimationDuration(long duration) {
        mPagerIndicator.setAnimationDuration(duration);
    }

    public void setIndicatorGravity(int gravity) {
        FrameLayout.LayoutParams layoutParams = (LayoutParams) mPagerIndicator.getLayoutParams();
        layoutParams.gravity = gravity;
        mPagerIndicator.setLayoutParams(layoutParams);
    }

    public void setIndicatorPadding(int padding) {
        mPagerIndicator.setPadding(padding);
    }

    public void setIndicatorOrientation(Orientation orientation) {
        mPagerIndicator.setOrientation(orientation);
    }

    public void setIndicatorAnimation(IndicatorAnimations animations) {

        switch (animations) {
            case DROP:
                mPagerIndicator.setAnimationType(AnimationType.DROP);
                break;
            case FILL:
                mPagerIndicator.setAnimationType(AnimationType.FILL);
                break;
            case NONE:
                mPagerIndicator.setAnimationType(AnimationType.NONE);
                break;
            case SWAP:
                mPagerIndicator.setAnimationType(AnimationType.SWAP);
                break;
            case WORM:
                mPagerIndicator.setAnimationType(AnimationType.WORM);
                break;
            case COLOR:
                mPagerIndicator.setAnimationType(AnimationType.COLOR);
                break;
            case SCALE:
                mPagerIndicator.setAnimationType(AnimationType.SCALE);
                break;
            case SLIDE:
                mPagerIndicator.setAnimationType(AnimationType.SLIDE);
                break;
            case SCALE_DOWN:
                mPagerIndicator.setAnimationType(AnimationType.SCALE_DOWN);
                break;
            case THIN_WORM:
                mPagerIndicator.setAnimationType(AnimationType.THIN_WORM);
                break;
        }
    }

    public void setIndicatorVisibility(boolean visibility) {
        if (visibility) {
            mPagerIndicator.setVisibility(VISIBLE);
        } else {
            mPagerIndicator.setVisibility(GONE);
        }
    }

    private int getAdapterItemsCount() {
        try {
            return getSliderAdapter().getCount();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public void startAutoCycle() {

        if (mSliderRunnable != null) {
            mHandler.removeCallbacks(mSliderRunnable);
            mSliderRunnable = null;
        }

        mSliderRunnable = new Runnable() {
            @Override
            public void run() {

                try {
                    // check is on auto scroll mode
                    if (!mIsAutoCycle) {
                        return;
                    }

                    int currentPosition = mWEBSliderPager.getCurrentItem();

                    if (mAutoCycleDirection == AUTO_CYCLE_DIRECTION_BACK_AND_FORTH) {
                        if (currentPosition == 0) {
                            mFlagBackAndForth = true;
                        }
                        if (currentPosition == getAdapterItemsCount() - 1) {
                            mFlagBackAndForth = false;
                        }
                        if (mFlagBackAndForth) {
                            mWEBSliderPager.setCurrentItem(++currentPosition, true);
                        } else {
                            mWEBSliderPager.setCurrentItem(--currentPosition, true);
                        }
                    } else if (mAutoCycleDirection == AUTO_CYCLE_DIRECTION_LEFT) {
                        if (currentPosition == 0) {
                            mWEBSliderPager.setCurrentItem(getAdapterItemsCount() - 1, true);
                        } else {
                            mWEBSliderPager.setCurrentItem(--currentPosition, true);
                        }
                    } else {
                        if (currentPosition == getAdapterItemsCount() - 1) {
                            // if is last item return to the first position
                            mWEBSliderPager.setCurrentItem(0, true);
                        } else {
                            // continue smooth transition between pager
                            mWEBSliderPager.setCurrentItem(++currentPosition, true);
                        }
                    }


                } finally {
                    mHandler.postDelayed(this, mScrollTimeInSec * 1000);
                }

            }
        };

        //Run the loop for the first time
        mHandler.postDelayed(mSliderRunnable, mScrollTimeInSec * 1000);
    }

    public void setAutoCycleDirection(int direction) {
        mAutoCycleDirection = direction;
    }

    public int getAutoCycleDirection() {
        return mAutoCycleDirection;
    }

    public int getIndicatorRadius() {
        return mPagerIndicator.getRadius();
    }

    public void setIndicatorRtlMode(RtlMode rtlMode) {
        mPagerIndicator.setRtlMode(rtlMode);
    }

    public void setIndicatorRadius(int pagerIndicatorRadius) {
        this.mPagerIndicator.setRadius(pagerIndicatorRadius);
    }

    public void setIndicatorMargin(int margin) {
        FrameLayout.LayoutParams layoutParams = (LayoutParams) mPagerIndicator.getLayoutParams();
        layoutParams.setMargins(margin, margin, margin, margin);
        mPagerIndicator.setLayoutParams(layoutParams);
    }
    
    public void setIndicatorMarginCustom(int left,int top,int right,int bottom) {
        FrameLayout.LayoutParams layoutParams = (LayoutParams) mPagerIndicator.getLayoutParams();
        layoutParams.setMargins(left, top, right, bottom);
        mPagerIndicator.setLayoutParams(layoutParams);
    }

    public void setIndicatorSelectedColor(int color) {
        this.mPagerIndicator.setSelectedColor(color);
    }

    public int getIndicatorSelectedColor() {
        return this.mPagerIndicator.getSelectedColor();
    }

    public void setIndicatorUnselectedColor(int color) {
        this.mPagerIndicator.setUnselectedColor(color);
    }

    public int getIndicatorUnselectedColor() {
        return this.mPagerIndicator.getUnselectedColor();
    }

}
