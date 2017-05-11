/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright (C) 2011 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.viewpagerindicator;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import go.fast.fastenvelopes.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;


/**
 * This widget implements the dynamic action bar tab behavior that can change
 * across different configurations or circumstances.
 */
public class NewTabPageIndicator extends LinearLayout implements PageIndicator {
    /** Title text used when no title is provided by the adapter. */
    private static final CharSequence EMPTY_TITLE = "";

    /**
     * Interface for a callback when the selected tab has been reselected.
     */
    public interface OnTabReselectedListener {
	/**
	 * Callback when the selected tab has been reselected.
	 *
	 * @param position
	 *            Position of the current center item.
	 */
	void onTabReselected(int position);
    }

    private Runnable mTabSelector;

    private final OnClickListener mTabClickListener = new OnClickListener() {
	public void onClick(View view) {
	    TabView tabView = (TabView) view;
	    final int oldSelected = mViewPager.getCurrentItem();
	    final int newSelected = tabView.getIndex();
	    mViewPager.setCurrentItem(newSelected);
	    if (oldSelected == newSelected && mTabReselectedListener != null) {
		mTabReselectedListener.onTabReselected(newSelected);
	    }
	}
    };

    private final IcsLinearLayout mTabLayout;

    private ViewPager mViewPager;
    private OnPageChangeListener mListener;

    private int mMaxTabWidth;
    private int mSelectedTabIndex;
    private Context context;
    private OnTabReselectedListener mTabReselectedListener;

    public NewTabPageIndicator(Context context) {
	this(context, null);
	this.context = context;
    }

    public NewTabPageIndicator(Context context, AttributeSet attrs) {
	super(context, attrs);
	this.context = context;
	setHorizontalScrollBarEnabled(false);

	mTabLayout = new IcsLinearLayout(context,
		R.attr.vpiTabPageIndicatorStyle);
	addView(mTabLayout, new ViewGroup.LayoutParams(MATCH_PARENT,
		MATCH_PARENT));
    }

    public void setOnTabReselectedListener(OnTabReselectedListener listener) {
	mTabReselectedListener = listener;
    }

    // @Override
    // public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    // final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
    // final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
    // // setFillViewport(lockedExpanded);
    //
    // final int childCount = mTabLayout.getChildCount();
    // if (childCount > 1
    // && (widthMode == MeasureSpec.EXACTLY || widthMode ==
    // MeasureSpec.AT_MOST)) {
    // if (childCount > 2) {
    // mMaxTabWidth = (int) (MeasureSpec.getSize(widthMeasureSpec) * 0.4f);
    // } else {
    // mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
    // }
    // } else {
    // mMaxTabWidth = -1;
    // }
    //
    // final int oldWidth = getMeasuredWidth();
    // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    // final int newWidth = getMeasuredWidth();
    //
    // if (lockedExpanded && oldWidth != newWidth) {
    // // Recenter the tab display if we're at a new (scrollable) size.
    // setCurrentItem(mSelectedTabIndex);
    // }
    // }

    private void animateToTab(final int position) {
	final View tabView = mTabLayout.getChildAt(position);
	if (mTabSelector != null) {
	    removeCallbacks(mTabSelector);
	}
	mTabSelector = new Runnable() {
	    public void run() {
		final int scrollPos = tabView.getLeft()
			- (getWidth() - tabView.getWidth()) / 2;
		// smoothScrollTo(scrollPos, 0);
		mTabSelector = null;
	    }
	};
	post(mTabSelector);
    }

    @Override
    public void onAttachedToWindow() {
	super.onAttachedToWindow();
	if (mTabSelector != null) {
	    // Re-post the selector we saved
	    post(mTabSelector);
	}
    }

    @Override
    public void onDetachedFromWindow() {
	super.onDetachedFromWindow();
	if (mTabSelector != null) {
	    removeCallbacks(mTabSelector);
	}
    }

    private void addTab(int index, CharSequence text, int iconResId,
	    int backGroundId, int textSize, int textColor) {
	final TabView tabView = new TabView(getContext(),index);
	tabView.mIndex = index;
	tabView.setFocusable(true);
	tabView.setOnClickListener(mTabClickListener);
	tabView.setText(text);
	WindowManager wm = (WindowManager) context
		.getSystemService(Context.WINDOW_SERVICE);
	DisplayMetrics dm = new DisplayMetrics();
	wm.getDefaultDisplay().getMetrics(dm);// 屏幕宽度
	int width = dm.widthPixels;
	// tabView.setPadding(
	// (int) dp2Pixel((int) width > 1000 ? 11 : ((width > 600 ? 10
	// : (width > 479 ? 8 : (width > 319 ? 7 : 6)))), context),
	// 0, 0, 0);
	if (textSize != 0) {
	    tabView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
	} else {
	    tabView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
	}
	ColorStateList csl;
	if (textColor != 0) {
	    csl = (ColorStateList) context.getResources().getColorStateList(
		    textColor);
	} else {

	    csl = (ColorStateList) context.getResources().getColorStateList(
		    R.color.perm_title_color);
	}
	if (csl != null) {
	    tabView.setTextColor(csl);
	}
	if (iconResId != 0) {
	    tabView.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0);
	    // tabView.setBackgroundResource(iconResId);
	}
	if (backGroundId != 0) {
	    // tabView.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0,
	    // 0);
	    tabView.setBackgroundResource(backGroundId);
	}
	mTabLayout.addView(tabView, new LayoutParams(0,
		MATCH_PARENT, 1));
    }

    private void addTab(int index, CharSequence text, int iconResId,
	    int backGroundId, int textSize, int textColor, boolean isNew) {

	final TabView tabView = new TabView(getContext(),index);
	tabView.mIndex = index;
	tabView.setFocusable(true);
	tabView.setOnClickListener(mTabClickListener);
	tabView.setText(text);
	WindowManager wm = (WindowManager) context
		.getSystemService(Context.WINDOW_SERVICE);
	DisplayMetrics dm = new DisplayMetrics();
	wm.getDefaultDisplay().getMetrics(dm);// 屏幕宽度
	int width = dm.widthPixels;
	// tabView.setPadding(
	// (int) dp2Pixel(paddingLeft == 0 ? (int) width > 1000 ? 17
	// : ((width > 600 ? 16 : (width > 479 ? 13
	// : (width > 319 ? 12 : 11)))) : paddingLeft,
	// context), 0,
	// paddingRight == 0 ? (int) dp2Pixel(2, context) : paddingRight,
	// 0);
	if (textSize != 0) {
	    tabView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
	} else {
	    tabView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
	}
	ColorStateList csl;
	if (textColor != 0) {
	    csl = (ColorStateList) context.getResources().getColorStateList(
		    textColor);
	} else {

	    csl = (ColorStateList) context.getResources().getColorStateList(
		    R.color.perm_title_color);
	}
	if (csl != null) {
	    tabView.setTextColor(csl);
	}
	if (iconResId != 0) {
	    tabView.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0,
		    isNew ? R.drawable.friend_play_new_point : 0, 0);
	    // tabView.setBackgroundResource(iconResId);
	}
	if (backGroundId != 0) {
	    // tabView.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0,
	    // 0);
	    tabView.setBackgroundResource(backGroundId);
	}
	LayoutParams lp = new LayoutParams(0,
		MATCH_PARENT, 1);
	// if(index==0)
	// {
	// lp.gravity=Gravity.RIGHT;
	// lp.rightMargin=PixelUtil.dp2Pixel(20, context);
	// }
	// else
	// {
	// lp.gravity=Gravity.LEFT;
	// lp.leftMargin=PixelUtil.dp2Pixel(20, context);
	// }
	mTabLayout.addView(tabView, lp);
    }

    private int paddingLeft;
    private int paddingRight;

    public void setLabelPadding(int left, int right) {
	paddingLeft = left;
	paddingRight = right;
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
	if (mListener != null) {
	    mListener.onPageScrollStateChanged(arg0);
	}
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
	if (mListener != null) {
	    mListener.onPageScrolled(arg0, arg1, arg2);
	}
    }

    @Override
    public void onPageSelected(int arg0) {
	setCurrentItem(arg0);
	if (mListener != null) {
	    mListener.onPageSelected(arg0);
	}
    }

    @Override
    public void setViewPager(ViewPager view) {
	if (mViewPager == view) {
	    return;
	}
	if (mViewPager != null) {
	    mViewPager.setOnPageChangeListener(null);
	}
	final PagerAdapter adapter = view.getAdapter();
	if (adapter == null) {
	    throw new IllegalStateException(
		    "ViewPager does not have adapter instance.");
	}
	mViewPager = view;
	view.setOnPageChangeListener(this);
	notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
	mTabLayout.removeAllViews();
	PagerAdapter adapter = mViewPager.getAdapter();
	IconPagerAdapter iconAdapter = null;
	if (adapter instanceof IconPagerAdapter) {
	    iconAdapter = (IconPagerAdapter) adapter;
	}
	final int count = adapter.getCount();
	for (int i = 0; i < count; i++) {
	    CharSequence title = adapter.getPageTitle(i);
	    if (title == null) {
		title = EMPTY_TITLE;
	    }
	    int iconResId = 0;
	    int backgroundResId = 0;
	    int textSize = 0;
	    int textColor = 0;
	    if (iconAdapter != null) {
		iconResId = iconAdapter.getIconResId(i);
		backgroundResId = iconAdapter.getBackgroundResId(i);
		textSize = iconAdapter.getTextSize();
		textColor = iconAdapter.getTextColor();
	    }
	    if (count < 5) {
		addTab(i, title, iconResId, backgroundResId, textSize,
			textColor, false);
	    } else {
		addTab(i, title, iconResId, backgroundResId, textSize,
			textColor);
	    }
	}
	if (mSelectedTabIndex > count) {
	    mSelectedTabIndex = count - 1;
	}
	setCurrentItem(mSelectedTabIndex);
	requestLayout();
    }

    public void notifyDataSetChanged(boolean[] isNew) {
	mTabLayout.removeAllViews();
	PagerAdapter adapter = mViewPager.getAdapter();
	IconPagerAdapter iconAdapter = null;
	if (adapter instanceof IconPagerAdapter) {
	    iconAdapter = (IconPagerAdapter) adapter;
	}
	final int count = adapter.getCount();
	for (int i = 0; i < count; i++) {
	    CharSequence title = adapter.getPageTitle(i);
	    if (title == null) {
		title = EMPTY_TITLE;
	    }
	    int iconResId = 0;
	    int backgroundResId = 0;
	    int textSize = 0;
	    int textColor = 0;
	    if (iconAdapter != null) {
		iconResId = iconAdapter.getIconResId(i);
		backgroundResId = iconAdapter.getBackgroundResId(i);
		textSize = iconAdapter.getTextSize();
		textColor = iconAdapter.getTextColor();
	    }
	    addTab(i, title, iconResId, backgroundResId, textSize, textColor,
		    isNew[i]);
	}
	if (mSelectedTabIndex > count) {
	    mSelectedTabIndex = count - 1;
	}
	setCurrentItem(mSelectedTabIndex);
	requestLayout();
    }

    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
	setViewPager(view);
	setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int item) {
	if (mViewPager == null) {
	    throw new IllegalStateException("ViewPager has not been bound.");
	}
	mSelectedTabIndex = item;
	mViewPager.setCurrentItem(item);

	final int tabCount = mTabLayout.getChildCount();
	for (int i = 0; i < tabCount; i++) {
	    final View child = mTabLayout.getChildAt(i);
	    final boolean isSelected = (i == item);
	    child.setSelected(isSelected);
	    if (isSelected) {
		animateToTab(item);
	    }
	}
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
	mListener = listener;
    }

    public class TabView extends LinearLayout {
	private int mIndex;
	TextView text;

	public TabView(Context context, int index) {
	    super(context);
	    // super(context, null, R.attr.vpiTabPageIndicatorStyle);
	    setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
	    text = new TextView(context);
//	    if (index == 0) {
//		text.setGravity(Gravity.RIGHT);
//		text.setPadding(0, 0, PixelUtil.dp2Pixel(50, context), 0);
//	    } else {
//		text.setGravity(Gravity.LEFT);
//		text.setPadding(PixelUtil.dp2Pixel(50, context), 0, 0, 0);
//	    }
	    LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
		    LayoutParams.WRAP_CONTENT);
	    lp.gravity = Gravity.CENTER;
	    addView(text, lp);
	    text.setSingleLine();
	    text.setCompoundDrawablePadding((int) dp2Pixel(4, context));
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public void setTextSize(int size) {
	    text.setTextSize(size);
	}

	public void setText(CharSequence tx) {
	    text.setText(tx);
	}

	public void setTextSize(int type, int size) {
	    text.setTextSize(type, size);
	}

	public int getIndex() {
	    return mIndex;
	}

	public void setTextColor(int csl) {
	    text.setTextColor(csl);
	}

	public void setTextColor(ColorStateList csl) {
	    text.setTextColor(csl);
	}

	public void setCompoundDrawablesWithIntrinsicBounds(int left,
		int right, int top, int bottom) {
	    text.setCompoundDrawablesWithIntrinsicBounds(left, right, top,
		    right);
	}
    }

    public static float dp2Pixel(float dp, Context context) {
	float density = context.getResources().getDisplayMetrics().density;
	return dp * density;
    }

    public static int sp2px(float spValue, Context context) {
	float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
	return (int) (spValue * fontScale + 0.5f);
    }
}
