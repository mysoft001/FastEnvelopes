//package go.fast.fastenvelopes.receiver;
//
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//
//import com.igexin.sdk.PushConsts;
//import com.igexin.sdk.PushManager;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//
//import go.fast.fastenvelopes.info.BaseInfo;
//import go.fast.fastenvelopes.info.PushMsgInfo;
//import go.fast.fastenvelopes.json.JsonUtils;
//import go.fast.fastenvelopes.utils.CommonUtils;
//import go.fast.fastenvelopes.utils.PreferenceUtil;
//import go.fast.fastenvelopes.utils.PreferenceUtils;
//import go.fast.fastenvelopes.utils.ToolUtils;
//
//public class PushReceiver extends BroadcastReceiver {
//
//    // private final int USER_ATTENTION_TAG = 1;// 有用户关注的通知tag
//
//    public static final String SEND_ATTENTION_ACTION = "new_attention_actiton";
//
//    public static final String SEND_ESPECIALLY_ACTION = "new_especially_actiton";
//
//
//    public static final String SEND_REWARD_ACTION = "new_reward_actiton";//打赏的action
//
//    /**
//     * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiSdkDemoActivity.tLogView ==
//     * null)
//     */
//    public static StringBuilder payloadData = new StringBuilder();
//
//    @Override
//    public void onReceive(final Context context, Intent intent) {
//	Bundle bundle = intent.getExtras();
//	Log.d("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action"));
//
//	switch (bundle.getInt(PushConsts.CMD_ACTION)) {
//	case PushConsts.GET_MSG_DATA:
//	    // 获取透传数据
//	    // String appid = bundle.getString("appid");
//	    byte[] payload = bundle.getByteArray("payload");
//
//	    String taskid = bundle.getString("taskid");
//	    String messageid = bundle.getString("messageid");
//
//	    // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
//	    boolean result = PushManager.getInstance().sendFeedbackMessage(
//		    context, taskid, messageid, 90001);
//	    System.out.println("第三方回执接口调用" + (result ? "成功" : "失败"));
//	    // ToolUtils.showToast(context, "GET_MSG_DATA  " + payload);
//
//	    if (payload != null) {
//		String data = new String(payload);
//
//		// data="{'nickname':'你好吗','account':'1004','type':1}";
//		System.out.println("回来的 json----" + data);
//
//		JsonUtils<PushMsgInfo> jsonUtils = new JsonUtils<>(
//			PushMsgInfo.class);
//		try {
//		    PushMsgInfo pushMsgInfo = jsonUtils.parseResponseNow(data,
//			    false);
//		    showAttentionNotify(context, pushMsgInfo);// 根据消息类型跳转不同页面
//
//		} catch (Throwable e) {
//
//		    System.out.println("     解析异常     " + e);
//		    ToolUtils.showToast(context, " Json解析异常  ");
//		    e.printStackTrace();
//		}
//
//		Log.d("GetuiSdkDemo", "receiver payload : " + data);
//
//		payloadData.append(data);
//		payloadData.append("\n");
//
//		// if (GetuiSdkDemoActivity.tLogView != null) {
//		// GetuiSdkDemoActivity.tLogView.append(data + "\n");
//		// }
//	    }
//	    break;
//
//	case PushConsts.GET_CLIENTID:
//
//	    // 获取ClientID(CID)
//	    // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
//	    final String cid = bundle.getString("clientid");
//	    System.out.println("   GET_CLIENTID         " + cid);
//
//	    HttpRequest.bindClicentId(context, cid, new onRequestCallback() {
//		@Override
//		public void onSuccess(BaseInfo response) {
//
//		}
//
//		@Override
//		public void onFailure(String rawJsonData) {
//		}
//	    });
//	    break;
//	case PushConsts.GET_SDKONLINESTATE:
//	    boolean online = bundle.getBoolean("onlineState");
//	    Log.d("GetuiSdkDemo", "online = " + online);
//	    break;
//
//	case PushConsts.SET_TAG_RESULT:
//	    String sn = bundle.getString("sn");
//	    String code = bundle.getString("code");
//	    ToolUtils.showToast(context, "收到了设置用户标签的回调");
//	    String text = "设置标签失败, 未知异常";
//	    switch (Integer.valueOf(code)) {
//	    case PushConsts.SETTAG_SUCCESS:
//		text = "设置标签成功";
//		break;
//
//	    case PushConsts.SETTAG_ERROR_COUNT:
//		text = "设置标签失败, tag数量过大, 最大不能超过200个";
//		break;
//
//	    case PushConsts.SETTAG_ERROR_FREQUENCY:
//		text = "设置标签失败, 频率过快, 两次间隔应大于1s";
//		break;
//
//	    case PushConsts.SETTAG_ERROR_REPEAT:
//		text = "设置标签失败, 标签重复";
//		break;
//
//	    case PushConsts.SETTAG_ERROR_UNBIND:
//		text = "设置标签失败, 服务未初始化成功";
//		break;
//
//	    case PushConsts.SETTAG_ERROR_EXCEPTION:
//		text = "设置标签失败, 未知异常";
//		break;
//
//	    case PushConsts.SETTAG_ERROR_NULL:
//		text = "设置标签失败, tag 为空";
//		break;
//
//	    case PushConsts.SETTAG_NOTONLINE:
//		text = "还未登陆成功";
//		break;
//
//	    case PushConsts.SETTAG_IN_BLACKLIST:
//		text = "该应用已经在黑名单中,请联系售后支持!";
//		break;
//
//	    case PushConsts.SETTAG_NUM_EXCEED:
//		text = "已存 tag 超过限制";
//		break;
//
//	    default:
//		break;
//	    }
//
//	    Log.d("GetuiSdkDemo", "settag result sn = " + sn + ", code = "
//		    + code);
//	    Log.d("GetuiSdkDemo", "settag result sn = " + text);
//	    break;
//	case PushConsts.THIRDPART_FEEDBACK:
//	    /*
//	     * String appid = bundle.getString("appid"); String taskid =
//	     * bundle.getString("taskid"); String actionid =
//	     * bundle.getString("actionid"); String result =
//	     * bundle.getString("result"); long timestamp =
//	     * bundle.getLong("timestamp");
//	     *
//	     * Log.d("GetuiSdkDemo", "appid = " + appid); Log.d("GetuiSdkDemo",
//	     * "taskid = " + taskid); Log.d("GetuiSdkDemo", "actionid = " +
//	     * actionid); Log.d("GetuiSdkDemo", "result = " + result);
//	     * Log.d("GetuiSdkDemo", "timestamp = " + timestamp);
//	     */
//	    break;
//
//	default:
//	    break;
//	}
//    }
//
//    private void showAttentionNotify(final Context context,
//	    PushMsgInfo pushMsgInfo) {
//
//	if(!ImageLoader.getInstance().isInited())
//	{
//	    ImageLoader.getInstance().init(
//			ImageLoaderConfiguration.createDefault(context));
//	}
//
//
//	pushMsgInfo.createTime = System.currentTimeMillis();
//
//	if (pushMsgInfo.type < 9) {
//	    PreferenceUtils.getInstance(context).setSettingUnreadMsg(true);
//	    CoolDBHelper.insertNotify(context, pushMsgInfo);// 将通知消息存到本地消息表中
//	}
//
//	// ToolUtils.showToast(context,
//	// "应用在后台吗----"+ToolUtils.isBackground(context));
//
//
//	if (ToolUtils.isBackground(context)) {
//
//	    NotifyPushMsgInfo(context, pushMsgInfo);
//
//	} else {
//
//	    if(PreferenceUtil.getInstance(context).getInt(CommonUtils.NOTIFY_TYPE_PREFERENCE, 2)==1
//		    ||PreferenceUtil.getInstance(context).getInt(CommonUtils.NOTIFY_TYPE_PREFERENCE, 2)==2)
//	    {
//		ToolUtils.startVibrator(context);// 震动提醒
//	    }
//	    Intent intent = new Intent();
//	    // 设置Intent的Action属性
//
//	    if (pushMsgInfo.type==9 ||pushMsgInfo.type==10)// 当大于8的时候
//	    {
//		intent.setAction(SEND_ESPECIALLY_ACTION);
//
//		System.out.println("  发送评论广播  ");
//	    } else
//	    if(pushMsgInfo.type==11)
//	    {
//
//		intent.setAction(SEND_REWARD_ACTION);
//
//	    }
//	    else
//	    {
//		intent.setAction(SEND_ATTENTION_ACTION);
//	    }
//	    // 如果只传一个bundle的信息，可以不包bundle，直接放在intent里
//	    // intent.putExtra("content",pushMsgInfo.nickname
//	    // + "关注了你");
//	    intent.putExtra("content", pushMsgInfo);
//	    // 发送广播
//	    context.sendBroadcast(intent);
//
//	}
//
//    }
//
//    private void NotifyPushMsgInfo(final Context context,
//	    PushMsgInfo pushMsgInfo) {
//	switch (pushMsgInfo.type) {
//
//	case PushMsgOperating.USER_ATTENTION_TAG:
//	    userAttentionYou(context, pushMsgInfo);
//	    break;
//	case PushMsgOperating.USER_COMMENT_TAG:
//	    userCommentArticle(context, pushMsgInfo);
//	    break;
//	case PushMsgOperating.USER_ZAN_TAG:
//	    userZan(context, pushMsgInfo);
//	    break;
//	case PushMsgOperating.USER_CALLBACK_TAG:
//	    userCallback(context, pushMsgInfo);
//	    break;
//	case PushMsgOperating.ARTICLE_COLLECTED_UPDATE:
//	    collectedArticleUpdate(context, pushMsgInfo);
//	    break;
//	case PushMsgOperating.ARTICLE_CHECK:
//	    articleNeedCheck(context, pushMsgInfo);
//	    break;
//	case PushMsgOperating.ARTICLE_SHARE:
//	    shareYourArticle(context, pushMsgInfo);
//	    break;
//
//	case PushMsgOperating.ARTICLE_COLLECTED:
//	    coolectYourArticle(context, pushMsgInfo);
//	    break;
//	}
//    }
//
//    private void userAttentionYou(final Context context,
//	    final PushMsgInfo pushMsgInfo) {
//
//	Intent attentionIntent = new Intent(context,
//		ShowAttentionActivity.class);
//	attentionIntent.putExtra("fromNotify", true);
//	attentionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	attentionIntent.setAction(".wholewriter.showAttentionAction");
//
//	ToolUtils
//		.showNotification(context, "全民小说", pushMsgInfo.nickname
//			+ " 关注了你", attentionIntent,
//			PushMsgOperating.USER_ATTENTION_TAG);
//    }
//
//    /**
//     * 有用户评论了你的作品
//     *
//     * @param context
//     * @param pushMsgInfo
//     */
//    private void userCommentArticle(final Context context,
//	    final PushMsgInfo pushMsgInfo) {
//
//	Intent intent = new Intent(context, DetailArticleInfoActivity.class);
//	intent.putExtra("fromNotify", true);
//	intent.putExtra("pageInfo", pushMsgInfo);
//	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	ToolUtils.showNotification(context, "全民小说", pushMsgInfo.nickname
//		+ " 评论了你的作品" + "《" + pushMsgInfo.articleName + "》", intent,
//		PushMsgOperating.USER_COMMENT_TAG);
//
//    }
//
//    private void userZan(final Context context, final PushMsgInfo pushMsgInfo) {
//	Intent intent = new Intent(context, SplashActivity.class);
//	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	ToolUtils.showNotification(context, "全民小说", pushMsgInfo.nickname
//		+ " 赞了你", intent, PushMsgOperating.USER_ZAN_TAG);
//
//	// ToolUtils.showNotification(context, "全民小说", pushMsgInfo.nickname
//	// + " 赞了你的作品" + "《" + pushMsgInfo.articleName + "》", intent,
//	// PushMsgOperating.USER_ZAN_TAG);
//    }
//
//    private void userCallback(final Context context,
//	    final PushMsgInfo pushMsgInfo) {
//	Intent intent = new Intent(context, DetailArticleInfoActivity.class);
//	intent.putExtra("fromNotify", true);
//	intent.putExtra("pageInfo", pushMsgInfo);
//	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	ToolUtils.showNotification(context, "全民小说", pushMsgInfo.nickname
//		+ " 在作品" + "《" + pushMsgInfo.articleName + "》中回复了你", intent,
//		PushMsgOperating.USER_CALLBACK_TAG);
//    }
//
//    private void collectedArticleUpdate(final Context context,
//	    final PushMsgInfo pushMsgInfo) {
//	Intent intent = new Intent(context, BookPlayActivity.class);
//	intent.putExtra("fromNotify", true);
//	intent.putExtra("pageInfo", pushMsgInfo);
//	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	ToolUtils.showNotification(context, "全民小说", "作品" + "《"
//		+ pushMsgInfo.articleName + "》更新了新的章节，快去看吧", intent,
//		PushMsgOperating.ARTICLE_COLLECTED_UPDATE);
//    }
//
//    private void articleNeedCheck(final Context context,
//	    final PushMsgInfo pushMsgInfo) {
//	Intent intent = new Intent(context, DetailArticleInfoActivity.class);
//	intent.putExtra("fromNotify", true);
//	intent.putExtra("pageInfo", pushMsgInfo);
//	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	ToolUtils.showNotification(context, "全民小说", "作品" + "《"
//		+ pushMsgInfo.articleName + "》续写了新内容，需要你审核", intent,
//		PushMsgOperating.ARTICLE_CHECK);
//    }
//
//    private void shareYourArticle(final Context context,
//	    final PushMsgInfo pushMsgInfo) {
//
//	Intent intent = new Intent(context, SplashActivity.class);
//	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	ToolUtils.showNotification(context, "全民小说", pushMsgInfo.nickname
//		+ "分享了你的作品" + "《" + pushMsgInfo.articleName + "》", intent,
//		PushMsgOperating.ARTICLE_SHARE);
//    }
//
//    private void coolectYourArticle(final Context context,
//	    final PushMsgInfo pushMsgInfo) {
//
//	Intent intent = new Intent(context, DetailArticleInfoActivity.class);
//	intent.putExtra("fromNotify", true);
//	intent.putExtra("pageInfo", pushMsgInfo);
//	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	ToolUtils.showNotification(context, "全民小说", pushMsgInfo.nickname
//		+ " 收藏了你的作品" + "《" + pushMsgInfo.articleName + "》", intent,
//		PushMsgOperating.ARTICLE_COLLECTED);
//
//    }
//
//}
