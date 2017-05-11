package go.fast.fastenvelopes.utils;

import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.widget.TextView;

import java.util.ArrayList;

public class TextUtils {

    public static int getFontHeight(float fontSize) {
	Paint paint = new Paint();
	paint.setTextSize(fontSize);
	FontMetrics fm = paint.getFontMetrics();
	return (int) Math.ceil(fm.descent - fm.ascent);
    }

    public static int getFontHeight(Paint paint) {
	FontMetrics fm = paint.getFontMetrics();
	return (int) Math.ceil(fm.descent - fm.ascent);
    }

    public static void setTextLineSpace(TextView textView, float fFontWidth,
	    float fLineHeight) {
	Paint paint = new Paint();
	paint.setTextSize(fFontWidth);
	float fMulValue;
	float fAddValue;
	FontMetrics fm = paint.getFontMetrics();

	float fFontHeight = (float) Math.ceil(fm.descent - fm.ascent);
	if (fFontHeight > fLineHeight) {
	    fMulValue = fLineHeight / fFontHeight;
	    fAddValue = -1;
	} else {
	    fMulValue = 1;
	    fAddValue = fLineHeight - fFontHeight;
	}
	textView.setTextSize(fFontWidth);
	textView.setLineSpacing(fAddValue, fMulValue);
    }





    public static ArrayList<String> getListByString(String data) {

	if (ToolUtils.isNullOrEmpty(data)) {
	    return null;
	}

	String[] arr = data.split(",");

	if (arr.length > 0) {
	    ArrayList<String> result = new ArrayList<>();
	    for (int i = 0; i < arr.length; i++) {

	    }
	    return result;
	}

	return null;
    }


}
