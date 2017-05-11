package go.fast.fastenvelopes.service;

import android.content.Context;
import android.content.Intent;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import go.fast.fastenvelopes.activity.PersonalLetterActivity;
import go.fast.fastenvelopes.activity.ShowAttentionActivity;
import go.fast.fastenvelopes.db.CoolDBHelper;
import go.fast.fastenvelopes.info.BaseInfo;
import go.fast.fastenvelopes.info.MessageInfo;
import go.fast.fastenvelopes.info.PushMsgInfo;
import go.fast.fastenvelopes.info.SessionItemInfo;
import go.fast.fastenvelopes.info.UserInfo;
import go.fast.fastenvelopes.json.HttpRequest;
import go.fast.fastenvelopes.json.JsonUtils;
import go.fast.fastenvelopes.utils.CommonUtils;
import go.fast.fastenvelopes.utils.PreferenceUtil;
import go.fast.fastenvelopes.utils.PreferenceUtils;
import go.fast.fastenvelopes.utils.ToolUtils;

/**
 * Created by hanwei on 2017/1/6.
 */

public class PushIntentService extends GTIntentService {

    public static final String SEND_ATTENTION_ACTION = "new_attention_actiton";

    public static final String SEND_ESPECIALLY_ACTION = "new_especially_actiton";


    public static final String SEND_REWARD_ACTION = "new_reward_actiton";//打赏的action

    public static final String RECEIVE_MSG = "receive_msg_actiton";//获取聊天内容的action

    @Override
    public void onReceiveServicePid(Context context, int i) {

    }

    @Override
    public void onReceiveClientId(Context context, String s) {

        HttpRequest.bindClicentId(context, s, new HttpRequest.onRequestCallback() {
            @Override
            public void onSuccess(BaseInfo response) {
            }

            @Override
            public void onFailure(String rawJsonData) {
            }
        });
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage gtTransmitMessage) {

        String appid = gtTransmitMessage.getAppid();
        byte[] payload = gtTransmitMessage.getPayload();

        String taskid = gtTransmitMessage.getTaskId();
        String messageid = gtTransmitMessage.getMessageId();

        System.out.println("回来的 onReceiveMessageData----" + payload);
        if (payload != null) {
            String data = new String(payload);

            // data="{'nickname':'你好吗','account':'1004','type':1}";
            System.out.println("回来的 json----" + data);

            JsonUtils<PushMsgInfo> jsonUtils = new JsonUtils<>(
                    PushMsgInfo.class);
            try {
                PushMsgInfo pushMsgInfo = jsonUtils.parseResponseNow(data,
                        false);
                showAttentionNotify(context, pushMsgInfo);// 根据消息类型跳转不同页面

            } catch (Throwable e) {

                System.out.println("     解析异常     " + e);
                ToolUtils.showToast(context, " Json解析异常  ");
                e.printStackTrace();
            }


        }
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {

    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {

        ToolUtils.showToast(context, gtCmdMessage.getPkgName() + "       " + gtCmdMessage.getAppid());

    }


    private void showAttentionNotify(final Context context,
                                     PushMsgInfo pushMsgInfo) {

        if (!ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance().init(
                    ImageLoaderConfiguration.createDefault(context));
        }


        pushMsgInfo.createTime = System.currentTimeMillis();

        if (pushMsgInfo.type >6)
        {

            PreferenceUtils.getInstance(context).setSettingUnreadMsg(true);

            CoolDBHelper.insertNotify(context, pushMsgInfo);// 将通知消息存到本地消息表中

        }

       if(pushMsgInfo.type==CommonUtils.PUSH_MSG_TYPE_LETTER)
        {
            MessageInfo msgInfo = new MessageInfo();
            msgInfo.account = pushMsgInfo.account;
            msgInfo.content = pushMsgInfo.content;
            msgInfo.headurl =pushMsgInfo.headurl;
            msgInfo.nickname =pushMsgInfo.nickname;
            msgInfo.createdAt =pushMsgInfo.createdAt;

            CoolDBHelper.insertMessage(context, msgInfo.account,msgInfo);//更新聊天记录表
            SessionItemInfo sessionItemInfo=new SessionItemInfo();
            sessionItemInfo.lastContent=msgInfo.content;
            sessionItemInfo.lastPublishTime=pushMsgInfo.createdAt;
            sessionItemInfo.account= msgInfo.account;
            sessionItemInfo.nickname=pushMsgInfo.nickname;
            sessionItemInfo.headurl=pushMsgInfo.headurl;
            sessionItemInfo.sex=pushMsgInfo.sex;
            sessionItemInfo.level=pushMsgInfo.level;
            CoolDBHelper.insertOrUpdateSessionListByAccount(context,sessionItemInfo);//更新会话表
        }


        // ToolUtils.showToast(context,
        // "应用在后台吗----"+ToolUtils.isBackground(context));


        if (ToolUtils.isBackground(context)) {

            NotifyPushMsgInfo(context, pushMsgInfo);

        } else {

            if (PreferenceUtil.getInstance(context).getInt(CommonUtils.NOTIFY_TYPE_PREFERENCE, 2) == 1
                    || PreferenceUtil.getInstance(context).getInt(CommonUtils.NOTIFY_TYPE_PREFERENCE, 2) == 2) {
                ToolUtils.startVibrator(context);// 震动提醒
            }
            Intent intent = new Intent();
            // 设置Intent的Action属性

            if (pushMsgInfo.type < 7)// 接收message消息（这是和红包聊天有关的消息）
            {
                intent.setAction(RECEIVE_MSG);
                System.out.println("  发送一条消息广播  " + pushMsgInfo.content);
            } else {
                intent.setAction(SEND_ATTENTION_ACTION);
            }
            // 如果只传一个bundle的信息，可以不包bundle，直接放在intent里
            // intent.putExtra("content",pushMsgInfo.nickname
            // + "关注了你");
            intent.putExtra("content", pushMsgInfo);
            // 发送广播
            sendBroadcast(intent);

        }

    }

    private void NotifyPushMsgInfo(final Context context,
                                   PushMsgInfo pushMsgInfo) {
        switch (pushMsgInfo.type) {

            case CommonUtils.PUSH_MSG_TYPE_USER_ATTENTION://用户关注

                userAttentionYou(context,
                 pushMsgInfo);
                break;

            case CommonUtils.PUSH_MSG_TYPE_LETTER://私信

                receiverALetter(context,
                        pushMsgInfo);
                break;
        }
    }

    private void userAttentionYou(final Context context,
                                  final PushMsgInfo pushMsgInfo) {

        Intent attentionIntent = new Intent(context,
                ShowAttentionActivity.class);
        attentionIntent.putExtra("fromNotify", true);
        attentionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        attentionIntent.setAction("com.just.wholewriter.showAttentionAction");

        ToolUtils
                .showNotification(context, "快玩红包", pushMsgInfo.nickname
                                + " 关注了你", attentionIntent,
                        CommonUtils.PUSH_MSG_TYPE_USER_ATTENTION);
    }

    private void receiverALetter(final Context context,
                                  final PushMsgInfo pushMsgInfo) {

        UserInfo userInfo=new UserInfo();
        userInfo.account=pushMsgInfo.account;
        userInfo.nickName=pushMsgInfo.nickname;
        userInfo.headurl=pushMsgInfo.headurl;
        Intent attentionIntent = new Intent(context,
                PersonalLetterActivity.class);
        attentionIntent.putExtra("fromNotify", true);
        attentionIntent.putExtra("userInfo", userInfo);
        attentionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       // attentionIntent.setAction("com.just.wholewriter.showAttentionAction");

        ToolUtils
                .showNotification(context, "快玩红包", pushMsgInfo.nickname
                                + " 给你发送了一条消息", attentionIntent,
                        CommonUtils.PUSH_MSG_TYPE_LETTER);
    }


}
