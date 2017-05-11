/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package go.fast.fastenvelopes.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static go.fast.fastenvelopes.db.AttentionUserDB.level;

public class PreferenceUtils {

    /**
     * 保存Preference的name
     */
    public static final String PREFERENCE_NAME = "saveInfo";
    private static SharedPreferences mSharedPreferences;
    private static PreferenceUtils mPreferenceUtils;
    private static SharedPreferences.Editor editor;

    private String SHARED_KEY_SETTING_NOTIFICATION = "shared_key_setting_notification";
    private String SHARED_KEY_SETTING_SOUND = "shared_key_setting_sound";
    private String SHARED_KEY_SETTING_VIBRATE = "shared_key_setting_vibrate";
    private String SHARED_KEY_SETTING_SPEAKER = "shared_key_setting_speaker";

    /************ 设置用户信息 *************/
    private String SHARED_KEY_SETTING_USER_NICKNAME = "shared_key_setting_user_nickname";
    private String SHARED_KEY_SETTING_USER_ACCOUNT = "shared_key_setting_user_account";// 用户的唯一账号
    private String SHARED_KEY_SETTING_USER_ID = "shared_key_setting_user_id";// 用户绑定的机器码
    private String SHARED_KEY_SETTING_USER_PIC = "shared_key_setting_user_pic";
    private String SHARED_KEY_SETTING_USER_SEX = "shared_key_setting_user_sex";
    private String SHARED_KEY_SETTING_USER_AGE = "shared_key_setting_user_age";
    private String SHARED_KEY_SETTING_USER_AREA = "shared_key_setting_user_area";
    // -----start
    private String SHARED_KEY_SETTING_USER_ZHIYE = "shared_key_setting_user_zhiye";
    private String SHARED_KEY_SETTING_USER_QIANMING = "shared_key_setting_user_qianming";

    private String SHARED_KEY_SETTING_USER_LEVEL = "user_level";
    private String SHARED_KEY_SETTING_LAST_PLAYCOUNTS = "user_lastplaycounts";
    private String SHARED_KEY_ENFORCE_PSD = "shared_key_enforce_psd";

    private String SHARED_KEY_SETTING_STRANGE_MSG = "shared_key_setting_user_strangermsg";
    private String SHARED_KEY_SETTING_FRIEND_MSG = "shared_key_setting_user_friendmsg";

    // -----end
    private String SHARED_KEY_SETTING_USER_ZAINA = "shared_key_setting_user_zaina";

    private String SHARED_KEY_SHOW_LIVING_HELP = "shared_key_show_living_help";

    private String SHARED_KEY_ISWRITING_PRESS = "shared_key_iswriting_press";// 用户是否有未读消息

    private String UESR_SETTING_FANS = "shared_key_setting_user_fans";// 用户的粉丝数

    private String UESR_SETTING_ATTENTION = "shared_key_setting_user_attention";// 用户的关注数

    private String UESR_SETTING_ZAN = "shared_key_setting_user_zan";// 用户被点赞的字数

    private String SHARED_KEY_SETTING_USER_LOC = "shared_key_setting_user_loc";

    private String SHARED_KEY_LIVING_CHECK = "shared_key_living_check";// 用户是否开启了直播按钮

    private String SHARED_KEY_DANMU_CHECK = "shared_key_danmu_check";// 用户是否开启了弹幕按钮

    private String SHARED_KEY_UNREAD_CHECK = "shared_key_unread_check";// 用户是否有未读消息

    private String FIRST_SPLASH_PAGE = "first_splash_page";
    /**** ×*******设置加载用户性别 ****/
    private String SHARED_KEY_LOAD_SEX = "shared_key_load_sex";
    /**** ×*******设置按地区或者时间筛选 ****/
    private String SHARED_KEY_LOAD_TIME_LOC = "shared_key_load_time_loc";

    private String SHARED_KEY_LAST_LOGIN_TYPE = "shared_key_last_login_type";// 上次用户登录的类型（qq微信
									     // 或者手机号）

    private String SHARED_KEY_USER_LAST = "shared_key_user_phone";// 用户的手机号

    private String SHARED_KEY_USER_UID = "shared_key_user_uid";// 用户的uid

    private String SHARED_KEY_USER_PHONE = "shared_key_user_phone";// 用户的手机号

    private String SHARED_KEY_LAST_LOGIN_NICKNAME = "shared_key_login_nickname";// 用户最近一次登录的昵称

    private String SHARED_KEY_USER_CHECKIN_TIMES = "shared_key_user_checkin_times";// 用户的uid
    private String SHARED_KEY_USER_CHECKIN_TOTALGLODEN = "shared_key_user_checkin_totalgloden";// 用户签到获得的总金币数
    
    private String SHARED_KEY_USER_TOTALGLODEN = "shared_key_user_totalgloden";// 用户获得的总的金币数

    private PreferenceUtils(Context cxt) {
	// mSharedPreferences =
	// cxt.getSharedPreferences(DemoApplication.getInstance().getUser()+PREFERENCE_NAME,
	// Context.MODE_PRIVATE);
	mSharedPreferences = cxt.getSharedPreferences(PREFERENCE_NAME,
		Context.MODE_PRIVATE);
    }

    /**
     * 单例模式，获取instance实例
     * 
     * @param cxt
     * @return
     */
    public static PreferenceUtils getInstance(Context cxt) {
	if (mPreferenceUtils == null) {
	    mPreferenceUtils = new PreferenceUtils(cxt);
	}
	editor = mSharedPreferences.edit();
	return mPreferenceUtils;
    }

    public String getUUid(Context cxt) {
	DeviceUuidFactory uuid = new DeviceUuidFactory(cxt);
	String uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
	return uid;
    }

    public void setSettingMsgNotification(boolean paramBoolean) {
	editor.putBoolean(SHARED_KEY_SETTING_NOTIFICATION, paramBoolean);
	editor.commit();
    }

    public boolean getSettingMsgNotification() {
	return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_NOTIFICATION,
		true);
    }

    public void setSettingUserLevel(String level) {
	editor.putString(SHARED_KEY_SETTING_USER_LEVEL, level);
	editor.commit();
    }

    public String getSettingUserLevel() {
	return mSharedPreferences.getString(SHARED_KEY_SETTING_USER_LEVEL, "1");
    }
    public void setSettingUserLastPlayCounts(int  playCounts) {
        editor.putInt(SHARED_KEY_SETTING_LAST_PLAYCOUNTS, playCounts);
        editor.commit();
    }

    public int getSettingUserLastPlayCounts() {
        return mSharedPreferences.getInt(SHARED_KEY_SETTING_LAST_PLAYCOUNTS, 10);
    }
    public void setSettingMsgSound(boolean paramBoolean) {
	editor.putBoolean(SHARED_KEY_SETTING_SOUND, paramBoolean);
	editor.commit();
    }

    public boolean getFirstSplash() {
	return mSharedPreferences.getBoolean(FIRST_SPLASH_PAGE, false);
    }

    public void setFirstSplash(boolean paramBoolean) {
	editor.putBoolean(FIRST_SPLASH_PAGE, paramBoolean);
	editor.commit();
    }

    public boolean getSettingMsgSound() {

	return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_SOUND, true);
    }

    public void setSettingMsgVibrate(boolean paramBoolean) {
	editor.putBoolean(SHARED_KEY_SETTING_VIBRATE, paramBoolean);
	editor.commit();
    }

    public boolean getSettingMsgVibrate() {
	return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_VIBRATE, true);
    }

    public void setSettingMsgSpeaker(boolean paramBoolean) {
	editor.putBoolean(SHARED_KEY_SETTING_SPEAKER, paramBoolean);
	editor.commit();
    }

    public boolean getSettingMsgSpeaker() {
	return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_SPEAKER, true);
    }

    public void setSettingEnforcePsd(int paramBoolean) {

	editor.putBoolean(SHARED_KEY_ENFORCE_PSD, paramBoolean == 1 ? true
		: false);
	editor.commit();
    }

    public boolean getSettingEnforcePsd() {
	return mSharedPreferences.getBoolean(SHARED_KEY_ENFORCE_PSD, true);
    }

    public void setSettingShowLivingHelp(boolean isShown) {

	editor.putBoolean(SHARED_KEY_SHOW_LIVING_HELP, isShown);
	editor.commit();
    }

    public boolean getSettingShowLivingHelp() {
	return mSharedPreferences
		.getBoolean(SHARED_KEY_SHOW_LIVING_HELP, false);
    }

    public void setSettingUnreadMsg(boolean isUnread) {

	editor.putBoolean(SHARED_KEY_UNREAD_CHECK, isUnread);
	editor.commit();
    }

    public boolean getSettingUnreadMsg() {
	return mSharedPreferences.getBoolean(SHARED_KEY_UNREAD_CHECK, false);
    }

    /**
     * 设置用户昵称
     * 
     * @param UserNickName
     */
    public void setSettingUserNickName(String UserNickName) {
	editor.putString(SHARED_KEY_SETTING_USER_NICKNAME, UserNickName);
	editor.commit();
    }

    /**
     * 获取用户昵称
     */
    public String getSettingUserNickName() {
	return mSharedPreferences.getString(SHARED_KEY_SETTING_USER_NICKNAME,
		"");
    }

    /**
     * 设置用户头像
     * 
     * @param UserPic
     */
    public void setSettingUserPic(String UserPic) {
	editor.putString(SHARED_KEY_SETTING_USER_PIC, UserPic);
	editor.commit();
    }

    /**
     * 获取用户头像
     */
    public String getSettingUserPic() {
	return mSharedPreferences.getString(SHARED_KEY_SETTING_USER_PIC, "");
    }

    /**
     * 设置用户性别
     * 
     * @param UserSex
     */
    public void setSettingUserSex(String UserSex) {
	if (ToolUtils.isNullOrEmpty(UserSex)) {
	    UserSex = "1";
	}
	editor.putString(SHARED_KEY_SETTING_USER_SEX,
		Integer.valueOf(UserSex) == 1 ? "男" : "女");
	editor.commit();
    }

    /**
     * 获取用户性别
     */
    public String getSettingUserSex() {
	return mSharedPreferences.getString(SHARED_KEY_SETTING_USER_SEX, "男");
    }

    /**
     * 设置用户年龄
     * 
     * @param UserAge
     */
    public void setSettingUserAge(String UserAge) {
	editor.putString(SHARED_KEY_SETTING_USER_AGE, UserAge);
	editor.commit();
    }

    /**
     * 获取用户年龄
     */
    public String getSettingUserAge() {
	return mSharedPreferences.getString(SHARED_KEY_SETTING_USER_AGE, "21");
    }

    /**
     * 设置用户机器码token
     * 
     * @param UserAge
     */
    public void setSettingUserID(String UserID) {
	editor.putString(SHARED_KEY_SETTING_USER_ID, UserID);
	editor.commit();
    }

    /**
     * 获取用户机器码
     */
    public String getSettingUserID() {
	return mSharedPreferences.getString(SHARED_KEY_SETTING_USER_ID, "999");
    }

    /**
     * 设置用户账号
     * 
     * @param UserAge
     */
    public void setSettingUserAccount(String userAccount) {
	editor.putString(SHARED_KEY_SETTING_USER_ACCOUNT, userAccount);
	editor.commit();
    }

    /**
     * 获取用户账号
     */
    public String getSettingUserAccount() {
	return mSharedPreferences.getString(SHARED_KEY_SETTING_USER_ACCOUNT,
		"999");
    }

    /**
     * 设置用户的手机号
     * 
     * @param UserAge
     */
    public void setSettingUserPhone(String phone) {
	editor.putString(SHARED_KEY_USER_PHONE, phone);
	editor.commit();
    }

    /**
     * 获取用户账号
     */
    public String getSettingUserPhone() {
	return mSharedPreferences.getString(SHARED_KEY_USER_PHONE, "");
    }

    /**
     * 设置用户的Uid第三方返回的
     * 
     * @param UserAge
     */
    public void setSettingUserUid(String userUid) {

	editor.putString(SHARED_KEY_USER_UID, userUid);
	editor.commit();
    }

    /**
     * 获取用户的Uid第三方返回的
     */
    public String getSettingUserUid() {

	return mSharedPreferences.getString(SHARED_KEY_USER_UID, "");
    }

    /**
     * 设置用户区域
     * 
     * @param UserArea
     */
    public void setSettingUserArea(String UserArea) {
	editor.putString(SHARED_KEY_SETTING_USER_AREA, UserArea);
	editor.commit();
    }

    /**
     * 获取用户区域
     */
    public String getSettingUserArea() {
	return mSharedPreferences.getString(SHARED_KEY_SETTING_USER_AREA, "");
    }

    /**
     * 设置用户在哪动态
     * 
     * @param UserZaina
     */
    public void setSettingUserZaina(String UserZaina) {
	editor.putString(SHARED_KEY_SETTING_USER_ZAINA, UserZaina);
	editor.commit();
    }

    /**
     * 获取用户在哪动态
     */
    public String getSettingUserZaina() {
	return mSharedPreferences.getString(SHARED_KEY_SETTING_USER_ZAINA, "");
    }

    /**
     * 设置用户被点赞数
     * 
     * @param UserZaina
     */
    public void setSettingUserZan(int userZaned) {
	editor.putInt(UESR_SETTING_ZAN, userZaned);
	editor.commit();
    }

    /**
     * 获取用户被点赞数
     */
    public int getSettingUserZan() {
	return mSharedPreferences.getInt(UESR_SETTING_ZAN, 0);
    }

    /**
     * 设置用户被关注数
     * 
     * @param UserZaina
     */
    public void setSettingUserFans(int userFans) {
	editor.putInt(UESR_SETTING_FANS, userFans);
	editor.commit();
    }

    /**
     * 获取用户被关注数
     */
    public int getSettingUserFans() {
	return mSharedPreferences.getInt(UESR_SETTING_FANS, 0);
    }

    /**
     * 设置上次用户登录的类型
     * 
     * @param UserZaina
     */
    public void setSettingLastLoginType(int lastLoginType) {
	editor.putInt(SHARED_KEY_LAST_LOGIN_TYPE, lastLoginType);
	editor.commit();
    }

    /**
     * 获取上次用户登录的类型
     */
    public int getSettingLastLoginType() {
	return mSharedPreferences.getInt(SHARED_KEY_LAST_LOGIN_TYPE,
		CommonUtils.LOGIN_TYPE_NONE);
    }

    /**
     * 设置用户关注别人的数量
     * 
     * @param UserZaina
     */
    public void setSettingUserAttention(int userAttention) {
	editor.putInt(UESR_SETTING_ATTENTION, userAttention);
	editor.commit();
    }

    /**
     * 获取用户被点赞数
     */
    public int getSettingUserAttention() {
	return mSharedPreferences.getInt(UESR_SETTING_ATTENTION, 0);
    }

    /**
     * 设置用户经纬度
     * 
     * @param UserZaina
     */
    public void setSettingUserloc(String UserLoc) {
	editor.putString(SHARED_KEY_SETTING_USER_LOC, UserLoc);
	editor.commit();
    }

    /**
     * 获取用户经纬度
     */
    public String getSettingUserloc() {
	return mSharedPreferences.getString(SHARED_KEY_SETTING_USER_LOC, "");
    }

    /**
     * 设置筛选用户性别
     * 
     * @param UserZaina
     */
    public void setloadsex(String loadsex) {
	editor.putString(SHARED_KEY_LOAD_SEX, loadsex);
	editor.commit();
    }

    /**
     * 获取筛选用户性别
     */
    public String getloadsex() {
	return mSharedPreferences.getString(SHARED_KEY_LOAD_SEX, "");
    }

    /**
     * 设置按距离或者是时间筛选用户
     * 
     * @param UserZaina
     */
    public void setloadtimeloc(String loadtimeloc) {
	editor.putString(SHARED_KEY_LOAD_TIME_LOC, loadtimeloc);
	editor.commit();
    }

    /**
     * 获取按距离或者是时间筛选用户
     */
    public String getloadtimeloc() {
	return mSharedPreferences.getString(SHARED_KEY_LOAD_TIME_LOC, "");
    }

    /**
     * 设置用户职业
     * 
     * @param UserZaina
     */
    public void setSettingUserZhiye(String Zhiye) {
	editor.putString(SHARED_KEY_SETTING_USER_ZHIYE, Zhiye);
	editor.commit();
    }

    /**
     * 获取用户职业
     */
    public String getSettingUserZhiye() {
	return mSharedPreferences.getString(SHARED_KEY_SETTING_USER_ZHIYE, "");
    }

    public boolean getSettingDanmu() {
	return mSharedPreferences.getBoolean(SHARED_KEY_DANMU_CHECK, true);
    }

    public void setSettingDanmu(boolean paramBoolean) {
	editor.putBoolean(SHARED_KEY_DANMU_CHECK, paramBoolean);
	editor.commit();
    }

    public boolean getSettingPersonalLiving() {
	return mSharedPreferences.getBoolean(SHARED_KEY_LIVING_CHECK, true);
    }

    public void setSettingPersonalLiving(boolean paramBoolean) {
	editor.putBoolean(SHARED_KEY_LIVING_CHECK, paramBoolean);
	editor.commit();
    }

    /**
     * 设置筛选用户签名
     * 
     * @param UserZaina
     */
    public void setSettingUserQianming(String Qianming) {
	editor.putString(SHARED_KEY_SETTING_USER_QIANMING, Qianming);
	editor.commit();
    }

    /**
     * 获取筛选用户签名
     */
    public String getSettingUserQianming() {
	return mSharedPreferences.getString(SHARED_KEY_SETTING_USER_QIANMING,
		"");
    }

    /**
     * 设置接收陌生人消息的类型
     * 
     * @param UserZaina
     */
    public void setSettingUserstrangerMsg(String strangermsg) {
	editor.putString(SHARED_KEY_SETTING_STRANGE_MSG, strangermsg);
	editor.commit();
    }

    /**
     * 获取接收陌生人消息的类型
     */
    public String getSettingUserstrangerMsg() {
	return mSharedPreferences
		.getString(SHARED_KEY_SETTING_STRANGE_MSG, "v");
    }

    /**
     * 设置接收好友消息的类型
     * 
     * @param UserZaina
     */
    public void setSettingUserfriendMsg(String friendMsg) {
	editor.putString(SHARED_KEY_SETTING_FRIEND_MSG, friendMsg);
	editor.commit();
    }

    /**
     * 获取接收好友消息的类型
     */
    public String getSettingUserfriendMsg() {
	return mSharedPreferences.getString(SHARED_KEY_SETTING_FRIEND_MSG,
		"vtp");
    }

    public void setSettingCheckInTimes(int checkInTimes) {

	editor.putInt(SHARED_KEY_USER_CHECKIN_TIMES, checkInTimes);
	editor.commit();
    }

    public int getSettingCheckInTimes() {
	return mSharedPreferences.getInt(SHARED_KEY_USER_CHECKIN_TIMES, 0);
    }

    public void setSettingCheckInTotalSize(int totalCheckGlodens) {

	editor.putInt(SHARED_KEY_USER_CHECKIN_TOTALGLODEN, totalCheckGlodens);
	editor.commit();
    }

    public int getSettingCheckInTotalSize() {
	return mSharedPreferences
		.getInt(SHARED_KEY_USER_CHECKIN_TOTALGLODEN, 0);
    }

    public void setSettingTotalGoldenSize(int totalGlodens) {

	editor.putInt(SHARED_KEY_USER_TOTALGLODEN, totalGlodens);
	editor.commit();
    }

    public int getSettingTotalGoldenSize() {
	return mSharedPreferences
		.getInt(SHARED_KEY_USER_TOTALGLODEN, 0);
    }
    
    
    /*
     * private String = "shared_key_setting_user_pic"; private String =
     * "shared_key_setting_user_sex"; private String =
     * "shared_key_setting_user_age"; private String =
     * "shared_key_setting_user_area"; private String =
     * "shared_key_setting_user_zaina";
     */
}
