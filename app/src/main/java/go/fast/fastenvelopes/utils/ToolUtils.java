package go.fast.fastenvelopes.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import dialogplus.DialogPlus;
import dialogplus.DialogPlusBuilder;
import dialogplus.ViewHolder;
import go.fast.fastenvelopes.R;




public class ToolUtils {
    private static Toast toast;

    public static int getRandom(int min, int max) {
	return (int) (Math.random() * (max - min) + min);
    }

    public static long getDayByMillsecond(int year, int month, int day) {
	Calendar c = Calendar.getInstance();
	try {
	    c.setTime(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
		    .parse(year + "-" + month + "-" + day));
	} catch (Exception e) {
	}
	return c.getTimeInMillis();
    }

    public static String getDetailTimeByMillsecond(long time) {
	Calendar nowC = Calendar.getInstance();
	Calendar c = Calendar.getInstance();
	c.setTimeInMillis(time);
	SimpleDateFormat dateFormater;
	if (nowC.get(Calendar.YEAR) > c.get(Calendar.YEAR)) {

	    dateFormater = new SimpleDateFormat("yyyy.MM.dd HH:mm");

	} else {
	    dateFormater = new SimpleDateFormat("MM.dd HH:mm");
	}
	return dateFormater.format(new Date(time));
    }

    public static long delayDay(int day) {
	return 60 * 60 * 24 * day * 1000;
    }

    public static boolean basicAdSwitcher() {
	if (System.currentTimeMillis() - getDayByMillsecond(2015, 10, 27) > delayDay(1)) {
	    return true;
	}
	return false;
    }

    public static long getShortTimeStamp() {// 精确到秒
	long timestamp = (System.currentTimeMillis() - getDayByMillsecond(2016,
		10, 27)) / 1000;

	if (timestamp < Integer.MAX_VALUE) {
	    return timestamp;
	}
	timestamp = Integer.MAX_VALUE - getRandom(0, 10000);
	return timestamp;
    }

    public static void goodApp(final Context context) {
	Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
	Intent it = new Intent(Intent.ACTION_VIEW, uri);
	context.startActivity(it);
    }

    public static void share(Context mContext, String subject, String body) {
	Intent intent = new Intent(Intent.ACTION_SEND);
	intent.setType("text/plain");
	intent.putExtra(Intent.EXTRA_SUBJECT, subject);
	intent.putExtra(Intent.EXTRA_TEXT, body);
	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	mContext.startActivity(Intent.createChooser(intent, "分享给好友"));
    }

    public static boolean isNullOrEmpty(String data) {
	if (data == null || data.length() == 0 || data.equals("")) {
	    return true;
	}
	return false;

    }

    public static <T> ArrayList<T> string2List(T[] data) {
	if (data != null && data.length > 0) {
	    List<T> result = new ArrayList<T>();
	    for (int i = 0; i < data.length; i++) {
		result.add(data[i]);
	    }

	    return (ArrayList<T>) result;
	}

	return null;
    }

    public static void showToast(Context context, String hint) {
	if (toast == null) {
	    toast = Toast.makeText(context, hint, Toast.LENGTH_LONG);
	} else {
	    toast.setText(hint);
	    toast.setDuration(Toast.LENGTH_LONG);
	}

	toast.show();
    }

    public static void showToastByStr(final Activity activity, final String msg) {
	activity.runOnUiThread(new Runnable() {

	    @Override
	    public void run() {
		Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
	    }
	});
    }

    public static void showToasta(Context context, final String msg) {

	Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    static Timer timer;
    static TimerTask timerTask;
    static int second;
    static int minute;
    static Handler handler;

    public static void setTimer(int timeSecond, final OnTimerListener listener) {
	minute = timeSecond / 60;
	second = timeSecond % 60;
	if (timer == null) {
	    timer = new Timer();
	}
	if (timerTask == null) {
	    timerTask = new TimerTask() {

		@Override
		public void run() {
		    Message msg = new Message();
		    msg.what = 1;
		    handler.sendMessage(msg);

		}
	    };
	}

	handler = new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
		super.handleMessage(msg);

		if (minute == 0) {
		    if (second == 0) {
			listener.endTime();

			if (timer != null) {
			    timer.cancel();
			    timer = null;
			}
			if (timerTask != null) {
			    timerTask = null;
			}
		    } else {
			second--;
			if (second >= 10) {

			    listener.setCurrentTime("0" + minute + ":" + second);
			} else {
			    listener.setCurrentTime("0" + minute + ":0"
				    + second);
			}
		    }
		} else {
		    if (second == 0) {
			second = 59;
			minute--;
			if (minute >= 10) {
			    listener.setCurrentTime(minute + ":" + second);
			} else {
			    listener.setCurrentTime("0" + minute + ":" + second);
			}
		    } else {
			second--;
			if (second >= 10) {
			    if (minute >= 10) {
				listener.setCurrentTime(minute + ":" + second);
			    } else {
				listener.setCurrentTime("0" + minute + ":"
					+ second);
			    }
			} else {
			    if (minute >= 10) {
				listener.setCurrentTime(minute + ":0" + second);
			    } else {
				listener.setCurrentTime("0" + minute + ":0"
					+ second);
			    }
			}
		    }
		}
	    }

	};
	// timerTask.cancel();
	// timer.cancel();//防止之前调用过该方法
	timer.schedule(timerTask, 0, 1000);

    }

    public static void stopTimer() {
	if (timer != null) {
	    timer.cancel();
	    timer = null;
	}
	if (timerTask != null) {
	    timerTask = null;
	}
    }

    public static String GetNetworkType(Context context) {
	String strNetworkType = "";

	NetworkInfo networkInfo = ((ConnectivityManager) context
		.getSystemService(Context.CONNECTIVITY_SERVICE))
		.getActiveNetworkInfo();
	if (networkInfo != null && networkInfo.isConnected()) {
	    if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
		strNetworkType = "WIFI";
	    } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
		String _strSubTypeName = networkInfo.getSubtypeName();

		Log.e("cocos2d-x", "Network getSubtypeName : "
			+ _strSubTypeName);

		// TD-SCDMA networkType is 17
		int networkType = networkInfo.getSubtype();
		switch (networkType) {
		case TelephonyManager.NETWORK_TYPE_GPRS:
		case TelephonyManager.NETWORK_TYPE_EDGE:
		case TelephonyManager.NETWORK_TYPE_CDMA:
		case TelephonyManager.NETWORK_TYPE_1xRTT:
		case TelephonyManager.NETWORK_TYPE_IDEN: // api<8 : replace by
							 // 11
		    strNetworkType = "2G";
		    break;
		case TelephonyManager.NETWORK_TYPE_UMTS:
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
		case TelephonyManager.NETWORK_TYPE_HSDPA:
		case TelephonyManager.NETWORK_TYPE_HSUPA:
		case TelephonyManager.NETWORK_TYPE_HSPA:
		case TelephonyManager.NETWORK_TYPE_EVDO_B: // api<9 : replace by
							   // 14
		case TelephonyManager.NETWORK_TYPE_EHRPD: // api<11 : replace by
							  // 12
		case TelephonyManager.NETWORK_TYPE_HSPAP: // api<13 : replace by
							  // 15
		    strNetworkType = "3G";
		    break;
		case TelephonyManager.NETWORK_TYPE_LTE: // api<11 : replace by
							// 13
		    strNetworkType = "4G";
		    break;
		default:
		    // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
		    if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA")
			    || _strSubTypeName.equalsIgnoreCase("WCDMA")
			    || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
			strNetworkType = "3G";
		    } else {
			strNetworkType = _strSubTypeName;
		    }

		    break;
		}

		Log.e("cocos2d-x",
			"Network getSubtype : "
				+ Integer.valueOf(networkType).toString());
	    }
	}

	Log.e("cocos2d-x", "Network Type : " + strNetworkType);

	return strNetworkType;
    }

    public static String getDateByTime(long time) {
	SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
	return dataFormat.format(new Date(time));
    }

    static Timer timingTimer;

    /**
     * 设置倒计时触发事件
     * 
     * 注意:回调不是线程安全的 不可以更新UI
     * 
     * @param second
     * 
     *            倒计时秒数
     */
    public static void startTiming(int second,
	    final OnTimingListener timingListener) {

	timingTimer = new Timer();
	timingTimer.schedule(new TimerTask() {

	    @Override
	    public void run() {
		timingListener.endTime();
	    }
	}, 0, second * 1000);

    }

    /**
     * 停止定时器
     */
    public static void stopTiming() {
	if (timingTimer != null) {
	    timingTimer.cancel();
	}
    }

    public interface OnTimerListener {
	public void setCurrentTime(String currentTime);

	public void endTime();
    }

    public interface OnTimingListener {
	public void endTime();
    }


    /**
     * 转换设定的时间距当前时间的显示规格
     * 
     * @param shijianchuo
     * @return
     */

    @SuppressLint("SimpleDateFormat")
    public static String showTimeDuration(long shijianchuo) {
	long unixLong = 0;
	StringBuffer sb = null;
	try {
	    unixLong = shijianchuo;
	} catch (Exception ex) {
	    System.out.println("String转换Long错误，请确认数据可以转换！");
	}

	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date now;
	try {
	    now = df.parse(df.format(new Date()));
	    // java.util.Date date=df.parse("2004-01-02 11:30:24");
	    Date date = df.parse(df.format(unixLong));// 获取参数时间
	    long l = now.getTime() - date.getTime();
	    long day = l / (24 * 60 * 60 * 1000);
	    long hour = (l / (60 * 60 * 1000) - day * 24);
	    long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
	    long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

	    sb = new StringBuffer();
	    // sb.append("发表于：");
	    if (day > 0) {
		sb.append(day + "天前");
	    } else if (hour > 0) {
		sb.append(hour + "小时前");
	    } else if (min > 0) {
		sb.append(min + "分钟前");
	    } else {
		sb.append("刚刚");
	    }
	    // sb.append(s+"秒 前");
	    // sb.append("前");
	    // System.out.println(sb.toString());
	} catch (ParseException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return sb.toString();

    }

    /**
     * 根据毫秒数获取分秒
     * 
     * @param millseconds
     * @return
     */

    public static int[] getTimeByMS(long millseconds) {
	int[] time = new int[2];
	time[0] = (int) ((millseconds / 1000) / 60);
	time[1] = (int) ((millseconds / 1000) % 60);

	return time;
    }

    /**
     * 是否超过了时间间隔
     * 
     * @param millseconds
     * 
     *            前一个操作的时间
     * 
     * @param gap
     * 
     *            时间间隔(分钟)
     * 
     * @return
     * 
     * 
     * 
     */

    public static boolean isTimeOut(long millseconds, int gap) {
	if ((System.currentTimeMillis() - millseconds > gap * 1000 * 60)) {
	    return true;
	}

	return false;
    }

    public static int[] getScreenWH(Context context) {
	int[] screenWH = new int[2];
	Resources resources = context.getResources();
	DisplayMetrics dm = resources.getDisplayMetrics();
	float density1 = dm.density;
	int width = dm.widthPixels;
	int height = dm.heightPixels;
	screenWH[0] = width;
	screenWH[1] = height;
	return screenWH;
    }

    /**
     * 是否是夜间
     * 
     * @return
     */
    public static boolean isNight() {

	long time = System.currentTimeMillis();
	final Calendar mCalendar = Calendar.getInstance();
	mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
	mCalendar.setTimeInMillis(time);

	int mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
	if (mHour < 7 || mHour > 18) {
	    return true;
	}

	return false;
    }

    /**
     * 得到自定义的progressDialog
     * 
     * @param context
     * @param msg
     * @return
     */
    public static Dialog createLoadingDialog(Context context, String msg) {

	LayoutInflater inflater = LayoutInflater.from(context);
	View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
	LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
	// main.xml中的ImageView
	ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
	TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
	// 加载动画
	Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
		context, R.anim.loading_animation);
	// 使用ImageView显示动画
	spaceshipImage.startAnimation(hyperspaceJumpAnimation);
	tipTextView.setText(msg);// 设置加载信息

	Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

	loadingDialog.setCancelable(false);// 不可以用“返回键”取消
	loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
		LinearLayout.LayoutParams.FILL_PARENT,
		LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
	return loadingDialog;

    }

    public static Dialog createLoadingDialogCanCancel(Context context,
	    String msg) {

	LayoutInflater inflater = LayoutInflater.from(context);
	View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
	LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
	// main.xml中的ImageView
	ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
	TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
	// 加载动画
	Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
		context, R.anim.loading_animation);
	// 使用ImageView显示动画
	spaceshipImage.startAnimation(hyperspaceJumpAnimation);
	tipTextView.setText(msg);// 设置加载信息

	Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

	loadingDialog.setCancelable(true);// 不可以用“返回键”取消
	loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
		LinearLayout.LayoutParams.FILL_PARENT,
		LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
	return loadingDialog;

    }

    public static boolean isBackground(Context context) {

	String packageName = context.getPackageName();
	ActivityManager activityManager = (ActivityManager) context
		.getSystemService(Context.ACTIVITY_SERVICE);
	List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
	if (tasksInfo.size() > 0) {
	    // 应用程序位于堆栈的顶层
	    if (packageName.equals(tasksInfo.get(0).topActivity
		    .getPackageName())) {
		return false;
	    }
	}
	return true;
    }

    public static void showNotification(Context context, String title,
	    String content, Intent tagIntent, int notifyId) {
	NotificationManager mNotificationManager = (NotificationManager) context
		.getSystemService(Activity.NOTIFICATION_SERVICE);
	NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
		context);
	mBuilder.setContentTitle(title)
		// 设置通知栏标题
		.setContentText(content)
		.setContentIntent(
			getDefalutIntent(context,
				Notification.FLAG_AUTO_CANCEL, tagIntent)) // 设置通知栏点击意图
		// .setNumber(number) //设置通知集合的数量
		.setTicker("收到了一条快玩小说消息") // 通知首次出现在通知栏，带上升动画效果的
		.setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
		.setPriority(Notification.PRIORITY_HIGH) // 设置该通知优先级
		.setAutoCancel(true)// 设置这个标志当用户单击面板就可以让通知将自动取消
		.setOngoing(false)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
		// .setDefaults(Notification.DEFAULT_SOUND)//
		// 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
		// Notification.DEFAULT_ALL Notification.DEFAULT_SOUND 添加声音 //
		// requires VIBRATE permission
		.setSmallIcon(R.drawable.ic_launcher);// 设置通知小ICON
	//mBuilder.setDefaults(getNotifyDefaults(context));

	mNotificationManager.notify(notifyId, mBuilder.build());
    }


	public static void showPopNotify(final Context context, String[] contents,
									 final View.OnClickListener listener) {

		if (PreferenceUtil.getInstance(context).getInt(
				CommonUtils.NOTIFY_TYPE_PREFERENCE, 2) == 3)// 设置了不提示
		{
			return;
		}

		DialogPlusBuilder dialog = DialogPlus.newDialog(context);
		dialog.setContentHolder(new ViewHolder(R.layout.pop_notify_layout));
		dialog.setGravity(Gravity.TOP);
		dialog.setInAnimation(R.anim.push_top_in);
		dialog.setOutAnimation(R.anim.push_top_out);
		dialog.setOverlayBackgroundResource(android.R.color.transparent);
		final DialogPlus showPopDialog = dialog.create();
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				showPopDialog.dismiss();
			}
		}, 5000);
		showPopDialog.show();
		View setView = showPopDialog.getHolderView();
		setView.setPadding(PixelUtil.dp2Pixel(10, context), PreferenceUtil
				.getInstance(context).getInt("titleH", 40), PixelUtil.dp2Pixel(
				10, context), PixelUtil.dp2Pixel(10, context));
		TextView textView1 = (TextView) setView.findViewById(R.id.text1_tv);
		TextView textView2 = (TextView) setView.findViewById(R.id.text2_tv);
		TextView textView3 = (TextView) setView.findViewById(R.id.text3_tv);
		if (contents.length == 1) {
			textView1.setText(contents[0]);
		} else if (contents.length == 2) {
			textView1.setText(contents[0]);
			textView2.setText(contents[1]);
		} else if (contents.length == 3) {
			textView1.setText(contents[0]);
			textView2.setText(contents[1]);
			textView3.setText(contents[2]);
		}

		textView1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showPopDialog.dismiss();
				listener.onClick(v);
			}
		});
		textView2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showPopDialog.dismiss();
				listener.onClick(v);
			}
		});
		textView3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showPopDialog.dismiss();
				listener.onClick(v);
			}
		});
		setView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showPopDialog.dismiss();
				listener.onClick(v);
			}
		});

	}




    public static PendingIntent getDefalutIntent(Context context, int flags,
	    Intent tagIntent) {

	PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
		tagIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	return pendingIntent;
    }

    public static void startVibrator(Context context) {
	Vibrator vibrator = (Vibrator) context
		.getSystemService(Context.VIBRATOR_SERVICE);
	long[] pattern = { 100, 400, 100, 400 }; // 停止 开启 停止 开启
	vibrator.vibrate(pattern, -1);
    }
}
