package go.fast.fastenvelopes.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import dialogplus.DialogPlus;
import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.adapters.ChatlListAdapter;
import go.fast.fastenvelopes.adapters.FreeAnswerListAdapter;
import go.fast.fastenvelopes.http.Constant;
import go.fast.fastenvelopes.http.HttpRestClient;
import go.fast.fastenvelopes.info.BaseInfo;
import go.fast.fastenvelopes.info.FreeAnswerInfo;
import go.fast.fastenvelopes.info.MessageInfo;
import go.fast.fastenvelopes.info.MessageListInfo;
import go.fast.fastenvelopes.info.PushMsgInfo;
import go.fast.fastenvelopes.info.RoomItemInfo;
import go.fast.fastenvelopes.json.HttpRequest;
import go.fast.fastenvelopes.utils.CommonUtils;
import go.fast.fastenvelopes.utils.DeviceUuidFactory;
import go.fast.fastenvelopes.utils.HttpResponseUtil;
import go.fast.fastenvelopes.utils.PreferenceUtils;
import go.fast.fastenvelopes.utils.PublicViewUtils;
import go.fast.fastenvelopes.utils.ToolUtils;
import go.fast.fastenvelopes.view.NewCountDownTimer;
import me.maxwin.view.XListView;


/**
 * 会话页面Fragment
 */
public class ChatBaseActivity extends BaseNoPushActivity implements XListView.IXListViewListener {
    private static final String TAG = "FontsFragment";


    protected XListView chatListv;
    public static final int FRIST_GET_DATE = 111;// 首次加载
    public static final int REFRESH_GET_DATE = 112;// 刷新数据
    public static final int LOADMORE_GET_DATE = 113;// 获取更多数据
    private int page = 1;
    protected EditText msgEdit;

  //  protected EditText numEdit;
    protected Button sendBtn;
    protected Button envelopeBtn;
    protected List<MessageInfo> chatInfoList;

    protected ChatlListAdapter chatlListAdapter;
    protected RelativeLayout sendMsgLayout;

    protected RoomItemInfo roomInfo;

    protected View maskingView;

    protected View envelopePopView;

    protected View freeEnvelopePopView;

    protected View nearEnvelopePopView;
    //protected RelativeLayout numKeyboardLayout;

    //protected CustomKeyboard mCustomKeyboard;
    protected NewCountDownTimer updateTimer;

    protected NewCountDownTimer envelopeWaitingTimer;


    protected TextView countDownText;
    protected TextView countDownRightText;
    protected TextView startorJoinText;
    protected TextView joinSizeText;
    protected TextView needGoldTitleText;
    protected TextView needGoldText;
    protected RelativeLayout delayStartTimeLayout;
    protected RelativeLayout guessingLayout;
    protected ImageView currentHeadImg;
    protected TextView currentNameTextV;
    protected TextView currentAnswerTextV;
    protected TextView answerPrepareTextV;

    protected TextView freeJoinLeftT;//”已有“ 在红包结果出来后改为“共有”
    protected TextView freeAnswerText;//参与列表title
    protected TextView freeGoldSizeT;//自由猜红包的总金额
    protected TextView freeEnvelopeSizeT;
    protected TextView freeJoinSizeT;
    protected LinearLayout freeJoinSizeLayout;
    protected RelativeLayout freeDelayTimeLayout;//自由红包的倒计时
    protected TextView freeDelayTimeLT;
    protected TextView freeDelayTimeRT;
    protected ListView freeAnswerListV;//回答的列表
    protected FreeAnswerListAdapter freeAnswerListAdapter;
    protected List<FreeAnswerInfo> freeAnswerInfoList;


    protected  int isPopNeedShow;//(为1 表示此时该显示 猜金额的弹窗  2 表示显示 自由猜的弹窗)

    protected  int currentMsgType=CommonUtils.MESSAGE_TYPE_MSG_CONTENT;//当前消息类型
    protected TextView freeInstructText;



    protected TextView middleBottomView;//title下面的文字显示是否隐藏弹窗

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chat_layout);
        envelopePopView=findViewById(R.id.envelop_ic);
        freeEnvelopePopView=findViewById(R.id.free_envelop_ic);
        sendMsgLayout = (RelativeLayout) findViewById(R.id.send_comment_rl);
        chatListv = (XListView) findViewById(R.id.chat_lv);
        msgEdit = (EditText) findViewById(R.id.et_sendmessage);
        sendBtn = (Button) findViewById(R.id.btn_send);
        envelopeBtn = (Button) findViewById(R.id.startEnvelopeBtn);
        maskingView = findViewById(R.id.masking_v);




        freeJoinLeftT= (TextView)findViewById(R.id.free_join_left_size_tv);
        freeAnswerText= (TextView)findViewById(R.id.free_answer_tv);
        startorJoinText = (TextView) findViewById(R.id.joinorstart_tv);
        freeGoldSizeT=(TextView)findViewById(R.id.free_gold_size_tv);
        freeEnvelopeSizeT=(TextView)findViewById(R.id.free_envelope_size_tv);
        freeJoinSizeT=(TextView)findViewById(R.id.free_join_size_tv);
        freeInstructText=(TextView)findViewById(R.id.free_instruct_tv);
        freeJoinSizeLayout=(LinearLayout) findViewById(R.id.free_join_size_ll);
        freeDelayTimeLayout=(RelativeLayout) findViewById(R.id.free_delaystart_time_ll);
        freeDelayTimeLT= (TextView)findViewById(R.id.free_delaystart_time_tv);
        freeDelayTimeRT = (TextView)findViewById(R.id.free_delaystart_r_tv);
        freeAnswerListV = (ListView) findViewById(R.id.free_answer_lv);
        freeAnswerInfoList=new ArrayList<>();

        currentHeadImg=(ImageView)findViewById(R.id.currentHead_img);
        currentNameTextV=(TextView)findViewById(R.id.currentName_tv);
        currentAnswerTextV=(TextView)findViewById(R.id.currentAnswer_tv);
        answerPrepareTextV=(TextView)findViewById(R.id.answer_prepare_tv);
        guessingLayout=(RelativeLayout)findViewById(R.id.guessing_rl);
        delayStartTimeLayout=(RelativeLayout)findViewById(R.id.delaystart_time_ll);
        needGoldText = (TextView) findViewById(R.id.gold_size_tv);
        needGoldTitleText = (TextView)findViewById(R.id.gold_text_tv);
        joinSizeText = (TextView) findViewById(R.id.join_size_tv);
        countDownText = (TextView)findViewById(R.id.delaystart_time_tv);
        countDownRightText = (TextView)findViewById(R.id.delaystart_time_r_tv);


        //numKeyboardLayout=(RelativeLayout)findViewById(R.id.numkeyboard_rl);
        setInitView();
    }

    private TextView middleView;

    @Override
    public void initMiddle(TextView middleView) {
        super.initMiddle(middleView);
        middleView.setText("抢红包");
        this.middleView = middleView;
    }

//	@Override
//	public void initLeft(ImageView imageView, TextView textView, View parent) {
//		//super.initLeft(imageView, textView, parent);
//		parent.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//				if(roomInfo.isAttention==1)//已经关注的
//				{
//
//					inOrExitRoom(CommonUtils.ROOM_TYPE_OUT);
//
//				}
//				else//没有关注
//				{
//					inOrExitRoom(CommonUtils.ROOM_TYPE_EXIT);
//				}
//			}
//		});
//	}


    private void setInitView() {
        roomInfo = (RoomItemInfo) getIntent().getSerializableExtra("roomInfo");
        chatInfoList = new ArrayList<>();
        chatlListAdapter = new ChatlListAdapter(this,
                R.layout.chat_list_item, chatInfoList);
        chatListv.setAdapter(chatlListAdapter);
        msgEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        chatListv.setPullLoadEnable(false);
        chatListv.setPullRefreshEnable(true);
        chatListv.setXListViewListener(this);
        chatListv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dismissNumKeyBoard();
                return false;
            }
        });
        sendMsgLayout
                .addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onLayoutChange(View v, int left, int top,
                                               int right, int bottom, int oldLeft, int oldTop,
                                               int oldRight, int oldBottom) {
                        if (oldTop - top > 0)// 说明软件盘弹起
                        {
                            chatListv.smoothScrollToPosition(chatInfoList.size());

                        } else if (oldTop - top < 0)// 说明软件盘放下
                        {
                            msgEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                        }
                    }
                });
        if (roomInfo != null) {
            middleView.setText(roomInfo.roomName);
        }
        inOrExitRoom(CommonUtils.ROOM_TYPE_IN);
        if (CommonUtils.isNetWorkConnected(this)) {
            sendRequest(FRIST_GET_DATE);// 请求数据
        }

    }
    public void sendRequest(final int ACTION) {
        switch (ACTION) {
            case FRIST_GET_DATE:// 第一次加载数据
                requestParams(FRIST_GET_DATE);
                break;
            case REFRESH_GET_DATE:// 刷新数据
                requestParams(REFRESH_GET_DATE);
                break;
            case LOADMORE_GET_DATE:// 获取更多数据
                requestParams(LOADMORE_GET_DATE);
                break;
        }

    }
    protected void lockSendButton() {
        sendBtn.setClickable(false);


    }

    protected void unlockSendButton() {
        sendBtn.setClickable(true);
    }

    protected void showNumKeyBoard() {
        msgEdit.setText("");
        msgEdit.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//		imm.showSoftInput(msgEdit,InputMethodManager.SHOW_FORCED);

        chatListv.smoothScrollToPosition(chatInfoList.size());
    }

    protected void dismissNumKeyBoard() {
        msgEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        hideSoftInput();

    }

    protected void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(msgEdit.getWindowToken(), 0);
        // contentEdit.setText("");
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        stopMsgDelay();
        cancelWaitingTimer();//取消定时器
        if (roomInfo.isAttention == 1)//已经关注的
        {

            inOrExitRoom(CommonUtils.ROOM_TYPE_OUT);

        } else//没有关注
        {
            inOrExitRoom(CommonUtils.ROOM_TYPE_EXIT);
        }

    }


    @Override
    public void onRefresh() {
        requestParams(REFRESH_GET_DATE);
    }

    @Override
    public void onLoadMore() {

    }


    protected void sendMsg() {
        if (ToolUtils.isNullOrEmpty(msgEdit.getText().toString())) {
            ToolUtils.showToast(this, "请输入要发送的内容");
        } else {
            //hideSoftInput();
            sendMessage();
        }

    }

    //关闭超时消息定时器
    protected void stopMsgDelay() {
        if (updateTimer != null) {
            updateTimer.cancel();
            updateTimer = null;
            unlockSendButton();
        }
    }

    protected void startMsgDelay() {

        if (updateTimer != null) {
            updateTimer.cancel();
            updateTimer.start();
        } else {
            updateTimer = new NewCountDownTimer(45 * 1000, 4 * 1000) {

                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {

                    ToolUtils.showToast(ChatBaseActivity.this, "发送消息失败");
                    chatInfoList.remove(chatInfoList.size() - 1);
                    chatlListAdapter.notifyDataSetChanged();
                    unlockSendButton();

                }
            };

            updateTimer.start();
        }

    }


    private void sendMessage() {

        if(currentMsgType==CommonUtils.MESSAGE_TYPE_MSG_CONTENT)//普通消息
        {
            MessageInfo msgInfo = new MessageInfo();
            msgInfo.roomId = roomInfo.roomId;
            msgInfo.account = PreferenceUtils.getInstance(this).getSettingUserAccount();
            msgInfo.content = msgEdit.getText().toString();
            msgInfo.headurl = PreferenceUtils.getInstance(this).getSettingUserPic();
            msgInfo.createdAt = System.currentTimeMillis();
            msgInfo.chatType = CommonUtils.MESSAGE_TYPE_MSG_CONTENT;
            msgInfo.isPreLoading = true;
            chatInfoList.add(msgInfo);
            chatlListAdapter.notifyDataSetChanged();
            chatListv.smoothScrollToPosition(chatInfoList.size());
        }
        else
        if(currentMsgType==CommonUtils.MESSAGE_TYPE_MSG_GUESSING_ENVELOPE)//正在猜的消息
        {
            MessageInfo msgInfo = new MessageInfo();
            msgInfo.roomId = roomInfo.roomId;
            msgInfo.account = PreferenceUtils.getInstance(this).getSettingUserAccount();
            msgInfo.content = msgEdit.getText().toString();
            msgInfo.envelopeType=roomInfo.roomType;
            msgInfo.headurl = PreferenceUtils.getInstance(this).getSettingUserPic();
            msgInfo.createdAt = System.currentTimeMillis();
            msgInfo.chatType = CommonUtils.MESSAGE_TYPE_MSG_GUESSING_ENVELOPE;
            msgInfo.isPreLoading = true;
            chatInfoList.add(msgInfo);
            chatlListAdapter.notifyDataSetChanged();
            chatListv.smoothScrollToPosition(chatInfoList.size());
        }

        lockSendButton();
        startMsgDelay();
        HttpRequest.sendMsg(this, roomInfo.roomId, currentMsgType, msgEdit.getText().toString(), new HttpRequest.onRequestCallback() {
            @Override
            public void onSuccess(BaseInfo response) {
                if (HttpResponseUtil.isResponseOk(response)) {

                } else//发送消息不成功
                {
                    stopMsgDelay();//接收消息成功取消超时定时器
//                    if(response.getCode().equals("3010"))
//                    {
                        ToolUtils.showToast(ChatBaseActivity.this, response.msg);
                   // }
                    if(currentMsgType==CommonUtils.MESSAGE_TYPE_MSG_CONTENT||currentMsgType==CommonUtils.MESSAGE_TYPE_MSG_GUESSING_ENVELOPE)
                    {
                        chatInfoList.remove(chatInfoList.size() - 1);
                        chatlListAdapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onFailure(String rawJsonData) {
                System.out.println("   发送自由猜失败---   "+rawJsonData);
                stopMsgDelay();
                if(currentMsgType==CommonUtils.MESSAGE_TYPE_MSG_CONTENT||currentMsgType==CommonUtils.MESSAGE_TYPE_MSG_GUESSING_ENVELOPE)
                {
                    chatInfoList.remove(chatInfoList.size() - 1);
                    chatlListAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    protected void inOrExitRoom(int type) {
        if (roomInfo == null) {
            ToolUtils.showToast(this, "获取房间信息失败，请稍后再试");
            finish();
            return;
        }
        HttpRequest.In0rExitRoom(this, roomInfo.roomId, type, new HttpRequest.onRequestCallback() {
            @Override
            public void onSuccess(BaseInfo response) {
                RoomItemInfo roomItemInfo = (RoomItemInfo) response;
                if (HttpResponseUtil.isResponseOk(response)) {
                    roomInfo.isAttention = roomItemInfo.isAttention;
                } else {
                    if (response.getCode().equals("1999")) {
                        //JumpUtils.
                        Intent loginIntent = new Intent(ChatBaseActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                        ToolUtils.showToast(ChatBaseActivity.this, "请先登录");
                        //finish();
                    } else if (response.getCode().equals("2009")) {
                        ToolUtils.showToast(ChatBaseActivity.this, "房间已经满员了，请稍后再进");
                        finish();
                    } else if (response.getCode().equals("2004")) {
                        ToolUtils.showToast(ChatBaseActivity.this, "用户不存在");
                        finish();
                    }
                    else if (response.getCode().equals("2011")) {//当前有红包正在进行，无法进入
                        ToolUtils.showToast(ChatBaseActivity.this,response.msg);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(String rawJsonData) {

            }
        });
    }

   protected void dismissEnvelopeLiving()
   {
       envelopePopView.setVisibility(View.INVISIBLE);
       envelopePopView.startAnimation(AnimationUtils.loadAnimation(this,R.anim.slide_out_top));
   }

    public void showEnvelopeLiving(final PushMsgInfo pushMsgInfo) {
//        if(envelopePopView.isShown())
//        {
//            envelopePopView.setVisibility(View.GONE);
//            envelopePopView.startAnimation(AnimationUtils.loadAnimation(this,R.anim.slide_out_top));


      //  }
        isPopNeedShow=1;//标识显示猜金额弹窗
        middleBottomView.setVisibility(View.VISIBLE);
        middleBottomView.setText("(点我隐藏)");
        envelopePopView.setVisibility(View.VISIBLE);
        envelopePopView.startAnimation(AnimationUtils.loadAnimation(this,R.anim.slide_in_top));

        needGoldText.setText(pushMsgInfo.envelopeMoney+"");

        needGoldTitleText.setText("需要金币");

        joinSizeText.setText(pushMsgInfo.envelopeJoinSize + "");

        countDownRightText.setText("后自动开始猜红包游戏");
//        String[] times = TimeUtils.getCountDownTimes(3*60*1000,startTimeStamp);//总倒计时3分钟
//        countDownText.setText(times[0] + "分" + times[1] + "秒后自动开始");
        startWaitingTimer(pushMsgInfo,CommonUtils.ENVELOPE_TYPE_GUESS,30);

        delayStartTimeLayout.setVisibility(View.VISIBLE);
        startorJoinText.setVisibility(View.VISIBLE);
        currentHeadImg.setVisibility(View.INVISIBLE);
        currentNameTextV.setVisibility(View.INVISIBLE);
        answerPrepareTextV.setVisibility(View.INVISIBLE);
        guessingLayout.setVisibility(View.INVISIBLE);
        if(PreferenceUtils.getInstance(this).getSettingUserAccount().equals(pushMsgInfo.account))//红包是自己发的
        {
            startorJoinText.setText("立即开始");
        }
        else
        {
            startorJoinText.setText("立即参与");
        }

        startorJoinText.setOnClickListener(new View.OnClickListener() {//立即开始或者参与
            @Override
            public void onClick(View v) {

                if(PreferenceUtils.getInstance(ChatBaseActivity.this).getSettingUserAccount().equals(pushMsgInfo.account))//红包是自己发的
                {
                    startGoEnvelope(pushMsgInfo);
                }
                else
                {
                    joinGuessEnvelope( pushMsgInfo);
                }
            }
        });

     //   rechargeDialog.show();

    }


    protected void dismissFreeEnvelopePop()
    {
        freeEnvelopePopView.setVisibility(View.INVISIBLE);
        freeEnvelopePopView.startAnimation(AnimationUtils.loadAnimation(this,R.anim.slide_out_top));
    }

    public void showFreeEnvelopePop(final PushMsgInfo pushMsgInfo) {
        if(envelopePopView.isShown())
        {
            envelopePopView.setVisibility(View.GONE);
            envelopePopView.startAnimation(AnimationUtils.loadAnimation(this,R.anim.slide_out_top));
          }
        middleBottomView.setVisibility(View.VISIBLE);
        middleBottomView.setText("(点我隐藏)");

        freeDelayTimeRT.setVisibility(View.VISIBLE);
        freeDelayTimeLT.setText("30分00秒");
        isPopNeedShow=2;//标识显示自由猜弹窗
        freeAnswerInfoList.clear();//清空已有的
        freeEnvelopePopView.setVisibility(View.VISIBLE);
        freeEnvelopePopView.startAnimation(AnimationUtils.loadAnimation(this,R.anim.slide_in_top));
        startWaitingTimer(pushMsgInfo,CommonUtils.ENVELOPE_TYPE_FREE,30);
        freeEnvelopeSizeT.setText(""+pushMsgInfo.envelopeCounts);
        freeGoldSizeT.setText(""+pushMsgInfo.envelopeMoneySize);
        freeAnswerListAdapter=new FreeAnswerListAdapter(this,R.layout.free_answer_list_item,freeAnswerInfoList);
        freeAnswerListV.setAdapter(freeAnswerListAdapter);
    }

    private void joinGuessEnvelope(final PushMsgInfo msgInfo)
    {

        PublicViewUtils.showConsumeGold(this, "花费" + msgInfo.envelopeMoney + "金币参与抢红包", msgInfo.envelopeMoney, new PublicViewUtils.OnPayGoldListener() {
            @Override
            public void payGold(final DialogPlus dialog) {

                HttpRequest.joinEnvelope(ChatBaseActivity.this, msgInfo, new HttpRequest.onRequestCallback() {
                    @Override
                    public void onSuccess(BaseInfo response) {

                        if(HttpResponseUtil.isResponseOk(response))
                        {
                            dialog.dismiss();

                        }
                        else
                        {
                                ToolUtils.showToast(ChatBaseActivity.this,response.msg);
                        }
                    }

                    @Override
                    public void onFailure(String rawJsonData) {

                    }
                });

            }
        });


    }

    /**
     * 开始红包游戏
     * @param msgInfo
     */
    private void startGoEnvelope(final PushMsgInfo msgInfo)
    {

        HttpRequest.goingEnvelope(ChatBaseActivity.this, msgInfo, new HttpRequest.onRequestCallback() {
            @Override
            public void onSuccess(BaseInfo response) {

                if(HttpResponseUtil.isResponseOk(response))
                {
                }
                else
                {
                    ToolUtils.showToast(ChatBaseActivity.this,response.msg);
                }
            }

            @Override
            public void onFailure(String rawJsonData) {

            }
        });


    }

    protected void startWaitingTimer(final PushMsgInfo pushMsgInfo,final int envelopeType,final int  delayMin) {

        if (envelopeWaitingTimer != null) {
            envelopeWaitingTimer.cancel();
            envelopeWaitingTimer.start();
        } else {
            envelopeWaitingTimer = new NewCountDownTimer(100*60* 1000,  1000) {

                @Override
                public void onTick(long millisUntilFinished)
                {
                    String[] countDownTimes=new String[2];
                    int[] times = new int[2];
                    System.out.println("   startWaitingTimer     当前时间： "+System.currentTimeMillis()+"        "+pushMsgInfo.getCreatedAt()
                    +"          "+times);
                    long lastTime = delayMin
                            * 60
                            * 1000
                            - (System.currentTimeMillis() - pushMsgInfo.getCreatedAt());
                    if (lastTime < 0) {// 说明时间到了。将自动开始红包
                        if (envelopeWaitingTimer != null) {
                            envelopeWaitingTimer.cancel();
                            envelopeWaitingTimer=null;
                            timeUp( envelopeType);//时间到给用户的提示
                            return;
                        }

                    } else {
                        times[0] = ToolUtils
                                .getTimeByMS(lastTime)[0];


                        times[1] = ToolUtils
                                .getTimeByMS(lastTime)[1];

                    }
                    if(envelopeType==CommonUtils.ENVELOPE_TYPE_GUESS)
                    {
                        countDownText.setText((times[0]<10?("0"+times[0]) :times[0]) + "分" +(times[1]<10?("0"+times[1]) :times[1])  + "秒");
                    }
                    else
                    if(envelopeType==CommonUtils.ENVELOPE_TYPE_FREE)
                    {
                        freeDelayTimeLT.setText((times[0]<10?("0"+times[0]) :times[0] )+ "分" +(times[1]<10?("0"+times[1]) :times[1])  + "秒");
                    }
                    else//近者得
                    {

                    }

                }

                @Override
                public void onFinish() {


                }
            };

            envelopeWaitingTimer.start();
        }
    }

    private void timeUp(int envelopeType)
    {

        if(envelopeType==CommonUtils.ENVELOPE_TYPE_GUESS)
        {

          countDownText.setText(roomInfo.currentCreateEnvelopeNickname+"未在指定时间开始，红包将在一分钟内开始或取消");
            countDownRightText.setText("");
        }
        else  if(envelopeType==CommonUtils.ENVELOPE_TYPE_FREE)
        {

            freeDelayTimeLT.setText("未在规定时间内达到指定的红包人数，系统将在一分钟内取消或分发红包给已参与的人。");
            freeDelayTimeRT.setVisibility(View.GONE);
        }




    }

    private void cancelWaitingTimer()
    {
        if (envelopeWaitingTimer != null) {
            envelopeWaitingTimer.cancel();
            envelopeWaitingTimer=null;
        }
    }

    private void onLoad(int action)
    {// 防止多个线程同时调用
        // TODO Auto-generated method stub
        chatListv.stopRefresh();
        chatListv.stopLoadMore();
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy年MM月dd日   HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        chatListv.setRefreshTime(str);
        chatlListAdapter.notifyDataSetChanged();
        if(action==FRIST_GET_DATE)
        {
            chatListv.setSelection(chatInfoList.size());
        }
    }

    private void requestParams(final int action) {


        new Thread(new Runnable() {

            @Override
            public void run() {
                RequestParams params = new RequestParams();
                DeviceUuidFactory uuid = new DeviceUuidFactory(ChatBaseActivity.this);
                String uid = uuid.getDeviceUuid().toString(); // 获取设备唯一id
                params.put("access_token", uid);
                params.put("roomId", roomInfo.roomId);
                params.put("account", PreferenceUtils
                        .getInstance(ChatBaseActivity.this).getSettingUserAccount());
                    params.put("page", page);// 当前页
                Looper.prepare();
                HttpRestClient.post(Constant.GET_MSG_LIST, params,
                        new BaseJsonHttpResponseHandler<MessageListInfo>(
                                MessageListInfo.class) {

                            @Override
                            public void onSuccess(int statusCode,
                                                  Header[] headers, String rawJsonResponse,
                                                  final MessageListInfo response) {


                                System.out
                                        .println("GET_MSG_LIST--------------"
                                                + rawJsonResponse);
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {


                                        MessageListInfo messageListInfo=(MessageListInfo) response;
                                        if (HttpResponseUtil
                                                .isResponseOk(response)) {

                                            switch (action) {
                                                case FRIST_GET_DATE:
                                                case REFRESH_GET_DATE:
                                                    if (chatInfoList != null) {
                                                        chatInfoList.addAll(0,messageListInfo.list);
                                                        page++;
                                                    }
                                                    break;
                                            }
                                        }

                                        onLoad(action);
                                    }
                                });
                            }
                            @Override
                            public void onFailure(int statusCode,
                                                  Header[] headers, Throwable throwable,
                                                  String rawJsonData,
                                                  MessageListInfo errorResponse) {
                                System.out.println("onFailure--------------"
                                        + rawJsonData + "  statusCode  "
                                        + statusCode);
                               runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {

                                        onLoad(action);
                                    }
                                });

                            }



                            @Override
                            protected MessageListInfo parseResponse(
                                    String rawJsonData, boolean isFailure)
                                    throws Throwable {
                                // TODO Auto-generated method stub
                                return null;
                            }

                        });

                Looper.loop();

            }
        }).start();

    }

}
