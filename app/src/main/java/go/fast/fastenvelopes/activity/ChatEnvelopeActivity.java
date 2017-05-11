package go.fast.fastenvelopes.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.http.ImageOptions;
import go.fast.fastenvelopes.info.BaseInfo;
import go.fast.fastenvelopes.info.FreeAnswerInfo;
import go.fast.fastenvelopes.info.MessageInfo;
import go.fast.fastenvelopes.info.PushMsgInfo;
import go.fast.fastenvelopes.json.HttpRequest;
import go.fast.fastenvelopes.service.PushIntentService;
import go.fast.fastenvelopes.utils.CommonUtils;
import go.fast.fastenvelopes.utils.DataUtils;
import go.fast.fastenvelopes.utils.HttpResponseUtil;
import go.fast.fastenvelopes.utils.PixelUtil;
import go.fast.fastenvelopes.utils.PreferenceUtils;
import go.fast.fastenvelopes.utils.ShareUtils;
import go.fast.fastenvelopes.utils.ToolUtils;
import go.fast.fastenvelopes.widgets.NormalPopWindow;


/**
 * 会话页面Fragment
 */
public class ChatEnvelopeActivity extends ChatBaseActivity implements View.OnClickListener {


    List<String> joinList;//参与的人列表

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInitView();

    }


    private void setInitView() {

        sendBtn.setOnClickListener(this);
        envelopeBtn.setOnClickListener(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(PushIntentService.RECEIVE_MSG);
        registerReceiver(receiver, filter);

    }



    @Override
    public void initMiddle(TextView middleView,TextView middleBottomView,View parentView) {

        this.middleBottomView=middleBottomView;
        parentView.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    ImageView rightImg;

    @Override
    public void initRight(ImageView imageView, TextView textView, View parent) {
        super.initRight(imageView, textView, parent);
        parent.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
        rightImg = imageView;
        imageView.setImageResource(R.drawable.expand_icon);
        // textView.setText("搜索");
        parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showOption();
            }
        });
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.startEnvelopeBtn:

                System.out.println("   startEnvelopeBtn " + roomInfo);
                if (roomInfo != null) {
                    System.out.println("   startEnvelopeBtn      roomInfo " + roomInfo.roomType);
                    if (roomInfo.roomType == CommonUtils.ENVELOPE_TYPE_GUESS) {
                        Intent guessIntent = new Intent(this,
                                CreateGuessEnvelopeActivity.class);
                        guessIntent.putExtra("roomInfo", roomInfo);
                        startActivity(guessIntent);
                    } else if (roomInfo.roomType == CommonUtils.ENVELOPE_TYPE_FREE) {
                        Intent freeIntent=new Intent(this,
                                CreateFreeEnvelopeActivity.class);
                        freeIntent.putExtra("roomInfo", roomInfo);
                        startActivity(freeIntent);
                    } else if (roomInfo.roomType == CommonUtils.ENVELOPE_TYPE_NEAR) {
                        Intent nearIntent=new Intent(this,
                                CreateNearEnvelopeActivity.class);
                        nearIntent.putExtra("roomInfo", roomInfo);
                        startActivity(nearIntent);
                    }
                }


                overridePendingTransition(
                        R.anim.slide_in_bottom, R.anim.slide_out_top);

                // showNumKeyBoard();
                break;

            case R.id.btn_send:

                sendMsg();
                break;
            case R.id.text_title_rl://显示或隐藏弹窗
                showOrDismissPop();
                break;
        }

    }
    private void showOrDismissPop()
    {
       if(isPopNeedShow==1)//猜金额的POP处于可展示状态
       {
           if(envelopePopView.isShown())
           {
               dismissEnvelopeLiving();
               middleBottomView.setText("(点我显示)");
           }
           else
           {
               middleBottomView.setText("(点我隐藏)");
               envelopePopView.setVisibility(View.VISIBLE);
               envelopePopView.startAnimation(AnimationUtils.loadAnimation(this,R.anim.slide_in_top));
           }


       }
        else   if(isPopNeedShow==2)//自由猜的POP处于可展示状态
       {
           if(freeEnvelopePopView.isShown())
           {
               dismissFreeEnvelopePop();
               middleBottomView.setText("(点我显示)");
           }
           else
           {
               middleBottomView.setText("(点我隐藏)");
               freeEnvelopePopView.setVisibility(View.VISIBLE);
               freeEnvelopePopView.startAnimation(AnimationUtils.loadAnimation(this,R.anim.slide_in_top));
           }


       }
        else//没有任何类型的红包正在进行（如有弹窗可隐藏）
       {
           if(freeEnvelopePopView.isShown())
           {
               dismissFreeEnvelopePop();
               middleBottomView.setVisibility(View.GONE);
           }
           if(envelopePopView.isShown())
           {
               dismissEnvelopeLiving();
               middleBottomView.setVisibility(View.GONE);
           }
       }


    }


    @Override
    public void onBackPressed() {

            super.onBackPressed();
    }




    //-------------------------------------------收到的消息处理开始--------------------------------------------------------------
    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(PushIntentService.RECEIVE_MSG)) {
                PushMsgInfo pushMsgInfo = (PushMsgInfo) intent
                        .getSerializableExtra("content");
                showMessage(pushMsgInfo);
            }

        }
    };

    private void showMessage(PushMsgInfo pushMsgInfo) {

        if(!pushMsgInfo.roomId.equals(roomInfo.roomId))//房间id不匹配不作处理
        {
            return;
        }
        if(PreferenceUtils.getInstance(this).getSettingUserAccount().equals(pushMsgInfo.account))//别人的消息不清除
        {
            msgEdit.setText("");
        }
        stopMsgDelay();//接收消息成功取消超时定时器
        unlockSendButton();
        if (pushMsgInfo.type == CommonUtils.PUSH_MSG_TYPE_NORMALMSG)//普通消息的推送
        {
            normalMessage(pushMsgInfo);
        } else if (pushMsgInfo.type == CommonUtils.PUSH_MSG_TYPE_START_ENVELOPE)//发起的红包
        {
            prepareEnvelope(pushMsgInfo);
        } else if (pushMsgInfo.type == CommonUtils.PUSH_MSG_TYPE_JOIN_ENVELOPE)//加入红包
        {
            joinEnvelope(pushMsgInfo);
        } else if (pushMsgInfo.type == CommonUtils.PUSH_MSG_TYPE_GO_ENVELOPE)//开始抢红包
        {
            startEnvelope(pushMsgInfo);
        } else if (pushMsgInfo.type == CommonUtils.PUSH_MSG_TYPE_GUESSING_ENVELOPE)//正在猜红包
        {
            guessingtEnvelope(pushMsgInfo);
        } else if (pushMsgInfo.type == CommonUtils.PUSH_MSG_TYPE_CANCEL_ENVELOPE)//取消猜红包
        {
            cancelEnvelope( pushMsgInfo);
        }



//        else if(pushMsgInfo.type==CommonUtils.PUSH_MSG_TYPE__RESULT_ENVELOPE)//抢红包结果
//        {
//            startEnvelope(pushMsgInfo);
//        }


    }

    /**
     * 发起红包的消息
     *
     * @param pushMsgInfo
     */
    private void prepareEnvelope(PushMsgInfo pushMsgInfo) {
        roomInfo.isEnvelopeing = 1;
        currentMsgType=CommonUtils.MESSAGE_TYPE_MSG_CONTENT;//输入框发送消息的类型
        envelopeBtn.setVisibility(View.GONE);//已经有人发起了红包那么隐藏发红包入口
        if (pushMsgInfo.envelopeType == CommonUtils.ENVELOPE_TYPE_FREE)//自由猜红包
        {

//            if(pushMsgInfo.envelopeStatus==CommonUtils.ENVELOPE_STATUS_START)//红包发起
//            {
//
//            }
            isPopNeedShow=2;//标识显示自由猜的弹窗
            showFreeEnvelopePop(pushMsgInfo);
            List<FreeAnswerInfo> answerInfoList = pushMsgInfo.freeAnswerList;
            if(answerInfoList!=null)
            {
                freeAnswerInfoList.addAll(answerInfoList);
            }
            freeJoinSizeT.setText(pushMsgInfo.envelopeJoinSize+"");
            if(freeAnswerInfoList.size()==0)
            {
                freeInstructText.setVisibility(View.VISIBLE);
            }
            else
            {
                freeInstructText.setVisibility(View.INVISIBLE);
            }
//            if(pushMsgInfo.envelopeStatus==CommonUtils.ENVELOPE_STATUS_START)
//            {
//
//            }
            freeJoinLeftT.setText("已有");
            freeAnswerText.setText("参与列表");
            msgEdit.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_NUMBER);
            SpannableString ss = new SpannableString("请输入你认为最大的那个红包金额");//定义hint的值
            AbsoluteSizeSpan ass = new AbsoluteSizeSpan(10,true);//设置字体大小 true表示单位是sp
            ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            msgEdit.setHint(new SpannedString(ss));
            currentMsgType=CommonUtils.MESSAGE_TYPE_MSG_GUESSING_ENVELOPE;
        } else if (pushMsgInfo.envelopeType == CommonUtils.ENVELOPE_TYPE_GUESS)//猜金额红包
        {
            joinList = pushMsgInfo.joinList;
            if (joinList == null) {
                return;
            }

            showEnvelopeLiving(pushMsgInfo);
            roomInfo.currentCreateEnvelopeNickname=pushMsgInfo.nickname;//将发送红包的用户昵称记录下来

            if (PreferenceUtils.getInstance(this).getSettingUserAccount().equals(pushMsgInfo.account))//红包是自己发的
            {
                startorJoinText.setText("立即开始");
            } else {
                if (joinList.contains(PreferenceUtils.getInstance(this).getSettingUserAccount()))//我参与了抢红包游戏
                {
                    startorJoinText.setText("准备开抢");
                } else {
                    //startorJoinText.setText("立即参与");
                }
            }

            if (pushMsgInfo.envelopeStatus == CommonUtils.ENVELOPE_STATUS_GUESSING)//说明是刚进入页面
            {
                currentMsgType = CommonUtils.MESSAGE_TYPE_MSG_GUESSING_ENVELOPE;//设置当前的消息类型为正在抢红包
                delayStartTimeLayout.setVisibility(View.INVISIBLE);
                startorJoinText.setVisibility(View.INVISIBLE);
                currentHeadImg.setVisibility(View.INVISIBLE);
                currentNameTextV.setVisibility(View.INVISIBLE);
                answerPrepareTextV.setVisibility(View.INVISIBLE);
                guessingLayout.setVisibility(View.INVISIBLE);
                if (pushMsgInfo.envelopePower == CommonUtils.ENVELOPE_PERMISSION_ALL)//所有人可以抢的
                {

                    needGoldTitleText.setText("红包金币数");
                    joinSizeText.setText("所有人可抢");
                    msgEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
                    msgEdit.setHint("请输入数字答案");

                } else//参与者可猜
                {
                    needGoldTitleText.setText("红包金币数");
                    if (joinList == null) {
                        return;
                    }
                    if (joinList.contains(PreferenceUtils.getInstance(this).getSettingUserAccount()))//我参与了抢红包游戏
                    {
                        msgEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
                        msgEdit.setHint("请输入数字答案");
                    } else//没有参与
                    {
                        msgEdit.setHint("你未参与。正在进行红包游戏，无法进行聊天");
                        msgEdit.setEnabled(false);
                        sendBtn.setClickable(false);
                    }

                }
            }
        }


    }

    /**
     * 加入红包的信息处理
     *
     * @param pushMsgInfo
     */
    private void joinEnvelope(PushMsgInfo pushMsgInfo) {
//        if(envelopeBtn.isShown())//说明是刚进入页面
//        {
//            prepareEnvelope( pushMsgInfo);
//        }

       // currentMsgType = CommonUtils.MESSAGE_TYPE_JOIN_ENVELOPE;
        roomInfo.isEnvelopeing = 1;
        MessageInfo msgInfo = new MessageInfo();
        msgInfo.roomId = pushMsgInfo.roomId;
        msgInfo.account = pushMsgInfo.account;
        msgInfo.content = pushMsgInfo.content;
        msgInfo.headurl = pushMsgInfo.headurl;
        msgInfo.envelopeType=pushMsgInfo.envelopeType;
        msgInfo.createdAt = pushMsgInfo.createdAt;
        msgInfo.chatType = CommonUtils.MESSAGE_TYPE_JOIN_ENVELOPE;
        // msgInfo.isPreLoading=true;
        chatInfoList.add(msgInfo);
        chatlListAdapter.notifyDataSetChanged();
        chatListv.smoothScrollToPosition(chatInfoList.size());
        joinSizeText.setText("" + pushMsgInfo.envelopeJoinSize);
        if (DataUtils.isMe(this, pushMsgInfo.account)) {
            startorJoinText.setText("准备开抢");
        } else {
            //startorJoinText.setText("立即参与");
        }


    }

    private void startEnvelope(PushMsgInfo pushMsgInfo) {
        roomInfo.isEnvelopeing = 1;//房间有红包正在进行
//        if(envelopeBtn.isShown())//说明是刚进入页面
//        {
//            prepareEnvelope( pushMsgInfo);
//            joinEnvelope( pushMsgInfo);
//        }


        if(pushMsgInfo.envelopeType==CommonUtils.ENVELOPE_TYPE_FREE)//自由猜
        {

            showFreeEnvelopePop(pushMsgInfo);


        }
else if(pushMsgInfo.envelopeType==CommonUtils.ENVELOPE_TYPE_GUESS)//猜金额
        {
            if (pushMsgInfo.envelopePower == CommonUtils.ENVELOPE_PERMISSION_ALL)//所有人可以抢的
            {
                envelopeBtn.setVisibility(View.GONE);//已经有人发起了红包那么隐藏发红包入口
                showEnvelopeLiving(pushMsgInfo);
                joinSizeText.setText("所有人可抢");
                msgEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
                msgEdit.setHint("请输入数字答案");

            } else//参与者可抢
            {
                joinList = pushMsgInfo.joinList;
                if (joinList == null) {
                    return;
                }
                if (joinList.contains(PreferenceUtils.getInstance(this).getSettingUserAccount()))//我参与了抢红包游戏
                {
                    msgEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
                    msgEdit.setHint("请输入数字答案");
                } else//没有参与
                {
                    msgEdit.setHint("你未参与。正在进行红包游戏，无法进行聊天");
                    msgEdit.setEnabled(false);
                    sendBtn.setClickable(false);
                }
            }

            currentMsgType = CommonUtils.MESSAGE_TYPE_MSG_GUESSING_ENVELOPE;//设置当前的消息类型为正在抢红包
            currentHeadImg.setVisibility(View.INVISIBLE);
            currentNameTextV.setVisibility(View.INVISIBLE);
            answerPrepareTextV.setVisibility(View.INVISIBLE);
            guessingLayout.setVisibility(View.VISIBLE);
            delayStartTimeLayout.setVisibility(View.INVISIBLE);
            startorJoinText.setVisibility(View.INVISIBLE);
            currentAnswerTextV.setText("开始抢吧！");
            needGoldTitleText.setText("红包金币数");
        }

    }

    /**
     * 正在猜红包的消息
     *
     * @param pushMsgInfo
     */
    private void guessingtEnvelope(PushMsgInfo pushMsgInfo) {
        roomInfo.isEnvelopeing = 1;

        if (pushMsgInfo.envelopeType == CommonUtils.ENVELOPE_TYPE_GUESS) {
            delayStartTimeLayout.setVisibility(View.INVISIBLE);
            startorJoinText.setVisibility(View.INVISIBLE);
            currentHeadImg.setVisibility(View.VISIBLE);
            currentNameTextV.setVisibility(View.VISIBLE);
            answerPrepareTextV.setVisibility(View.VISIBLE);
            guessingLayout.setVisibility(View.VISIBLE);

            if (ToolUtils.isNullOrEmpty(pushMsgInfo.headurl)) {
            } else {
                ImageLoader.getInstance().displayImage(
                        pushMsgInfo.headurl,
                        currentHeadImg,
                        ImageOptions.getOptionsByRound(PixelUtil.dp2Pixel(9,
                                this)));
            }
            currentAnswerTextV.setText(pushMsgInfo.content);
            currentNameTextV.setText(pushMsgInfo.nickname);

            if (pushMsgInfo.envelopeResult == 0) {
                answerPrepareTextV.setText("猜中啦！");
            } else if (pushMsgInfo.envelopeResult == -1) {
                answerPrepareTextV.setText("小了");
            } else {
                answerPrepareTextV.setText("大了");
            }
            MessageInfo msgInfo = new MessageInfo();
            msgInfo.roomId = pushMsgInfo.roomId;
            msgInfo.account = pushMsgInfo.account;
            msgInfo.envelopeResult = pushMsgInfo.envelopeResult;
            msgInfo.content = pushMsgInfo.content;
            msgInfo.envelopeType=pushMsgInfo.envelopeType;
            msgInfo.headurl = pushMsgInfo.headurl;
            msgInfo.createdAt = pushMsgInfo.createdAt;
            msgInfo.chatType = CommonUtils.MESSAGE_TYPE_MSG_GUESSING_ENVELOPE;
            // msgInfo.isPreLoading=true;

            if (PreferenceUtils.getInstance(this).getSettingUserAccount().equals(pushMsgInfo.account))//如果是自己发的消息那么覆盖之前预加载的
            {
                chatInfoList.set(chatInfoList.size() - 1, msgInfo);
            } else {
                chatInfoList.add(msgInfo);
            }


            if (pushMsgInfo.envelopeResult == 0) {
                MessageInfo resultMsg = new MessageInfo();
                resultMsg.roomId = pushMsgInfo.roomId;
                resultMsg.account = pushMsgInfo.account;
                resultMsg.envelopeResult = pushMsgInfo.envelopeResult;
                resultMsg.content = pushMsgInfo.content;
                resultMsg.headurl = pushMsgInfo.headurl;
                resultMsg.nickname = pushMsgInfo.nickname;
                msgInfo.envelopeType=pushMsgInfo.envelopeType;
                resultMsg.envelopeMoneySize = pushMsgInfo.envelopeMoneySize;
                resultMsg.createdAt = pushMsgInfo.createdAt;
                resultMsg.chatType = CommonUtils.MESSAGE_TYPE_MSG_RESULT_ENVELOPE;//猜中了结果
                chatInfoList.add(resultMsg);
//            chatlListAdapter.notifyDataSetChanged();
//            chatListv.smoothScrollToPosition(chatInfoList.size());

                //----------------------------------去除红包相关的一些参数---------------
                envelopeBtn.setVisibility(View.VISIBLE);//显示发红包的入口
                msgEdit.setHint("请输入聊天内容");//允许用户进行输入
                msgEdit.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
                msgEdit.setEnabled(true);
                sendBtn.setClickable(true);
                roomInfo.isEnvelopeing = 0;
                currentMsgType = CommonUtils.MESSAGE_TYPE_MSG_CONTENT;//设置当前的消息类型为聊天
                if (joinList != null) {
                    joinList.clear();
                }

                isPopNeedShow=0;//此时可以关闭弹窗
                if(!envelopePopView.isShown())
                {
                    middleBottomView.setVisibility(View.GONE);
                }
                envelopePopView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismissEnvelopeLiving();
                        envelopePopView.setOnClickListener(null);
                    }
                });
            }
            chatlListAdapter.notifyDataSetChanged();
            chatListv.smoothScrollToPosition(chatInfoList.size());


        }
        else if(pushMsgInfo.envelopeType==CommonUtils.ENVELOPE_TYPE_FREE)//自由猜红包
        {



            if(pushMsgInfo.chatType==CommonUtils.MESSAGE_TYPE_MSG_RESULT_ENVELOPE)//红包结果
            {

                if(pushMsgInfo.envelopeFullResult==1)//满员的结果那么拼接输入的这条消息
                {
                    //不论是正在猜还是最后一个输入金额恰好等于红包个数 这时候返回一个结果类似的聊天类型都将添加一条正在猜的消息
                    MessageInfo msgInfo = new MessageInfo();
                    msgInfo.roomId = pushMsgInfo.roomId;
                    msgInfo.account = pushMsgInfo.account;
                    msgInfo.content = pushMsgInfo.content;
                    msgInfo.headurl = pushMsgInfo.headurl;
                    msgInfo.createdAt = pushMsgInfo.createdAt;
                    msgInfo.envelopeType=pushMsgInfo.envelopeType;
                    msgInfo.chatType = CommonUtils.MESSAGE_TYPE_MSG_GUESSING_ENVELOPE;
                    if (PreferenceUtils.getInstance(this).getSettingUserAccount().equals(pushMsgInfo.account))//如果是自己发的消息那么覆盖之前预加载的
                    {
                        chatInfoList.set(chatInfoList.size() - 1, msgInfo);
                    } else {
                        chatInfoList.add(msgInfo);
                    }
                }
                else//非满员 即参与人数不足倒计时时间到了 分发的结果消息
                {
                    freeDelayTimeLT.setText("该红包已分发完毕。所有参与者已领取");
                }
                if(pushMsgInfo.freeAnswerList!=null)
                {
                    for(int i=0;i<pushMsgInfo.freeAnswerList.size();i++)//将所有结果显示在消息列表中
                    {
                        MessageInfo resultFreeMsg = new MessageInfo();
                        resultFreeMsg.roomId = pushMsgInfo.roomId;
                        resultFreeMsg.account = pushMsgInfo.freeAnswerList.get(i).account;
                        resultFreeMsg.goldSize =pushMsgInfo.freeAnswerList.get(i).goldSize ;
                        resultFreeMsg.headurl = pushMsgInfo.freeAnswerList.get(i).headurl;
                        resultFreeMsg.createdAt = pushMsgInfo.createdAt;
                        resultFreeMsg.nickname=pushMsgInfo.freeAnswerList.get(i).nickname;
                        resultFreeMsg.envelopeType=pushMsgInfo.envelopeType;
                        resultFreeMsg.chatType = pushMsgInfo.chatType;
                        chatInfoList.add(resultFreeMsg);
                    }

                }
                freeJoinLeftT.setText("共有");
                freeAnswerText.setText("领取红包列表");
                if (joinList != null) {
                    joinList.clear();
                }


                isPopNeedShow=0;//此时可以关闭弹窗
                if(!freeEnvelopePopView.isShown())
                {
                    middleBottomView.setVisibility(View.GONE);
                }
                freeEnvelopePopView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismissFreeEnvelopePop();
                        freeEnvelopePopView.setOnClickListener(null);
                    }
                });

                chatlListAdapter.notifyDataSetChanged();
                chatListv.smoothScrollToPosition(chatInfoList.size());
                envelopeBtn.setVisibility(View.VISIBLE);//显示发红包的入口
                msgEdit.setHint("请输入聊天内容");//允许用户进行输入
                msgEdit.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
                msgEdit.setEnabled(true);
                sendBtn.setClickable(true);
                roomInfo.isEnvelopeing = 0;
                currentMsgType = CommonUtils.MESSAGE_TYPE_MSG_CONTENT;//设置当前的消息类型为聊天


            }
            else//输入红包金额正在猜 不是结果（人数够了或者倒计时时间到了）
            {
                //不论是正在猜还是最后一个输入金额恰好等于红包个数 这时候返回一个结果类似的聊天类型都将添加一条正在猜的消息
                MessageInfo msgInfo = new MessageInfo();
                msgInfo.roomId = pushMsgInfo.roomId;
                msgInfo.account = pushMsgInfo.account;
                msgInfo.content = pushMsgInfo.content;
                msgInfo.headurl = pushMsgInfo.headurl;
                msgInfo.createdAt = pushMsgInfo.createdAt;
                msgInfo.envelopeType=pushMsgInfo.envelopeType;
                msgInfo.chatType = CommonUtils.MESSAGE_TYPE_MSG_GUESSING_ENVELOPE;
                if (PreferenceUtils.getInstance(this).getSettingUserAccount().equals(pushMsgInfo.account))//如果是自己发的消息那么覆盖之前预加载的
                {
                    chatInfoList.set(chatInfoList.size() - 1, msgInfo);
                } else {
                    chatInfoList.add(msgInfo);
                }
                chatlListAdapter.notifyDataSetChanged();
                chatListv.smoothScrollToPosition(chatInfoList.size());
            }



          List<FreeAnswerInfo> answerInfoList=new ArrayList<>();
            if(pushMsgInfo.freeAnswerList!=null)
            {
                freeAnswerInfoList.clear();
                freeAnswerInfoList.addAll(pushMsgInfo.freeAnswerList);
                freeAnswerListAdapter.notifyDataSetChanged();
                freeAnswerListV.smoothScrollToPosition(freeAnswerInfoList.size());
            }
            if(freeAnswerInfoList.size()==0)
            {
                freeInstructText.setVisibility(View.VISIBLE);
            }
            else
            {
                freeInstructText.setVisibility(View.INVISIBLE);
            }
            freeJoinSizeT.setText(""+pushMsgInfo.envelopeJoinSize);

        }
        else if(pushMsgInfo.envelopeType==CommonUtils.ENVELOPE_TYPE_NEAR)//近者得红包
        {

        }


    }

    /**
     * 取消红包（猜金额可能因为时间到了 人数不足）
     *
     * @param pushMsgInfo
     */
    private void cancelEnvelope(PushMsgInfo pushMsgInfo) {
        roomInfo.isEnvelopeing = 1;//房间有红包正在进行
//        if(envelopeBtn.isShown())//说明是刚进入页面
//        {
//            prepareEnvelope( pushMsgInfo);
//            joinEnvelope( pushMsgInfo);
//        }
        envelopeBtn.setVisibility(View.VISIBLE);//显示发红包的入口
        msgEdit.setHint("请输入聊天内容");//允许用户进行输入
        msgEdit.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
        msgEdit.setEnabled(true);
        sendBtn.setClickable(true);
        isPopNeedShow=0;//此时可以关闭弹窗
        middleBottomView.setVisibility(View.GONE);

        if (pushMsgInfo.envelopeType == CommonUtils.ENVELOPE_TYPE_GUESS) {
            dismissEnvelopeLiving();
            ToolUtils.showToast(this, "由于参与人数不足，该红包游戏已经自动取消。红包已退回发送者");
        } else if (pushMsgInfo.envelopeType == CommonUtils.ENVELOPE_TYPE_FREE) {
            dismissFreeEnvelopePop();
            ToolUtils.showToast(this, "由于没有任何人参与到猜红包游戏中，游戏取消，红包已经退回发送者");
        }
    }

    /**
     * 普通消息处理
     *
     * @param pushMsgInfo
     */
    private void normalMessage(PushMsgInfo pushMsgInfo) {
        if (pushMsgInfo.chatType == CommonUtils.MESSAGE_TYPE_MSG_CONTENT)//是基础的聊天内容
        {
            MessageInfo msgInfo = new MessageInfo();
            msgInfo.roomId = pushMsgInfo.roomId;
            msgInfo.account = pushMsgInfo.account;
            msgInfo.content = pushMsgInfo.content;
            msgInfo.headurl = pushMsgInfo.headurl;
            msgInfo.createdAt = pushMsgInfo.createdAt;
            msgInfo.chatType = CommonUtils.MESSAGE_TYPE_MSG_CONTENT;
            // msgInfo.isPreLoading=true;

            if (PreferenceUtils.getInstance(this).getSettingUserAccount().equals(pushMsgInfo.account))//如果是自己发的消息那么覆盖之前预加载的
            {
                chatInfoList.set(chatInfoList.size() - 1, msgInfo);
            } else {
                chatInfoList.add(msgInfo);
            }
            chatlListAdapter.notifyDataSetChanged();
            chatListv.smoothScrollToPosition(chatInfoList.size());

            //ToolUtils.showToast(ChatEnvelopeActivity.this,"发送消息成功");
        }
    }

    //----------------------------------------收到的消息处理结束-----------------------------------------------

    NormalPopWindow pop;

    @SuppressLint("NewApi")
    protected void showOption() {


        if (pop == null) {
            pop = new NormalPopWindow(this, new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                }
            });

            maskingView.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= 19) {
                pop.showAsDropDown(rightImg, 0, 0, Gravity.END,
                        new String[]{(roomInfo.isAttention==1?"退出房间":"关注房间"), "分享房间", "玩法说明"},
                        new int[]{(roomInfo.isAttention==1?R.drawable.exit_room_info_icon:R.drawable.attention_info_icon),
                                 R.drawable.share_info_icon,
                                R.drawable.room_info_icon});
            } else {
                pop.showAsDropDown(rightImg, 0, 0, Gravity.END,
                        new String[]{(roomInfo.isAttention==1?"退出房间":"关注房间"), "分享房间", "玩法说明"},
                        new int[]{(roomInfo.isAttention==1?R.drawable.exit_room_info_icon:R.drawable.attention_info_icon),
                                R.drawable.share_info_icon,
                                R.drawable.room_info_icon});
            }
            pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {

                    maskingView.setVisibility(View.GONE);

                }
            });


        } else {
            if (pop.isShowing()) {
                maskingView.setVisibility(View.GONE);
                pop.dismiss();
            } else {
                maskingView.setVisibility(View.VISIBLE);

                if (Build.VERSION.SDK_INT >= 19) {
                    pop.showAsDropDown(rightImg, 0, 0, Gravity.END,
                            new String[]{(roomInfo.isAttention==1?"退出房间":"关注房间"), "分享房间", "玩法说明"},
                            new int[]{(roomInfo.isAttention==1?R.drawable.exit_room_info_icon:R.drawable.attention_info_icon),
                                    R.drawable.share_info_icon,
                                    R.drawable.room_info_icon});
                } else {
                    pop.showAsDropDown(rightImg, 0, 0, Gravity.END,
                            new String[]{(roomInfo.isAttention==1?"退出房间":"关注房间"), "分享房间", "玩法说明"},
                            new int[]{(roomInfo.isAttention==1?R.drawable.exit_room_info_icon:R.drawable.attention_info_icon),
                                    R.drawable.share_info_icon,
                                    R.drawable.room_info_icon});
                }
            }
        }
        pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0://关注房间
if(roomInfo.isAttention==1)
{
    attentionRoom(0);
}
                        else
{
    attentionRoom(1);
}

                        break;
                    case 1://分享房间
                        ShareUtils.shareRoom(ChatEnvelopeActivity.this,roomInfo);
                        break;
                    case 2://玩法帮助

                        Intent playIntent =new Intent(ChatEnvelopeActivity.this,PlayHelpActivity.class);
                        playIntent.putExtra("envelopeType",roomInfo.roomType);
                        startActivity(playIntent);

                        break;
                    case 3://


                        break;
                }
            }
        });


    }

    private void attentionRoom(final int type) {
        HttpRequest.attentionRoom(this, type, roomInfo.roomId, new HttpRequest.onRequestCallback() {
            @Override
            public void onSuccess(BaseInfo response) {
                if (HttpResponseUtil.isResponseOk(response)) {
                    if (type == CommonUtils.ATTENTION_ROOM) {
                        roomInfo.isAttention = 1;
                    } else {
                        roomInfo.isAttention = 0;
                    }

                    ToolUtils.showToast(ChatEnvelopeActivity.this, type == CommonUtils.ATTENTION_ROOM ? "关注房间成功" : "取消关注房间成功");
                } else {
                    ToolUtils.showToast(ChatEnvelopeActivity.this, type == CommonUtils.ATTENTION_ROOM ? "关注房间失败，请稍后再试" : "取消关注房间失败，请稍后再试");
                }
            }

            @Override
            public void onFailure(String rawJsonData) {
                ToolUtils.showToast(ChatEnvelopeActivity.this, type == CommonUtils.ATTENTION_ROOM ? "关注房间失败，请检查网络" : "取消关注房间失败，请检查网络");
            }
        });

    }


}
