package go.fast.fastenvelopes.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import go.fast.fastenvelopes.info.UserInfo;

public class DataUtils {




    
    
    public static boolean isHaveLogin(Context context)
    {
	
	if(PreferenceUtils.getInstance(context).getSettingUserAccount().equals("999"))
	{
	    return false;
	}
	return true;
    }

    public static void setUserToLocal(Context context, UserInfo user, int type) {
	PreferenceUtils.getInstance(context).setSettingUserPic(user.headurl);
	PreferenceUtils.getInstance(context).setSettingUserNickName(
		user.nickName);
	PreferenceUtils.getInstance(context).setSettingUserSex(
		String.valueOf(user.sex));
	PreferenceUtils.getInstance(context).setSettingUserAge(
		String.valueOf(user.age));
	PreferenceUtils.getInstance(context).setSettingUserQianming(
		user.signature);

	PreferenceUtils.getInstance(context).setSettingUserLevel(user.level);

	PreferenceUtils.getInstance(context)
		.setSettingUserZan(user.zanMeCounts);
	PreferenceUtils.getInstance(context).setSettingUserFans(
		user.attentionMeCounts);
	PreferenceUtils.getInstance(context).setSettingUserAttention(
		user.attentionOthersCounts);

	PreferenceUtils.getInstance(context)
		.setSettingUserAccount(user.account);

	PreferenceUtils.getInstance(context).setSettingEnforcePsd(
		user.isEnforce);

	PreferenceUtils.getInstance(context).setSettingLastLoginType(type);

	PreferenceUtils.getInstance(context).setSettingUserUid(user.uid);

	PreferenceUtils.getInstance(context).setSettingUserPhone(user.phone);
	;

	PreferenceUtils.getInstance(context).setSettingEnforcePsd(
		user.isEnforce);
    }


	public static boolean isMe(Context context ,String account)
	{

		if(ToolUtils.isNullOrEmpty(account))
		{
			return false;
		}
		else
		{
			if(PreferenceUtils.getInstance(context).getSettingUserAccount().equals(account))
			{
				return true;
			}
		}


		return false;

	}
    
    
  //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }
     
    //版本号
    public static int getVersionCode(Context context) {
		if(context!=null)
		{
			return getPackageInfo(context).versionCode;
		}
     return 16;
    }
     
    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;
     
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
     
            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
     
        return pi;
    }

}
