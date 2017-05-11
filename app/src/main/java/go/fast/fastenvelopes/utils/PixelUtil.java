package go.fast.fastenvelopes.utils;

import android.content.Context;

public class PixelUtil {

    public static float pixel2Dp(float pixel, Context context) {
	float density = 2;
	if (context != null) {

	    density = context.getResources().getDisplayMetrics().density;
	}
	return pixel / density;
    }

    public static int dp2Pixel(float dp, Context context) {
	float density = 2;
	if (context != null) {

	    density = context.getResources().getDisplayMetrics().density;
	}
	return (int) (dp * density);
    }

    public static int sp2px(float spValue, Context context) {
	float fontScale = 0;
	if (context != null) {
	    fontScale = context.getResources().getDisplayMetrics().scaledDensity;
	}
	return (int) (spValue * fontScale + 0.5f);
    }

    public static float getDensity(Context context) {
	return context.getResources().getDisplayMetrics().density;
    }
}
