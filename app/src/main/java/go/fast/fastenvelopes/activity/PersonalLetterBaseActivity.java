package go.fast.fastenvelopes.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import go.fast.fastenvelopes.R;
import go.fast.fastenvelopes.adapters.PersonalLetterListAdapter;
import go.fast.fastenvelopes.db.CoolDBHelper;
import go.fast.fastenvelopes.info.BaseInfo;
import go.fast.fastenvelopes.info.MessageInfo;
import go.fast.fastenvelopes.info.SessionItemInfo;
import go.fast.fastenvelopes.info.UserInfo;
import go.fast.fastenvelopes.json.HttpRequest;
import go.fast.fastenvelopes.utils.HttpResponseUtil;
import go.fast.fastenvelopes.utils.PreferenceUtils;
import go.fast.fastenvelopes.utils.ToolUtils;
import go.fast.fastenvelopes.view.NewCountDownTimer;
import me.maxwin.view.XListView;


/**
 * 会话页面Fragment
 */
public class PersonalLetterBaseActivity extends BaseNoPushActivity implements XListView.IXListViewListener {
    private static final String TAG = "FontsFragment";


	protected 	XListView chatListv;
	protected EditText msgEdit;

	protected EditText numEdit;
	protected Button sendBtn;
	protected List<MessageInfo> chatInfoList;
	protected PersonalLetterListAdapter personalLetterListAdapter;
	protected RelativeLayout sendMsgLayout;
	protected NewCountDownTimer updateTimer;

//	private String userAccount;
//	private String userNick;
protected UserInfo userInfo;

	protected long  historyStartTime;
	//protected RelativeLayout numKeyboardLayout;

	//protected CustomKeyboard mCustomKeyboard;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

		setContentView(R.layout.personal_chat_layout);

		numEdit=(EditText)findViewById(R.id.num_et);
        sendMsgLayout=(RelativeLayout)findViewById(R.id.send_comment_rl);
		chatListv=(XListView)findViewById(R.id.chat_lv) ;
		msgEdit=(EditText)findViewById(R.id.et_sendmessage);
		sendBtn=(Button)findViewById(R.id.btn_send);
		//numKeyboardLayout=(RelativeLayout)findViewById(R.id.numkeyboard_rl);
		setInitView();
    }

	TextView middleText;
	@Override
	public void initMiddle(TextView middleView) {
		super.initMiddle(middleView);
		middleView.setText("抢红包");
		middleText=middleView;
	}

	private void setInitView() {

		userInfo= (UserInfo) getIntent().getSerializableExtra("userInfo");
		if(userInfo!=null)
		{
			middleText.setText(userInfo.nickName);
		}
		chatInfoList=new ArrayList<>();
		personalLetterListAdapter= new PersonalLetterListAdapter(this,
				R.layout.chat_list_item, chatInfoList);
		chatListv.setAdapter(personalLetterListAdapter);
		msgEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
		chatListv.setPullLoadEnable(false);
		chatListv.setPullRefreshEnable(true);
		chatListv.setXListViewListener(this);


//
//		mCustomKeyboard = new CustomKeyboard(this, R.id.keyboardview,
//				R.xml.symbols);
//
//		mCustomKeyboard.registerEditText(R.id.num_et);


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
							//chatListv.setSelection(chatInfoList.size());

						}
						else if (oldTop - top < 0)// 说明软件盘放下
						{
							msgEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
							//msgEdit.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
						}
					}
				});
		getMessageList();

    }





	protected void hideSoftInput() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(msgEdit.getWindowToken(), 0);
		// contentEdit.setText("");
	}




	@Override
	public void onRefresh() {
		getMessageList();
	}

	private void getMessageList() {

		List<MessageInfo> messageInfoList= CoolDBHelper.getMessageListByPublishTime(this,
				userInfo.account,historyStartTime, 15);

		if (messageInfoList.size() > 0) {//如果有拉取到新的数据那么刷新

			chatInfoList.addAll(0,messageInfoList);
			personalLetterListAdapter.notifyDataSetChanged();
			historyStartTime=messageInfoList.get(0).createdAt;
			chatListv.setSelection(chatInfoList.size());
//			if (chatInfoList != null) {
//				chatListv.notifyDataSetChanged(chatInfoList.size(), 15);
//			}
		} else {
			if(chatInfoList.size()>0)
			{
				ToolUtils.showToast(this, "没有更多的消息了");
			}
		}

		chatListv.stopRefresh();
		chatListv.stopLoadMore();

//		if(chatInfoList.size()==0)
//		{
//			emptyView.setVisibility(View.VISIBLE);
//		}
//		else
//		{
//			emptyView.setVisibility(View.GONE);
//		}
	}

	@Override
	public void onLoadMore() {

	}


	protected void lockSendButton() {
		sendBtn.setClickable(false);


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

					ToolUtils.showToast(PersonalLetterBaseActivity.this, "发送消息失败");
					chatInfoList.remove(chatInfoList.size() - 1);
					personalLetterListAdapter.notifyDataSetChanged();
					unlockSendButton();

				}
			};

			updateTimer.start();
		}

	}

	protected void unlockSendButton() {
		sendBtn.setClickable(true);
	}
	protected void sendMsg()
	{
		if(ToolUtils.isNullOrEmpty(msgEdit.getText().toString()))
		{
			ToolUtils.showToast(this,"请输入要发送的内容");
		}
		else
		{
			//hideSoftInput();
			MessageInfo msgInfo = new MessageInfo();
			msgInfo.account = PreferenceUtils.getInstance(this).getSettingUserAccount();
			msgInfo.content = msgEdit.getText().toString();
			msgInfo.headurl = PreferenceUtils.getInstance(this).getSettingUserPic();
			msgInfo.createdAt = System.currentTimeMillis();
			msgInfo.isPreLoading = true;
			chatInfoList.add(msgInfo);
			personalLetterListAdapter.notifyDataSetChanged();
			chatListv.smoothScrollToPosition(chatInfoList.size());
			sendLetter(msgInfo.content);
		}

	}


	private void sendLetter(String content)
	{
		HttpRequest.sendPersonalLetter(this, userInfo.account, content, new HttpRequest.onRequestCallback() {
			@Override
			public void onSuccess(BaseInfo response) {
				stopMsgDelay();//接收消息成功取消超时定时器
				if(HttpResponseUtil.isResponseOk(response))
				{
					MessageInfo msgInfo = new MessageInfo();
					msgInfo.account = PreferenceUtils.getInstance(PersonalLetterBaseActivity.this).getSettingUserAccount();
					msgInfo.content = msgEdit.getText().toString();
					msgInfo.nickname=PreferenceUtils.getInstance(PersonalLetterBaseActivity.this).getSettingUserNickName();
					msgInfo.headurl = PreferenceUtils.getInstance(PersonalLetterBaseActivity.this).getSettingUserPic();
					msgInfo.createdAt = System.currentTimeMillis();
					chatInfoList.set(chatInfoList.size()-1,msgInfo);
					personalLetterListAdapter.notifyDataSetChanged();
					chatListv.smoothScrollToPosition(chatInfoList.size());

					CoolDBHelper.insertMessage(PersonalLetterBaseActivity.this,userInfo.account,msgInfo);//更新聊天记录表
					SessionItemInfo sessionItemInfo=new SessionItemInfo();
					sessionItemInfo.lastContent= msgEdit.getText().toString();
					sessionItemInfo.lastPublishTime=System.currentTimeMillis();
					sessionItemInfo.account=userInfo.account;
					sessionItemInfo.nickname=userInfo.nickName;
					sessionItemInfo.headurl=userInfo.headurl;
					sessionItemInfo.sex=userInfo.sex;
					sessionItemInfo.level=userInfo.level;
					CoolDBHelper.insertOrUpdateSessionListByAccount(PersonalLetterBaseActivity.this,sessionItemInfo);//更新会话表
					msgEdit.setText("");
				}
				else
				{

					if(response.getCode().equals("1999"))
					{
						HttpRequest.reLogin(PersonalLetterBaseActivity.this);
					}
					else
					{
						ToolUtils.showToast(PersonalLetterBaseActivity.this,response.msg);
					}
					chatInfoList.remove(chatInfoList.size() - 1);
					personalLetterListAdapter.notifyDataSetChanged();
				}
			}

			@Override
			public void onFailure(String rawJsonData) {
				stopMsgDelay();
				chatInfoList.remove(chatInfoList.size() - 1);
				personalLetterListAdapter.notifyDataSetChanged();
			}
		});

	}
}
