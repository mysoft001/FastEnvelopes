package go.fast.fastenvelopes.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 
 * @author */
public class PreferenceUtil {
	static PreferenceUtil preferenceUtil;
	private static String preferenceName = ".wholewriter_preferences";
	static SharedPreferences sharePreference;

	public static PreferenceUtil getInstance(Context context) {
		if (preferenceUtil == null) {
			preferenceUtil = new PreferenceUtil(context);

		}
		return preferenceUtil;
	}

	public PreferenceUtil(Context context) {
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

	public float getFloat(String key, float defaultValue) {
		return sharePreference.getFloat(key, defaultValue);
	}
	public void putFloat(String key, float value) {
		Editor editor = sharePreference.edit();
		editor.putFloat(key, value);
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

	// ----------------------------------------------------------------------------------------------------------------------

	public boolean isNeedMoney(Context context) {

		if (!sharePreference.getBoolean("stopPush", false)) {
			return true;
		}
		return false;
	}
	public boolean isOtherCity()
	{
			return sharePreference.getBoolean("cityOther", false);
	}
	public void setOtherCity(boolean value)
	{
			 putBoolean("cityOther",  value);
	}
}
