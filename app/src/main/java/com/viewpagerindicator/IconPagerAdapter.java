package com.viewpagerindicator;

public interface IconPagerAdapter {
    /**
     * Get icon representing the page at {@code index} in the adapter.
     */
    int getIconResId(int index);
    int getBackgroundResId(int index);
    public int getTextSize();
	public int getTextColor();
	
    // From PagerAdapter
    int getCount();
}
