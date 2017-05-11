package go.fast.fastenvelopes.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 
 * @author hanwei 存储工具�? */
public class AutoPreferenceUtils {
	static AutoPreferenceUtils preferenceUtil;
	private static String preferenceName = "com.wholewriter.auto_preferences";
	static SharedPreferences sharePreference;

	public static AutoPreferenceUtils getInstance(Context context) {
		if (preferenceUtil == null) {
			preferenceUtil = new AutoPreferenceUtils(context);

		}
		return preferenceUtil;
	}

	public AutoPreferenceUtils(Context context) {
		sharePreference = context.getSharedPreferences(preferenceName, 0);
	}

	public void putBoolean(String key, boolean value) {
		Editor editor = sharePreference.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public void putInt(String key, int value) {
		Editor editor = sharePreference.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		return sharePreference.getBoolean(key, defaultValue);
	}

	public int getInt(String key, int defaultValue) {
		return sharePreference.getInt(key, defaultValue);
	}
	public long getLong(String key, long defaultValue) {
		return sharePreference.getLong(key, defaultValue);
	}
	public void putLong(String key, long value) {
		Editor editor = sharePreference.edit();
		editor.putLong(key, value);
		editor.commit();
	}
	public void putString(String key, String value) {
		Editor editor = sharePreference.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public String getString(String key, String defaultValue) {
		return sharePreference.getString(key, defaultValue);
	}

	
}
