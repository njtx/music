package com.example.demo_musicplay;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesSettings{
	
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mEditor;
	public static String PREFERENCES_FLAG_TABS_MODE = "using_tabs_flag";
	
	public SharedPreferencesSettings(Context context)
	{
		mSharedPreferences = context.getSharedPreferences(PREFERENCES_FLAG_TABS_MODE,
				Context.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
	}
	
	public String getPreferenceValue(String preferenceKey, String defValue)
	{
		return mSharedPreferences.getString(preferenceKey, defValue);
	}

	public int getPreferenceValue(String preferenceKey, int defValue)
	{
		return mSharedPreferences.getInt(preferenceKey, defValue);
	}

	public float getPreferenceValue(String preferenceKey, float defValue)
	{
		return mSharedPreferences.getFloat(preferenceKey, defValue);
	}

	public long getPreferenceValue(String preferenceKey, long defValue)
	{
		return mSharedPreferences.getLong(preferenceKey, defValue);
	}

	public boolean getPreferenceValue(String preferenceKey, boolean defValue)
	{
		return mSharedPreferences.getBoolean(preferenceKey, defValue);
	}

	public boolean setPreferenceValue(String preferenceKey, String value)
	{
		return mEditor.putString(preferenceKey, value).commit();
	}

	public boolean setPreferenceValue(String preferenceKey, int value)
	{
		return mEditor.putInt(preferenceKey, value).commit();
	}

	public boolean setPreferenceValue(String preferenceKey, boolean value)
	{
		return mEditor.putBoolean(preferenceKey, value).commit();
	}

	public boolean setPreferenceValue(String preferenceKey, float value)
	{
		return mEditor.putFloat(preferenceKey, value).commit();
	}

	public boolean setPreferenceValue(String preferenceKey, long value)
	{
		return mEditor.putLong(preferenceKey, value).commit();
	}
}
