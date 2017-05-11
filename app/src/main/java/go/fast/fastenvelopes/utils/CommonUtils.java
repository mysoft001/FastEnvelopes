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

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CommonUtils {
    
    
    
    public static final int AUTO_KEEP_COMMENT=15; //在用户直播的时候最自动几次评论
    public static final int AUTO_KEEP_COUNT=6; //最自动几次收藏
    public static final int AUTO_GREAT_COUNT=5; //最多几次调用
    public static final int SENDCOMMENT_ARTICLE = 0;// 对作品本身评论

    public static final int SENDCOMMENT_REPLAY = 1;// 点击评论item里面的回复按钮进行的评论

    public static final int SENDCOMMENT_COMMENT = 2;// 回复评论的评论
    
    public static final int MAX_CHAPTER_LENGHT = 5000;// 章节最大字数
    
    
    public static final int ARTICLE_TYPE_PERSONAL = 2;//作品类型 个人作品
    
    public static final int ARTICLE_TYPE_SHARED = 1;//作品类型 共享
    
    
public static final int RANKING_TYPE_TOATL = 1;//排行榜类型  总榜s
    
public static final int RANKING_TYPE_MONTH = 2;//排行榜类型  月榜

public static final int RANKING_TYPE_WEEK = 3;//排行榜类型  周榜
    
public static final int DRAFT_CHAPTER_TAG = -1;//草稿章节发布时传个服务器的标识



public static final int LOGIN_TYPE_WEIXIN = 1;//通过微信快捷登录的

public static final int LOGIN_TYPE_QQ = 2;//通过QQ快捷登录的

public static final int LOGIN_TYPE_PHONE = 3;//通过手机号和密码登录的

public static final int LOGIN_TYPE_NONE = 0;//没有登录过

//没有网络连接 
public static final int NETWORN_NONE = 0; 
//wifi连接 
//手机网络数据连接类型 
public static final int NETWORN_MOBILE = 1; 
public static final int NETWORN_2G = 2; 
public static final int NETWORN_3G = 3; 
public static final int NETWORN_4G = 4; 
public static final int NETWORN_WIFI = 5; 

public static final int DOWNLOAD_SUC = 1; //网络图片下载完毕

public static final int DOWNLOAD_ERROR = 2; 


public static final int WRITING_STATE_ISWRITING = 1; //有人正在写

public static final int WRITING_STATE_NOWRITING = 0; //没人写
public static final int WRITING_STATE_CHECK =2; //正在审核


public static final String NOTIFY_TYPE_PREFERENCE ="notify_type_pre"; //通知提示类型的key

public static final int NOTIFY_TYPE_VOICE =1; //只有声音
public static final int NOTIFY_TYPE_VIBRET =2; //只有震动
public static final int NOTIFY_TYPE_ALL =3; //声音和震动
public static final int NOTIFY_TYPE_NONE =4; //都没有



public static final int REWARD_USER_SHARE =1; //分享奖励
public static final int REWARD_USER_SIGN =2; //签到奖励
public static final int REWARD_USER_PLAYINGREWARD =3; //打赏奖励
public static final int REWARD_USER_TUIJIAN_FRIEND =4; //推荐好友奖励

public static final int REWARD_USER_LIANXU_UPDATE =5; //连续更新的奖励
public static final int REWARD_USER_WRITING_MORE =6; //更新字数奖励


	public static final int ENVELOPE_TYPE_GUESS =1; //红包玩法类型 猜金额红包
	public static final int ENVELOPE_TYPE_FREE =2; //红包玩法类型 自由猜
	public static final int ENVELOPE_TYPE_NEAR =3; //红包玩法类型 近者得

	public static final int ROOM_TYPE_OUT =3; //关注者离开房间
	public static final int ROOM_TYPE_EXIT=2; //离开房间
	public static final int ROOM_TYPE_IN =1; //进入房间


	//（0 普通对话，1 猜红包对话，2 抢红包结果对话 3 发起红包 ）
	public static final int MESSAGE_TYPE_MSG_CONTENT=0; //发送基本的消息
	public static final int MESSAGE_TYPE_JOIN_ENVELOPE=4; //有人加入红包
	public static final int MESSAGE_TYPE_MSG_GUESSING_ENVELOPE =1; //正在抢红包
	public static final int MESSAGE_TYPE_MSG_RESULT_ENVELOPE =2; //红包结果
	public static final int MESSAGE_TYPE_MSG_START_ENVELOPE =3; //发起红包

	public static final int ATTENTION_ROOM =1; //关注房间
	public static final int CANCEL_ATTENTION_ROOM =0; //取消关注房间

	public static final int ENVELOPE_PERMISSION_ALL =2; //红包权限 所有人可以抢
	public static final int ENVELOPE_PERMISSION_JOIN =1; //红包权限 参与人可以抢

	public static final int PUSH_MSG_TYPE_NORMALMSG =1; //普通消息推送
	public static final int PUSH_MSG_TYPE_START_ENVELOPE =2; //发起红包的推送
	public static final int PUSH_MSG_TYPE_JOIN_ENVELOPE=3; //加入红包的推送消息
	public static final int PUSH_MSG_TYPE_GO_ENVELOPE=4; //开始红包的推送消息
	public static final int PUSH_MSG_TYPE_GUESSING_ENVELOPE=5; //正在猜的推送消息
	//public static final int PUSH_MSG_TYPE__RESULT_ENVELOPE=6; //结束了猜红包的推送消息
	public static final int PUSH_MSG_TYPE_CANCEL_ENVELOPE=6; //正在猜红包被取消

	public static final int PUSH_MSG_TYPE_USER_ATTENTION = 7;// 有用户关注的通知tag

	public static final int PUSH_MSG_TYPE_LETTER = 8;// 收到私信


	public static final int ENVELOPE_STATUS_START =1; //发起红包
	public static final int ENVELOPE_STATUS_GUESSING =2; //正在猜红包
	public static final int ENVELOPE_STATUS_RESULT =3; //结束红包

/** 
* 获取当前网络连接类型 
* @param context 
* @return 
*/
public static int getNetworkState(Context context) { 
//获取系统的网络服务 
ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
//如果当前没有网络 
if (null == connManager) 
return NETWORN_NONE; 
//获取当前网络类型，如果为空，返回无网络 
NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo(); 
if (activeNetInfo == null || !activeNetInfo.isAvailable()) { 
return NETWORN_NONE; 
} 
//判断是不是连接的是不是wifi 
NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); 
if (null != wifiInfo) { 
NetworkInfo.State state = wifiInfo.getState(); 
if (null != state) 
if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) { 
return NETWORN_WIFI; 
} 
} 
//如果不是wifi，则判断当前连接的是运营商的哪种网络2g、3g、4g等 
NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); 
if (null != networkInfo) { 
NetworkInfo.State state = networkInfo.getState(); 
String strSubTypeName = networkInfo.getSubtypeName(); 
if (null != state) 
if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) { 
switch (activeNetInfo.getSubtype()) { 
//如果是2g类型 
case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g 
case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g 
case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g 
case TelephonyManager.NETWORK_TYPE_1xRTT: 
case TelephonyManager.NETWORK_TYPE_IDEN: 
return NETWORN_2G; 
//如果是3g类型 
case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g 
case TelephonyManager.NETWORK_TYPE_UMTS: 
case TelephonyManager.NETWORK_TYPE_EVDO_0: 
case TelephonyManager.NETWORK_TYPE_HSDPA: 
case TelephonyManager.NETWORK_TYPE_HSUPA: 
case TelephonyManager.NETWORK_TYPE_HSPA: 
case TelephonyManager.NETWORK_TYPE_EVDO_B: 
case TelephonyManager.NETWORK_TYPE_EHRPD: 
case TelephonyManager.NETWORK_TYPE_HSPAP: 
return NETWORN_3G; 
//如果是4g类型 
case TelephonyManager.NETWORK_TYPE_LTE: 
return NETWORN_4G; 
default: 
//中国移动 联通 电信 三种3G制式 
if (strSubTypeName.equalsIgnoreCase("TD-SCDMA") || strSubTypeName.equalsIgnoreCase("WCDMA") || strSubTypeName.equalsIgnoreCase("CDMA2000")) { 
return NETWORN_3G; 
} else { 
return NETWORN_MOBILE; 
} 
} 
} 
} 
return NETWORN_NONE; 
} 



	/**
	 * 检测网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetWorkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}

		return false;
	}
	

	/**
	 * 检测Sdcard是否存在
	 * 
	 * @return
	 */
	public static boolean isExitsSdcard() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}
	
	public static String getBaseFilePath()
	{
	   String path= Environment.getExternalStorageDirectory()+"/wholewriter/";
	   System.out.println("getBaseFilePath-------------"+path);
	    return path;
	}

	public static String getTopActivity(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

		if (runningTaskInfos != null)
			return runningTaskInfos.get(0).topActivity.getClassName();
		else
			return "";
	}
	
	/**
	 * 判断对象或对象数组中每一个对象是否为空: 对象为null，字符序列长度为0，集合类、Map为empty
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNullOrEmpty(Object obj) {
		
		if (obj != null){
		}else{
			return true;
		}
		
		if (obj.toString() != null){//判断对象（不知道对不对）
		}else{
			return true;
		}
		
		/*if (!isNullOrEmpty(obj.toString())){//判断对象（不知道对不对）0904|好像有错，以后在好好看一下
			Log.d("isNullOrEmpty", "=》》》》》》not null");
		}else{
			return true;
		}*/
		
		
		if (obj instanceof CharSequence){
			return ((CharSequence) obj).length() == 0;
		}
		if (obj instanceof Collection){
			return ((Collection) obj).isEmpty();
		}
			 
		if (obj instanceof Map){
			return ((Map) obj).isEmpty(); 
		}

		if (obj instanceof Object[]) {
			Object[] object = (Object[]) obj;
			if (object.length == 0) {
				return true;
			}
			boolean empty = true;
			for (int i = 0; i < object.length; i++) {
				if (!isNullOrEmpty(object[i])) {
					empty = false;
					break;
				}
			}
			return empty;
		}
		
		if (obj instanceof JSONArray) {
			if (((JSONArray)obj).length() == 0) {
				return true;
			}
		}
		
		if (obj instanceof JSONObject) {
			if (((JSONObject)obj).length() == 0) {
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * 计算地球上任意两点(经纬度)距离
	 * 
	 * @param long1
	 *            第一点经度
	 * @param lat1
	 *            第一点纬度
	 * @param long2
	 *            第二点经度
	 * @param lat2
	 *            第二点纬度
	 * @return 返回距离 单位：米
	 */
	public static double Distance(double long1, double lat1, double long2, double lat2) {
		double a, b, R;
		R = 6378137; // 地球半径
		lat1 = lat1 * Math.PI / 180.0;
		lat2 = lat2 * Math.PI / 180.0;
		a = lat1 - lat2;
		b = (long1 - long2) * Math.PI / 180.0;
		double d;
		double sa2, sb2;
		sa2 = Math.sin(a / 2.0);
		sb2 = Math.sin(b / 2.0);
		d = 2
				* R
				* Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1)
						* Math.cos(lat2) * sb2 * sb2));
		return d;
	}

	 /**
     * 过滤emoji 或者 其他非文字类型的字符
     * @param source
     * @return
     */
    public static String filterEmoji(String source) {

        if (!containsEmoji(source)) {
            return source;//如果不包含，直接返回
        }
        //到这里铁定包含
        StringBuilder buf = null;

        int len = source.length();

        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);

            if (!isEmojiCharacter(codePoint)) {
                if (buf == null) {
                    buf = new StringBuilder(source.length());
                }

                buf.append(codePoint);
            } else {
            }
        }

        if (buf == null) {
            return "";
        } else {
            if (buf.length() == len) {//这里的意义在于尽可能少的toString，因为会重新生成字符串
                buf = null;
                return source;
            } else {
                return buf.toString();
            }
        }

    }
    
    private static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
    }
    
    /**
     * 检测是否有emoji字符
     * @param source
     * @return 一旦含有就抛出
     */
    public static boolean containsEmoji(String source) {
       // if (StringUtils.isBlank(source)) { //需要引用commons-lang-2.5.jar
       //     return false; 
       // }

        int len = source.length();

        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);

            if (isEmojiCharacter(codePoint)) {
                //do nothing，判断到了这里表明，确认有表情字符
                return true;
            }
        }

        return false;
    }


}
